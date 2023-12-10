package br.com.pacbittencourt.upgradedguacamole.util

object Converter {
    fun convertToDouble(strNumber: String?): Double {
        if (strNumber.isNullOrBlank()) return 0.0
        val number = strNumber.replace(",", ".")
        return if (Validation.isNumeric(number)) number.toDouble() else 0.0
    }
}