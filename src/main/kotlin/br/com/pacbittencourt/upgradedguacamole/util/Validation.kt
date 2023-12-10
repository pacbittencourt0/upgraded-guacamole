package br.com.pacbittencourt.upgradedguacamole.util

object Validation {
    fun isNumeric(strNumber: String?): Boolean {
        if (strNumber.isNullOrBlank()) return false
        val number = strNumber.replace(",", ".")
        return number.matches("""[-+]?[0-9]*\.?[0-9]+""".toRegex())
    }
}