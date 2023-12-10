package br.com.pacbittencourt.upgradedguacamole

import kotlin.math.sqrt

class Calculator {

    fun sum(first: Double, second: Double) = first + second
    fun sub(first: Double, second: Double) = first - second
    fun times(first: Double, second: Double) = first * second
    fun div(first: Double, second: Double) = first / second
    fun mean(first: Double, second: Double) = sum(first, second)/2
    fun squareRoot(number: Double) = sqrt(number)
}