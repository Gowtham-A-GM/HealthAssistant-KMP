package com.example.healthassistant.core.pdf

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.healthassistant.domain.model.assessment.Report
import java.io.ByteArrayOutputStream
import java.io.File

// ─── Public entry point ───

fun generateAndSavePdf(context: Context, report: Report): String {
    val pdfBytes = buildReportPdf(report)
    val fileName = "HealthReport_${report.topic.replace(" ", "_")}_${report.reportId.take(8)}.pdf"

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveViaMediaStore(context, fileName, pdfBytes)
        "Saved to Downloads: $fileName"
    } else {
        saveToExternalFiles(context, fileName, pdfBytes)
        "Saved to app Downloads folder: $fileName"
    }
}

// ─── MediaStore save (API 29+) ───

private fun saveViaMediaStore(context: Context, fileName: String, pdfBytes: ByteArray) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
        put(MediaStore.Downloads.IS_PENDING, 1)
    }
    val uri = context.contentResolver.insert(
        MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues
    ) ?: return
    context.contentResolver.openOutputStream(uri)?.use { it.write(pdfBytes) }
    contentValues.clear()
    contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
    context.contentResolver.update(uri, contentValues, null, null)
}

// ─── Fallback save (API < 29) ───

private fun saveToExternalFiles(context: Context, fileName: String, pdfBytes: ByteArray) {
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: return
    File(dir, fileName).writeBytes(pdfBytes)
}

// ─── PDF builder ───

