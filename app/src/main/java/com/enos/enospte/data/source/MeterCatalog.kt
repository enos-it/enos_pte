package com.enos.enospte.data.source

import com.enos.enospte.data.model.Group
import com.enos.enospte.data.model.Meter
import com.enos.enospte.data.model.MeterName
import com.enos.enospte.data.model.MeterUnit
import com.enos.enospte.data.model.Site
import com.enos.enospte.data.model.SubGroup
import com.enos.enospte.data.repository.ReadingRepository

object MeterCatalog {
    fun getMeters(): List<Meter> = buildList {
        addAll(belaMeters())
        addAll(jeseniceMeters())
    }.map { meter ->
        meter.copy(id = ReadingRepository.generateId(meter.site.name, meter.group.name, meter.subgroup.label, meter.name.label))
    }

    fun getMeter(id: String): Meter? = getMeters().firstOrNull { it.id == id }

    private fun belaMeters() = listOf(
        Meter(
            id = "bela_bosch_zagoni",
            site = Site.BELA,
            group = Group.KOTLI,
            subgroup = SubGroup.BOSCH,
            name = MeterName.STEVEC_ZAGONOV, //
            unit = MeterUnit.EMPTY,
            decimals = 0
        ),
        Meter(
            id = "bela_bosch_ure",
            site = Site.BELA,
            group = Group.KOTLI,
            subgroup = SubGroup.BOSCH,
            name = MeterName.STEVEC_UR, //
            unit = MeterUnit.HOUR,
            decimals = 0
        ),
        Meter(
            id = "bela_bosch_plinkorigirani",
            site = Site.BELA,
            group = Group.KOTLI,
            subgroup = SubGroup.BOSCH,
            name = MeterName.KORIGIRAN_PLINSKI_STEVEC, //
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "bela_bosch_plinmehanski",
            site = Site.BELA,
            group = Group.KOTLI,
            subgroup = SubGroup.BOSCH,
            name = MeterName.MEHANSKI_PLINSKI_STEVEC, //
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "bela_loos_ure", //
            site = Site.BELA,
            group = Group.KOTLI,
            subgroup = SubGroup.LOOS,
            name = MeterName.STEVEC_UR,
            unit = MeterUnit.HOUR,
            decimals = 0
        ),
        Meter(
            id = "bela_loos_zagoni", //
            site = Site.BELA,
            group = Group.KOTLI,
            subgroup = SubGroup.LOOS,
            name = MeterName.STEVEC_ZAGONOV,
            unit = MeterUnit.EMPTY,
            decimals = 0
        ),
        Meter(
            id = "bela_glavni_plinkorigirani",
            site = Site.BELA,
            group = Group.GLAVNI,
            subgroup = SubGroup.PLIN,
            name = MeterName.KORIGIRANI_PLINSKI_STEVEC,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "bela_glavni_plinmehanski",
            site = Site.BELA,
            group = Group.GLAVNI,
            subgroup = SubGroup.PLIN,
            name = MeterName.MEHANSKI_PLINSKI_STEVEC,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "bela_glavni_ekspanzija",
            site = Site.BELA,
            group = Group.GLAVNI,
            subgroup = SubGroup.EKSPANZIJA,
            name = MeterName.EKSPANZIJA,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "bela_glavni_toplota",
            site = Site.BELA,
            group = Group.GLAVNI,
            subgroup = SubGroup.TOPLOTA,
            name = MeterName.TOPLOTNI_STEVEC,
            unit = MeterUnit.KILOWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "bela_vodarna_predtlak",
            site = Site.BELA,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.TLAK_PRED,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "bela_vodarna_potlak",
            site = Site.BELA,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.TLAK_PO,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "bela_vodarna_trenutna",
            site = Site.BELA,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.TRENUTNI_IZMENJEVALEC,
            unit = MeterUnit.EMPTY,
            decimals = 0
        ),
        Meter(
            id = "bela_vodarna_regureod",
            site = Site.BELA,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.UR_OD_REGENERACIJE,
            unit = MeterUnit.HOUR,
            decimals = 0
        ),
        Meter(
            id = "bela_vodarna_regkubdo",
            site = Site.BELA,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.KUBIKOV_DO_REGENERACIJE,
            unit = MeterUnit.CUBIC_METER,
            decimals = 1
        ),
        Meter(
            id = "bela_spte_plin",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.PLINSKI_STEVEC,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "bela_spte_toplota",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.TOPLOTNI_STEVEC_2,
            unit = MeterUnit.KILOWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "bela_spte_zagoni",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.ZAGONI,
            unit = MeterUnit.EMPTY,
            decimals = 0
        ),
        Meter(
            id = "bela_spte_ure",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.URE_DELOVANJA,
            unit = MeterUnit.HOUR,
            decimals = 0
        ),
        Meter(
            id = "bela_spte_moč",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.MOC_TRENUTNO,
            unit = MeterUnit.KILOWATT,
            decimals = 0
        ),
        Meter(
            id = "bela_spte_tempvoda",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.VODA_TEMPERATURA,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "bela_spte_tempolje",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.OLJE_TEMPERATURA,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "bela_spte_tlakolje",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.OLJE_TLAK,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "bela_spte_izpuh1",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.IZPUSNI_TEMPERATURA_1,
            unit = MeterUnit.CELSIUS,
            decimals = 0
        ),
        Meter(
            id = "bela_spte_izpuh2",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.IZPUSNI_TEMPERATURA_2,
            unit = MeterUnit.CELSIUS,
            decimals = 0
        ),
        Meter(
            id = "bela_spte_izpuh3",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.IZPUSNI_TEMPERATURA_3,
            unit = MeterUnit.CELSIUS,
            decimals = 0
        ),
        Meter(
            id = "bela_spte_izpuh4",
            site = Site.BELA,
            group = Group.SPTE,
            subgroup = SubGroup.EMPTY,
            name = MeterName.IZPUSNI_TEMPERATURA_4,
            unit = MeterUnit.CELSIUS,
            decimals = 0
        ),
    )

