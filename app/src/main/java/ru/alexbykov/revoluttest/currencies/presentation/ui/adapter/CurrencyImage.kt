package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import ru.alexbykov.revoluttest.R

object CurrencyImage {

    private const val RUB = "RUB"
    private const val AUD = "AUD"
    private const val CAD = "CAD"
    private const val BGN = "BGN"
    private const val BRL = "BRL"
    private const val CHF = "CHF"
    private const val CNY = "CNY"
    private const val CZK = "CZK"
    private const val DKK = "DKK"
    private const val HKD = "HKD"
    private const val HRK = "HRK"
    private const val HUF = "HUF"
    private const val IDR = "IDR"
    private const val ILS = "ILS"
    private const val INR = "INR"
    private const val ISK = "ISK"
    private const val JPY = "JPY"
    private const val KRW = "KRW"
    private const val MXN = "MXN"
    private const val MYR = "MYR"
    private const val NOK = "NOK"
    private const val NZD = "NZD"
    private const val PHP = "PHP"
    private const val PLN = "PLN"
    private const val RON = "RON"
    private const val SEK = "SEK"
    private const val SGD = "SGD"
    private const val THB = "THB"
    private const val TRY = "TRY"
    private const val USD = "USD"
    private const val ZAR = "ZAR"
    private const val EUR = "EUR"
    private const val GBP = "GBP"


    private val images = hashMapOf(
        RUB to R.drawable.ic_rub_flag,
        CAD to R.drawable.ic_cad_flag,
        AUD to R.drawable.ic_aud_flag,
        BGN to R.drawable.ic_bgn_flag,
        BRL to R.drawable.ic_brl_flag,
        CHF to R.drawable.ic_chf_flag,
        CNY to R.drawable.ic_cny_flag,
        CZK to R.drawable.ic_czk_flag,
        DKK to R.drawable.ic_dkk_flag,
        HKD to R.drawable.ic_hkd_flag,
        HRK to R.drawable.ic_hrk_flag,
        HUF to R.drawable.ic_huf_flag,
        IDR to R.drawable.ic_idr_flag,
        ILS to R.drawable.ic_ils_flag,
        INR to R.drawable.ic_inr_flag,
        ISK to R.drawable.ic_isk_flag,
        JPY to R.drawable.ic_jpy_flag,
        KRW to R.drawable.ic_krw_flag,
        MXN to R.drawable.ic_mxn_flag,
        MYR to R.drawable.ic_myr_flag,
        NOK to R.drawable.ic_nok_flag,
        NZD to R.drawable.ic_nzd_flag,
        PHP to R.drawable.ic_php_flag,
        PLN to R.drawable.ic_pln_flag,
        RON to R.drawable.ic_ron_flag,
        SEK to R.drawable.ic_sek_flag,
        SGD to R.drawable.ic_sgd_flag,
        THB to R.drawable.ic_thb_flag,
        TRY to R.drawable.ic_try_flag,
        USD to R.drawable.ic_usd_flag,
        ZAR to R.drawable.ic_zar_flag,
        EUR to R.drawable.ic_eur_flag,
        GBP to R.drawable.ic_gbp_flag
    )

    fun from(currencyCode: String): Int {
        return images[currencyCode] ?: 0
    }


}