private fun buildReportPdf(report: Report): ByteArray {
    val document = PdfDocument()
    val state = PdfState(document)

    val margin = 40f
    val pageWidth = 595f

    val boldPaint = Paint().apply { isAntiAlias = true; typeface = Typeface.DEFAULT_BOLD }
    val normalPaint = Paint().apply { isAntiAlias = true; typeface = Typeface.DEFAULT }
    val primaryBlue = Color.rgb(28, 77, 141)

    // ── Title ──
    boldPaint.textSize = 22f
    boldPaint.color = Color.BLACK
    state.canvas.drawText("Assessment Report", margin, state.y, boldPaint)
    state.y += 30f

    // ── Divider ──
    normalPaint.color = primaryBlue
    normalPaint.strokeWidth = 1.5f
    state.canvas.drawLine(margin, state.y, pageWidth - margin, state.y, normalPaint)
    state.y += 18f

    // ── Date / Patient ──
    normalPaint.textSize = 10f
    normalPaint.color = primaryBlue
    state.canvas.drawText(report.generatedAt, margin, state.y, normalPaint)
    state.y += 16f
    normalPaint.color = Color.BLACK
    val patInfo = report.patientInfo
    if (patInfo != null) {
        state.canvas.drawText("${patInfo.name}, ${patInfo.gender}, ${patInfo.age} years old", margin, state.y, normalPaint)
        state.y += 16f
    }

    // ── Primary Symptom + Urgency ──
    boldPaint.textSize = 11f
    boldPaint.color = Color.BLACK
    state.canvas.drawText("Primary Symptom: ${report.topic}", margin, state.y, boldPaint)
    boldPaint.color = when {
        report.urgencyLevel.contains("emergency") -> Color.RED
        report.urgencyLevel.contains("doctor") || report.urgencyLevel.contains("yellow") -> Color.rgb(255, 160, 0)
        else -> Color.rgb(46, 125, 50)
    }
    val urgencyText = when {
        report.urgencyLevel.contains("emergency") -> "EMERGENCY"
        report.urgencyLevel.contains("doctor") || report.urgencyLevel.contains("yellow") -> "DOCTOR VISIT"
        else -> "SELF CARE"
    }
    state.canvas.drawText(urgencyText, pageWidth - margin - boldPaint.measureText(urgencyText), state.y, boldPaint)
    boldPaint.color = Color.BLACK
    state.y += 24f

    // ── Summary ──
    state.checkBreak(40f)
    boldPaint.textSize = 13f
    state.canvas.drawText("Summary", margin, state.y, boldPaint)
    state.y += 18f
    normalPaint.textSize = 10f
    normalPaint.color = Color.BLACK
    report.summary.forEach { item ->
        state.checkBreak(20f)
        state.y = drawWrapped(state.canvas, "• $item", margin, state.y, pageWidth - margin * 2, normalPaint)
    }
    state.y += 14f

    // ── Possible Causes ──
    state.checkBreak(40f)
    boldPaint.textSize = 13f
    state.canvas.drawText("Possible Causes", margin, state.y, boldPaint)
    state.y += 18f
    report.possibleCauses.forEachIndexed { i, cause ->
        state.checkBreak(60f)
        boldPaint.textSize = 11f
        state.y = drawWrapped(state.canvas, "${i + 1}. ${cause.title}", margin, state.y, pageWidth - margin * 2, boldPaint)
        normalPaint.textSize = 10f
        state.y = drawWrapped(state.canvas, cause.shortDescription, margin + 8f, state.y, pageWidth - margin * 2 - 8f, normalPaint)

        // Progress bar
        state.checkBreak(24f)
        val outOf10 = (cause.detail.percentage / 10).coerceIn(0, 10)
        normalPaint.color = primaryBlue
        state.canvas.drawText("$outOf10 out of 10 has this", margin + 8f, state.y, normalPaint)
        state.y += 14f
        val barPaint = Paint().apply { isAntiAlias = true }
        val barWidth = (pageWidth - margin * 2) * 0.6f
        // Track
        barPaint.color = Color.rgb(220, 220, 220)
        state.canvas.drawRoundRect(RectF(margin + 8f, state.y - 8f, margin + 8f + barWidth, state.y - 2f), 4f, 4f, barPaint)
        // Fill
        barPaint.color = when (cause.severity) {
            "severe" -> Color.RED
            "moderate" -> Color.rgb(255, 160, 0)
            else -> primaryBlue
        }
        val fillWidth = barWidth * (cause.detail.percentage / 100f)
        if (fillWidth > 0f)
            state.canvas.drawRoundRect(RectF(margin + 8f, state.y - 8f, margin + 8f + fillWidth, state.y - 2f), 4f, 4f, barPaint)
        state.y += 10f
        normalPaint.color = Color.BLACK
    }
    state.y += 14f

    // ── Advice ──
    state.checkBreak(40f)
    boldPaint.textSize = 13f
    state.canvas.drawText("What you were advised", margin, state.y, boldPaint)
    state.y += 18f
    normalPaint.textSize = 10f
    report.advice.forEach { item ->
        state.checkBreak(20f)
        state.y = drawWrapped(state.canvas, "• $item", margin, state.y, pageWidth - margin * 2, normalPaint)
    }

    state.finish()
    val stream = ByteArrayOutputStream()
    document.writeTo(stream)
    document.close()
    return stream.toByteArray()
}

// ─── Helpers ───

private class PdfState(val document: PdfDocument) {
    private val pageWidth = 595
    private val pageHeight = 842
    private var pageNum = 1

    var page: PdfDocument.Page = document.startPage(
        PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create()
    )
    var canvas: Canvas = page.canvas
    var y: Float = 60f

    fun checkBreak(needed: Float) {
        if (y + needed > pageHeight - 50f) {
            document.finishPage(page)
            pageNum++
            page = document.startPage(
                PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create()
            )
            canvas = page.canvas
            y = 60f
        }
    }

    fun finish() {
        document.finishPage(page)
    }
}

private fun drawWrapped(canvas: Canvas, text: String, x: Float, y: Float, maxW: Float, paint: Paint): Float {
    val words = text.split(" ")
    var line = ""
    var yCur = y
    for (word in words) {
        val test = if (line.isEmpty()) word else "$line $word"
        if (paint.measureText(test) > maxW && line.isNotEmpty()) {
            canvas.drawText(line, x, yCur, paint)
            yCur += paint.textSize + 3f
            line = word
        } else {
            line = test
        }
    }
    if (line.isNotEmpty()) {
        canvas.drawText(line, x, yCur, paint)
        yCur += paint.textSize + 3f
    }
    return yCur
}
