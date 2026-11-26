package com.enos.enospte.data.model

enum class Site(val id: Int) {
    JESENICE(1),
    BELA(2)
}

enum class Group(val id: Int) {
    SPTE(1),
    KOTLI(2),
    GLAVNI(3),
    VODARNA(4),
}

enum class SubGroup(val id: Int, val label: String) {
    EMPTY(0, ""),
    BIOMASA(1, "BIOMASA"),
    PANTERM(2, "PANTERM"),
    BOSCH(3, "BOSCH"),
    LOOS(4, "LOOS"),
    SPTE1(5, "SPTE 1"),
    SPTE2(6, "SPTE 2"),
    SPTE3(7, "SPTE 3"),
    SPTE4(8, "SPTE 4"),
    SPTE5(9, "SPTE 5"),
    SPTE6(10, "SPTE 6"),
    SPTE7(11, "SPTE 7"),
    PLIN(12, "PLIN"),
    EKSPANZIJA(13, "EKSPANZIJA"),
    TOPLOTA(14, "TOPLOTA"),
    VVK1(15, "VVK 1"),
    VVK2(16, "VVK 2")
}

enum class MeterName(val id: Int, val label: String) {
    STEVEC_ZAGONOV(1, "Števec Zagonov"),
    STEVEC_UR(2, "Števec Ur"),
    KORIGIRAN_PLINSKI_STEVEC(3, "Korigiran Plinski Števec"),
    MEHANSKI_PLINSKI_STEVEC(4, "Mehanski Plinski Števec"),
    KORIGIRANI_PLINSKI_STEVEC(5, "Korigirani Plinski Števec"),
    EKSPANZIJA(6, "Ekspanzija"),
    TOPLOTNI_STEVEC(7, "Toplotni števec"),
    TLAK_PRED(8, "Tlak Pred"),
    TLAK_PO(9, "Tlak Po"),
    TRENUTNI_IZMENJEVALEC(10, "Trenutni izmenjevalec"),
    UR_OD_REGENERACIJE(11, "Ur Od Regeneracije"),
    KUBIKOV_DO_REGENERACIJE(12, "Kubikov do Regeneracije"),
    PLINSKI_STEVEC(13, "Plinski števec"),
    TOPLOTNI_STEVEC_2(14, "Toplotni Števec"),
    ZAGONI(15, "Zagoni"),
    URE_DELOVANJA(16, "Ure Delovanja"),
    MOC_TRENUTNO(17, "Moč Trenutno"),
    VODA_TEMPERATURA(18, "Voda Temperatura"),
    OLJE_TEMPERATURA(19, "Olje Temperatura"),
    OLJE_TLAK(20, "Olje Tlak"),
    IZPUSNI_TEMPERATURA_1(21, "Izpušni Temperatura 1"),
    IZPUSNI_TEMPERATURA_2(22, "Izpušni Temperatura 2"),
    IZPUSNI_TEMPERATURA_3(23, "Izpušni Temperatura 3"),
    IZPUSNI_TEMPERATURA_4(24, "Izpušni Temperatura 4"),
    URE(25, "Ure"),
    PLIN_GLAVNI_MEHANSKI(26, "Plin - Glavni mehanski"),
    PLIN_GLAVNI_KORIGIRANI(27, "Plin - Glavni korigirani"),
    TOPLOTA_GLAVNI(28, "Toplota - Glavni"),
    TOPLOTA_PANTERM(29, "Toplota - Panterm"),
    PLIN_SPTE_1_KORIGIRANI(30, "Plin - SPTE 1 korigirani"),
    PLIN_SPTE_1_MEHANSKI(31, "Plin - SPTE 1 mehanski"),
    TOPLOTA_SPTE_1(32, "Toplota - SPTE 1"),
    T3_SPTE_1_TEMP_MESANICE(33, "T3-SPTE 1-Temp. mešanice"),
    T6_SPTE_1_TEMP_VODE(34, "T6-SPTE 1-Temp. vode"),
    T4_SPTE_1_TLAK_OLJA(35, "T4-SPTE 1-Tlak olja"),
    P6_SPTE_1_TEMP_OLJA(36, "P6-SPTE 1-Temp. olja"),
    ELEKTRIKA_SPTE_1(37, "Elektrika - SPTE 1"),
    PLIN_SPTE_2_KORIGIRANI(38, "Plin - SPTE 2 korigirani"),
    PLIN_SPTE_2_MEHANSKI(39, "Plin - SPTE 2 mehanski"),
    TOPLOTA_SPTE_2(40, "Toplota - SPTE 2"),
    T3_SPTE_2_TEMP_MESANICE(41, "T3-SPTE 2-Temp. mešanice"),
    T6_SPTE_2_TEMP_VODE(42, "T6-SPTE 2-Temp. vode"),
    T4_SPTE_2_TLAK_OLJA(43, "T4-SPTE 2-Tlak olja"),
    P6_SPTE_2_TEMP_OLJA(44, "P6-SPTE 2-Temp. olja"),
    ELEKTRIKA_SPTE_2(45, "Elektrika - SPTE 2"),
    PLIN_SPTE_3_KORIGIRANI(46, "Plin - SPTE 3 korigirani"),
    PLIN_SPTE_3_MEHANSKI(47, "Plin - SPTE 3 mehanski"),
    TOPLOTA_SPTE_3(48, "Toplota - SPTE 3"),
    T3_SPTE_3_TEMP_MESANICE(49, "T3-SPTE 3-Temp. mešanice"),
    T6_SPTE_3_TEMP_VODE(50, "T6-SPTE 3-Temp. vode"),
    T4_SPTE_3_TLAK_OLJA(51, "T4-SPTE 3-Tlak olja"),
    P6_SPTE_3_TEMP_OLJA(52, "P6-SPTE 3-Temp. olja"),
    ELEKTRIKA_SPTE_3(53, "Elektrika - SPTE 3"),
    PLIN_SPTE_4_KORIGIRANI(54, "Plin - SPTE 4 korigirani"),
    PLIN_SPTE_4_MEHANSKI(55, "Plin - SPTE 4 mehanski"),
    TOPLOTA_SPTE_4(56, "Toplota - SPTE 4"),
    T3_SPTE_4_TEMP_MESANICE(57, "T3-SPTE 4-Temp. mešanice"),
    T6_SPTE_4_TEMP_VODE(58, "T6-SPTE 4-Temp. vode"),
    T4_SPTE_4_TLAK_OLJA(59, "T4-SPTE 4-Tlak olja"),
    P6_SPTE_4_TEMP_OLJA(60, "P6-SPTE 4-Temp. olja"),
    ELEKTRIKA_SPTE_4(61, "Elektrika - SPTE 4"),
    PLIN_SPTE_5_KORIGIRANI(62, "Plin - SPTE 5 korigirani"),
    PLIN_SPTE_5_MEHANSKI(63, "Plin - SPTE 5 mehanski"),
    TOPLOTA_SPTE_5(64, "Toplota - SPTE 5"),
    T3_SPTE_5_TEMP_MESANICE(65, "T3-SPTE 5-Temp. mešanice"),
    T6_SPTE_5_TEMP_VODE(66, "T6-SPTE 5-Temp. vode"),
    T4_SPTE_5_TLAK_OLJA(67, "T4-SPTE 5-Tlak olja"),
    P6_SPTE_5_TEMP_OLJA(68, "P6-SPTE 5-Temp. olja"),
    ELEKTRIKA_SPTE_5(69, "Elektrika - SPTE 5"),
    PLIN_SPTE_6_KORIGIRANI(70, "Plin - SPTE 6 korigirani"),
    PLIN_SPTE_6_MEHANSKI(71, "Plin - SPTE 6 mehanski"),
    TOPLOTA_SPTE_6(72, "Toplota - SPTE 6"),
    T3_SPTE_6_TEMP_MESANICE(73, "T3-SPTE 6-Temp. mešanice"),
    T6_SPTE_6_TEMP_VODE(74, "T6-SPTE 6-Temp. vode"),
    T4_SPTE_6_TLAK_OLJA(75, "T4-SPTE 6-Tlak olja"),
    P6_SPTE_6_TEMP_OLJA(76, "P6-SPTE 6-Temp. olja"),
    ELEKTRIKA_SPTE_6(77, "Elektrika - SPTE 6"),
    PLIN_SPTE_7_KORIGIRANI(78, "Plin - SPTE 7 korigirani"),
    PLIN_SPTE_7_MEHANSKI(79, "Plin - SPTE 7 mehanski"),
    TOPLOTA_SPTE_7(80, "Toplota - SPTE 7"),
    T3_SPTE_7_TEMP_MESANICE(81, "T3-SPTE 7-Temp. mešanice"),
    T6_SPTE_7_TEMP_VODE(82, "T6-SPTE 7-Temp. vode"),
    T4_SPTE_7_TLAK_OLJA(83, "T4-SPTE 7-Tlak olja"),
    P6_SPTE_7_TEMP_OLJA(84, "P6-SPTE 7-Temp. olja"),
    ELEKTRIKA_SPTE_7(85, "Elektrika - SPTE 7"),
    TLAK_PRED_FILTROM(86, "Tlak pred filtrom"),
    TLAK_PO_FILTRU(87, "Tlak po filtru"),
    AKTIVNI_IZMENJEVALEC(88, "Aktivni izmenjevalec"),
    PRETOK(89, "Pretok"),
    UR_OD_REGENERACIJE_2(90, "Ur od regeneracije"),
    KUBIKOV_DO_REGENERACIJE_2(91, "Kubikov do regeneracije"),
    STEVEC_IZGUB_VODE(92, "Števec izgub vode"),
}

enum class MeterUnit(val id: Int, val label: String) {
    EMPTY(0, ""),
    HOUR(1, "h"),
    CUBIC_METER(2, "m³"),
    KILOWATT_HOUR(3, "kWh"),
    BAR(4, "bar"),
    KILOWATT(5, "kW"),
    CELSIUS(6, "°C"),
    MEGAWATT_HOUR(7, "MWh"),
    NORMAL_CUBIC_METER(8, "Nm³"),
    CUBIC_METER_PER_HOUR(9, "m³/h"),
}

data class Meter(
    val id: String,              // ID števca; kasneje naj bo enak Excel / SQL ID-ju
    val site: Site,            // npr. "BELA", "JESENICE" mora biti posifrirano

    val group: Group,        // npr. "KOTLI", "SPTE", "VODARNA"
    val subgroup: SubGroup,           // npr. "KOTEL 1", "SPTE 1", "GLAVNI ŠTEVEC"

    val name: MeterName,            // npr. "Plinski števec", "Števec ur"
    val unit: MeterUnit,            // npr. "m3", "h", "kWh", "bar"
    val decimals: Int = 0,
    val active: Boolean = true
)