    private fun jeseniceMeters() = listOf(
        Meter(
            id = "ST-022",
            site = Site.JESENICE,
            group = Group.KOTLI,
            subgroup = SubGroup.VVK1,
            name = MeterName.PLINSKI_STEVEC,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-024",
            site = Site.JESENICE,
            group = Group.KOTLI,
            subgroup = SubGroup.VVK2,
            name = MeterName.PLINSKI_STEVEC,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-023",
            site = Site.JESENICE,
            group = Group.KOTLI,
            subgroup = SubGroup.VVK1,
            name = MeterName.URE,
            unit = MeterUnit.HOUR,
            decimals = 0
        ),
        Meter(
            id = "ST-025",
            site = Site.JESENICE,
            group = Group.KOTLI,
            subgroup = SubGroup.VVK2,
            name = MeterName.URE,
            unit = MeterUnit.HOUR,
            decimals = 0
        ),
        Meter(
            id = "ST-031",
            site = Site.JESENICE,
            group = Group.KOTLI,
            subgroup = SubGroup.BIOMASA,
            name = MeterName.TOPLOTNI_STEVEC,
            unit = MeterUnit.MEGAWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "ST-026",
            site = Site.JESENICE,
            group = Group.GLAVNI,
            subgroup = SubGroup.PLIN,
            name = MeterName.PLIN_GLAVNI_MEHANSKI,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-027",
            site = Site.JESENICE,
            group = Group.GLAVNI,
            subgroup = SubGroup.PLIN,
            name = MeterName.PLIN_GLAVNI_KORIGIRANI,
            unit = MeterUnit.NORMAL_CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-028",
            site = Site.JESENICE,
            group = Group.GLAVNI,
            subgroup = SubGroup.TOPLOTA,
            name = MeterName.TOPLOTA_GLAVNI,
            unit = MeterUnit.MEGAWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "ST-030",
            site = Site.JESENICE,
            group = Group.GLAVNI,
            subgroup = SubGroup.PANTERM,
            name = MeterName.TOPLOTA_PANTERM,
            unit = MeterUnit.MEGAWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "ST-001",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE1,
            name = MeterName.PLIN_SPTE_1_KORIGIRANI,
            unit = MeterUnit.NORMAL_CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte1",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE1,
            name = MeterName.PLIN_SPTE_1_MEHANSKI,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-002",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE1,
            name = MeterName.TOPLOTA_SPTE_1,
            unit = MeterUnit.MEGAWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s1",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE1,
            name = MeterName.T3_SPTE_1_TEMP_MESANICE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s1",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE1,
            name = MeterName.T6_SPTE_1_TEMP_VODE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s1",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE1,
            name = MeterName.T4_SPTE_1_TLAK_OLJA,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s1",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE1,
            name = MeterName.P6_SPTE_1_TEMP_OLJA,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "ST-003",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE1,
            name = MeterName.ELEKTRIKA_SPTE_1,
            unit = MeterUnit.KILOWATT_HOUR,
            decimals = 0
        ),
        Meter(
            id = "ST-004",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE2,
            name = MeterName.PLIN_SPTE_2_KORIGIRANI,
            unit = MeterUnit.NORMAL_CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte2",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE2,
            name = MeterName.PLIN_SPTE_2_MEHANSKI,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-005",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE2,
            name = MeterName.TOPLOTA_SPTE_2,
            unit = MeterUnit.MEGAWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s2",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE2,
            name = MeterName.T3_SPTE_2_TEMP_MESANICE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s2",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE2,
            name = MeterName.T6_SPTE_2_TEMP_VODE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s2",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE2,
            name = MeterName.T4_SPTE_2_TLAK_OLJA,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s2",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE2,
            name = MeterName.P6_SPTE_2_TEMP_OLJA,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "ST-006",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE2,
            name = MeterName.ELEKTRIKA_SPTE_2,
            unit = MeterUnit.KILOWATT_HOUR,
            decimals = 0
        ),
        Meter(
            id = "ST-007",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE3,
            name = MeterName.PLIN_SPTE_3_KORIGIRANI,
            unit = MeterUnit.NORMAL_CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte3",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE3,
            name = MeterName.PLIN_SPTE_3_MEHANSKI,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-008",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE3,
            name = MeterName.TOPLOTA_SPTE_3,
            unit = MeterUnit.MEGAWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s3",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE3,
            name = MeterName.T3_SPTE_3_TEMP_MESANICE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s3",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE3,
            name = MeterName.T6_SPTE_3_TEMP_VODE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s3",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE3,
            name = MeterName.T4_SPTE_3_TLAK_OLJA,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s3",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE3,
            name = MeterName.P6_SPTE_3_TEMP_OLJA,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "ST-009",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE3,
            name = MeterName.ELEKTRIKA_SPTE_3,
            unit = MeterUnit.KILOWATT_HOUR,
            decimals = 0
        ),
        Meter(
            id = "ST-010",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE4,
            name = MeterName.PLIN_SPTE_4_KORIGIRANI,
            unit = MeterUnit.NORMAL_CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte4",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE4,
            name = MeterName.PLIN_SPTE_4_MEHANSKI,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-011",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE4,
            name = MeterName.TOPLOTA_SPTE_4,
            unit = MeterUnit.MEGAWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s4",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE4,
            name = MeterName.T3_SPTE_4_TEMP_MESANICE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s4",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE4,
            name = MeterName.T6_SPTE_4_TEMP_VODE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s4",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE4,
            name = MeterName.T4_SPTE_4_TLAK_OLJA,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s4",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE4,
            name = MeterName.P6_SPTE_4_TEMP_OLJA,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "ST-012",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE4,
            name = MeterName.ELEKTRIKA_SPTE_4,
            unit = MeterUnit.KILOWATT_HOUR,
            decimals = 0
        ),
        Meter(
            id = "ST-013",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE5,
            name = MeterName.PLIN_SPTE_5_KORIGIRANI,
            unit = MeterUnit.NORMAL_CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte5",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE5,
            name = MeterName.PLIN_SPTE_5_MEHANSKI,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-014",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE5,
            name = MeterName.TOPLOTA_SPTE_5,
            unit = MeterUnit.MEGAWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s5",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE5,
            name = MeterName.T3_SPTE_5_TEMP_MESANICE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s5",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE5,
            name = MeterName.T6_SPTE_5_TEMP_VODE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s5",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE5,
            name = MeterName.T4_SPTE_5_TLAK_OLJA,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s5",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE5,
            name = MeterName.P6_SPTE_5_TEMP_OLJA,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "ST-015",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE5,
            name = MeterName.ELEKTRIKA_SPTE_5,
            unit = MeterUnit.KILOWATT_HOUR,
            decimals = 0
        ),
        Meter(
            id = "ST-016",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE6,
            name = MeterName.PLIN_SPTE_6_KORIGIRANI,
            unit = MeterUnit.NORMAL_CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte6",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE6,
            name = MeterName.PLIN_SPTE_6_MEHANSKI,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-017",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE6,
            name = MeterName.TOPLOTA_SPTE_6,
            unit = MeterUnit.MEGAWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s6",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE6,
            name = MeterName.T3_SPTE_6_TEMP_MESANICE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s6",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE6,
            name = MeterName.T6_SPTE_6_TEMP_VODE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s6",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE6,
            name = MeterName.T4_SPTE_6_TLAK_OLJA,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s6",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE6,
            name = MeterName.P6_SPTE_6_TEMP_OLJA,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "ST-018",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE6,
            name = MeterName.ELEKTRIKA_SPTE_6,
            unit = MeterUnit.KILOWATT_HOUR,
            decimals = 0
        ),
        Meter(
            id = "ST-019",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE7,
            name = MeterName.PLIN_SPTE_7_KORIGIRANI,
            unit = MeterUnit.NORMAL_CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "plin_meh_spte7",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE7,
            name = MeterName.PLIN_SPTE_7_MEHANSKI,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
        Meter(
            id = "ST-020",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE7,
            name = MeterName.TOPLOTA_SPTE_7,
            unit = MeterUnit.MEGAWATT_HOUR,
            decimals = 1
        ),
        Meter(
            id = "temp_mesanica_s7",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE7,
            name = MeterName.T3_SPTE_7_TEMP_MESANICE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "temp_voda_s7",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE7,
            name = MeterName.T6_SPTE_7_TEMP_VODE,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "tlak_olje_s7",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE7,
            name = MeterName.T4_SPTE_7_TLAK_OLJA,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "temp_olje_s7",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE7,
            name = MeterName.P6_SPTE_7_TEMP_OLJA,
            unit = MeterUnit.CELSIUS,
            decimals = 1
        ),
        Meter(
            id = "ST-021",
            site = Site.JESENICE,
            group = Group.SPTE,
            subgroup = SubGroup.SPTE7,
            name = MeterName.ELEKTRIKA_SPTE_7,
            unit = MeterUnit.KILOWATT_HOUR,
            decimals = 0
        ),
        Meter(
            id = "tlak_pred_vodarna",
            site = Site.JESENICE,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.TLAK_PRED_FILTROM,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "tlak_po_vodarna",
            site = Site.JESENICE,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.TLAK_PO_FILTRU,
            unit = MeterUnit.BAR,
            decimals = 1
        ),
        Meter(
            id = "aktivni_izm_vodarna",
            site = Site.JESENICE,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.AKTIVNI_IZMENJEVALEC,
            unit = MeterUnit.EMPTY,
            decimals = 0
        ),
        Meter(
            id = "pretok_vodarna",
            site = Site.JESENICE,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.PRETOK,
            unit = MeterUnit.CUBIC_METER_PER_HOUR,
            decimals = 1
        ),
        Meter(
            id = "ur_od_regen",
            site = Site.JESENICE,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.UR_OD_REGENERACIJE_2,
            unit = MeterUnit.HOUR,
            decimals = 0
        ),
        Meter(
            id = "m3_do_regen",
            site = Site.JESENICE,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.KUBIKOV_DO_REGENERACIJE_2,
            unit = MeterUnit.CUBIC_METER,
            decimals = 1
        ),
        Meter(
            id = "ST-029",
            site = Site.JESENICE,
            group = Group.VODARNA,
            subgroup = SubGroup.EMPTY,
            name = MeterName.STEVEC_IZGUB_VODE,
            unit = MeterUnit.CUBIC_METER,
            decimals = 0
        ),
    )
}
