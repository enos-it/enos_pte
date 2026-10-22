package com.enos.enospte.data.model

data class Meter(
    val id: String,              // ID števca; kasneje naj bo enak Excel / SQL ID-ju
    val site: String,            // npr. "BELA", "JESENICE"

    val group: String = "OSTALO",        // npr. "KOTLI", "SPTE", "VODARNA"
    val subgroup: String = "",           // npr. "KOTEL 1", "SPTE 1", "GLAVNI ŠTEVEC"

    val name: String,            // npr. "Plinski števec", "Števec ur"
    val unit: String,            // npr. "m3", "h", "kWh", "bar"
    val decimals: Int = 0,
    val active: Boolean = true
)