package com.enos.enospte.data.source

import com.enos.enospte.data.model.Meter

object MeterCatalog {
    fun getMeters(): List<Meter> = buildList {
        addAll(belaMeters())
        addAll(jeseniceMeters())
    }

    private fun belaMeters() = listOf(
        Meter(
            id = "bela_bosch_zagoni",
            site = "BELA",
            group = "KOTLI",
            subgroup = "BOSCH",
            name = "Števec Zagonov",
            unit = "",
            decimals = 0
        ),
        Meter(
            id = "bela_bosch_ure",
            site = "BELA",
            group = "KOTLI",
            subgroup = "BOSCH",
            name = "Števec Ur",
            unit = "h",
            decimals = 0
        ),
        Meter(
            id = "bela_bosch_plinkorigirani",
            site = "BELA",
            group = "KOTLI",
            subgroup = "BOSCH",
            name = "Korigiran Plinski Števec",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "bela_bosch_plinmehanski",
            site = "BELA",
            group = "KOTLI",
            subgroup = "BOSCH",
            name = "Mehanski Plinski Števec",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "bela_loos_ure",
            site = "BELA",
            group = "KOTLI",
            subgroup = "LOOS",
            name = "Števec Ur",
            unit = "h",
            decimals = 0
        ),
        Meter(
            id = "bela_loos_zagoni",
            site = "BELA",
            group = "KOTLI",
            subgroup = "LOOS",
            name = "Števec Zagonov",
            unit = "",
            decimals = 0
        ),
        Meter(
            id = "bela_glavni_plinkorigirani",
            site = "BELA",
            group = "GLAVNI",
            subgroup = "PLIN",
            name = "Korigirani Plinski Števec",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "bela_glavni_plinmehanski",
            site = "BELA",
            group = "GLAVNI",
            subgroup = "PLIN",
            name = "Mehanski Plinski Števec",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "bela_glavni_ekspanzija",
            site = "BELA",
            group = "GLAVNI",
            subgroup = "EKSPANZIJA",
            name = "Ekspanzija",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "bela_glavni_toplota",
            site = "BELA",
            group = "GLAVNI",
            subgroup = "TOPLOTA",
            name = "Toplotni števec",
            unit = "kWh",
            decimals = 1
        ),
        Meter(
            id = "bela_vodarna_predtlak",
            site = "BELA",
            group = "VODARNA",
            name = "Tlak Pred",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "bela_vodarna_potlak",
            site = "BELA",
            group = "VODARNA",
            name = "Tlak Po",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "bela_vodarna_trenutna",
            site = "BELA",
            group = "VODARNA",
            name = "Trenutni izmenjevalec",
            unit = "",
            decimals = 0
        ),
        Meter(
            id = "bela_vodarna_regureod",
            site = "BELA",
            group = "VODARNA",
            name = "Ur Od Regeneracije",
            unit = "h",
            decimals = 0
        ),
        Meter(
            id = "bela_vodarna_regkubdo",
            site = "BELA",
            group = "VODARNA",
            name = "Kubikov do Regeneracije",
            unit = "m³",
            decimals = 1
        ),
        Meter(
            id = "bela_spte_plin",
            site = "BELA",
            group = "SPTE",
            name = "Plinski števec",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "bela_spte_toplota",
            site = "BELA",
            group = "SPTE",
            name = "Toplotni Števec",
            unit = "kWh",
            decimals = 1
        ),
        Meter(
            id = "bela_spte_zagoni",
            site = "BELA",
            group = "SPTE",
            name = "Zagoni",
            unit = "",
            decimals = 0
        ),
        Meter(
            id = "bela_spte_ure",
            site = "BELA",
            group = "SPTE",
            name = "Ure Delovanja",
            unit = "h",
            decimals = 0
        ),
        Meter(
            id = "bela_spte_moč",
            site = "BELA",
            group = "SPTE",
            name = "Moč Trenutno",
            unit = "kW",
            decimals = 0
        ),
        Meter(
            id = "bela_spte_tempvoda",
            site = "BELA",
            group = "SPTE",
            name = "Voda Temperatura",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "bela_spte_tempolje",
            site = "BELA",
            group = "SPTE",
            name = "Olje Temperatura",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "bela_spte_tlakolje",
            site = "BELA",
            group = "SPTE",
            name = "Olje Tlak",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "bela_spte_izpuh1",
            site = "BELA",
            group = "SPTE",
            name = "Izpušni Temperatura 1",
            unit = "°C",
            decimals = 0
        ),
        Meter(
            id = "bela_spte_izpuh2",
            site = "BELA",
            group = "SPTE",
            name = "Izpušni Temperatura 2",
            unit = "°C",
            decimals = 0
        ),
        Meter(
            id = "bela_spte_izpuh3",
            site = "BELA",
            group = "SPTE",
            name = "Izpušni Temperatura 3",
            unit = "°C",
            decimals = 0
        ),
        Meter(
            id = "bela_spte_izpuh4",
            site = "BELA",
            group = "SPTE",
            name = "Izpušni Temperatura 4",
            unit = "°C",
            decimals = 0
        ),
    )

