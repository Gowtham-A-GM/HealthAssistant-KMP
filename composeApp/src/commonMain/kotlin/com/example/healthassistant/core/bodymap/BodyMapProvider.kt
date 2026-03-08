package com.example.healthassistant.core.bodymap

expect object BodyMapProvider {

    fun loadRegions(): List<BodyRegion>

}