    private fun jeseniceMeters() = listOf(
        Meter(
            id = "ST-022",
            site = "JESENICE",
            group = "KOTLI",
            subgroup = "VVK 1",
            name = "Plinski števec",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "ST-024",
            site = "JESENICE",
            group = "KOTLI",
            subgroup = "VVK 2",
            name = "Plinski števec",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "ST-023",
            site = "JESENICE",
            group = "KOTLI",
            subgroup = "VVK 1",
            name = "Ure",
            unit = "h",
            decimals = 0
        ),
        Meter(
            id = "ST-025",
            site = "JESENICE",
            group = "KOTLI",
            subgroup = "VVK 2",
            name = "Ure",
            unit = "h",
            decimals = 0
        ),
        Meter(
            id = "ST-031",
            site = "JESENICE",
            group = "KOTLI",
            subgroup = "BIOMASA",
            name = "Toplotni števec",
            unit = "MWh",
            decimals = 1
        ),
        Meter(
            id = "ST-026",
            site = "JESENICE",
            group = "GLAVNI",
            subgroup = "PLIN",
            name = "Plin - Glavni mehanski",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "ST-027",
            site = "JESENICE",
            group = "GLAVNI",
            subgroup = "PLIN",
            name = "Plin - Glavni korigirani",
            unit = "Nm³",
            decimals = 0
        ),
        Meter(
            id = "ST-028",
            site = "JESENICE",
            group = "GLAVNI",
            subgroup = "TOPLOTA",
            name = "Toplota - Glavni",
            unit = "MWh",
            decimals = 1
        ),
        Meter(
            id = "ST-030",
            site = "JESENICE",
            group = "GLAVNI",
            subgroup = "PANTERM",
            name = "Toplota - Panterm",
            unit = "MWh",
            decimals = 1
        ),
        Meter(
            id = "ST-001",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 1",
            name = "Plin - SPTE 1 korigirani",
            unit = "Nm³",
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte1",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 1",
            name = "Plin - SPTE 1 mehanski",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "ST-002",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 1",
            name = "Toplota - SPTE 1",
            unit = "MWh",
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s1",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 1",
            name = "T3-SPTE 1-Temp. mešanice",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s1",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 1",
            name = "T6-SPTE 1-Temp. vode",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s1",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 1",
            name = "T4-SPTE 1-Tlak olja",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s1",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 1",
            name = "P6-SPTE 1-Temp. olja",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "ST-003",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 1",
            name = "Elektrika - SPTE 1",
            unit = "kWh",
            decimals = 0
        ),
        Meter(
            id = "ST-004",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 2",
            name = "Plin - SPTE 2 korigirani",
            unit = "Nm³",
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte2",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 2",
            name = "Plin - SPTE 2 mehanski",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "ST-005",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 2",
            name = "Toplota - SPTE 2",
            unit = "MWh",
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s2",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 2",
            name = "T3-SPTE 2-Temp. mešanice",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s2",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 2",
            name = "T6-SPTE 2-Temp. vode",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s2",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 2",
            name = "T4-SPTE 2-Tlak olja",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s2",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 2",
            name = "P6-SPTE 2-Temp. olja",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "ST-006",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 2",
            name = "Elektrika - SPTE 2",
            unit = "kWh",
            decimals = 0
        ),
        Meter(
            id = "ST-007",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 3",
            name = "Plin - SPTE 3 korigirani",
            unit = "Nm³",
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte3",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 3",
            name = "Plin - SPTE 3 mehanski",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "ST-008",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 3",
            name = "Toplota - SPTE 3",
            unit = "MWh",
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s3",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 3",
            name = "T3-SPTE 3-Temp. mešanice",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s3",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 3",
            name = "T6-SPTE 3-Temp. vode",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s3",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 3",
            name = "T4-SPTE 3-Tlak olja",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s3",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 3",
            name = "P6-SPTE 3-Temp. olja",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "ST-009",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 3",
            name = "Elektrika - SPTE 3",
            unit = "kWh",
            decimals = 0
        ),
        Meter(
            id = "ST-010",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 4",
            name = "Plin - SPTE 4 korigirani",
            unit = "Nm³",
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte4",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 4",
            name = "Plin - SPTE 4 mehanski",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "ST-011",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 4",
            name = "Toplota - SPTE 4",
            unit = "MWh",
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s4",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 4",
            name = "T3-SPTE 4-Temp. mešanice",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s4",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 4",
            name = "T6-SPTE 4-Temp. vode",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s4",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 4",
            name = "T4-SPTE 4-Tlak olja",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s4",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 4",
            name = "P6-SPTE 4-Temp. olja",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "ST-012",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 4",
            name = "Elektrika - SPTE 4",
            unit = "kWh",
            decimals = 0
        ),
        Meter(
            id = "ST-013",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 5",
            name = "Plin - SPTE 5 korigirani",
            unit = "Nm³",
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte5",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 5",
            name = "Plin - SPTE 5 mehanski",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "ST-014",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 5",
            name = "Toplota - SPTE 5",
            unit = "MWh",
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s5",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 5",
            name = "T3-SPTE 5-Temp. mešanice",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s5",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 5",
            name = "T6-SPTE 5-Temp. vode",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s5",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 5",
            name = "T4-SPTE 5-Tlak olja",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s5",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 5",
            name = "P6-SPTE 5-Temp. olja",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "ST-015",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 5",
            name = "Elektrika - SPTE 5",
            unit = "kWh",
            decimals = 0
        ),
        Meter(
            id = "ST-016",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 6",
            name = "Plin - SPTE 6 korigirani",
            unit = "Nm³",
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte6",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 6",
            name = "Plin - SPTE 6 mehanski",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "ST-017",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 6",
            name = "Toplota - SPTE 6",
            unit = "MWh",
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s6",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 6",
            name = "T3-SPTE 6-Temp. mešanice",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s6",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 6",
            name = "T6-SPTE 6-Temp. vode",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s6",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 6",
            name = "T4-SPTE 6-Tlak olja",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s6",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 6",
            name = "P6-SPTE 6-Temp. olja",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "ST-018",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 6",
            name = "Elektrika - SPTE 6",
            unit = "kWh",
            decimals = 0
        ),
        Meter(
            id = "ST-019",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 7",
            name = "Plin - SPTE 7 korigirani",
            unit = "Nm³",
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte7",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 7",
            name = "Plin - SPTE 7 mehanski",
            unit = "m³",
            decimals = 0
        ),
        Meter(
            id = "ST-020",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 7",
            name = "Toplota - SPTE 7",
            unit = "MWh",
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s7",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 7",
            name = "T3-SPTE 7-Temp. mešanice",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s7",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 7",
            name = "T6-SPTE 7-Temp. vode",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s7",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 7",
            name = "T4-SPTE 7-Tlak olja",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s7",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 7",
            name = "P6-SPTE 7-Temp. olja",
            unit = "°C",
            decimals = 1
        ),
        Meter(
            id = "ST-021",
            site = "JESENICE",
            group = "SPTE",
            subgroup = "SPTE 7",
            name = "Elektrika - SPTE 7",
            unit = "kWh",
            decimals = 0
        ),
        Meter(
            id = "tlak_pred_vodarna",
            site = "JESENICE",
            group = "VODARNA",
            name = "Tlak pred filtrom",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "tlak_po_vodarna",
            site = "JESENICE",
            group = "VODARNA",
            name = "Tlak po filtru",
            unit = "bar",
            decimals = 1
        ),
        Meter(
            id = "aktivni_izm_vodarna",
            site = "JESENICE",
            group = "VODARNA",
            name = "Aktivni izmenjevalec",
            unit = "",
            decimals = 0
        ),
        Meter(
            id = "pretok_vodarna",
            site = "JESENICE",
            group = "VODARNA",
            name = "Pretok",
            unit = "m³/h",
            decimals = 1
        ),
        Meter(
            id = "ur_od_regen",
            site = "JESENICE",
            group = "VODARNA",
            name = "Ur od regeneracije",
            unit = "h",
            decimals = 0
        ),
        Meter(
            id = "m3_do_regen",
            site = "JESENICE",
            group = "VODARNA",
            name = "Kubikov do regeneracije",
            unit = "m³",
            decimals = 1
        ),
        Meter(
            id = "ST-029",
            site = "JESENICE",
            group = "VODARNA",
            name = "Števec izgub vode",
            unit = "m³",
            decimals = 0
        ),
    )
}