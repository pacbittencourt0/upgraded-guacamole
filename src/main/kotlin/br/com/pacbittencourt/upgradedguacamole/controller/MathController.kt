package br.com.pacbittencourt.upgradedguacamole.controller

import br.com.pacbittencourt.upgradedguacamole.Calculator
import br.com.pacbittencourt.upgradedguacamole.exceptions.UnsupportedMathOperationException
import br.com.pacbittencourt.upgradedguacamole.util.Converter.convertToDouble
import br.com.pacbittencourt.upgradedguacamole.util.Validation.isNumeric
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MathController {

    private val calculator: Calculator = Calculator()

    @RequestMapping(value = ["/sum/{numberOne}/{numberTwo}"])
    fun sum(
        @PathVariable(value = "numberOne") numberOne: String?,
        @PathVariable(value = "numberTwo") numberTwo: String?
    ): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw UnsupportedMathOperationException("Please set a numeric value")
        }
        return calculator.sum(convertToDouble(numberOne), convertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/sub/{numberOne}/{numberTwo}"])
    fun sub(
        @PathVariable(value = "numberOne") numberOne: String?,
        @PathVariable(value = "numberTwo") numberTwo: String?
    ): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw UnsupportedMathOperationException("Please set a numeric value")
        }
        return calculator.sub(convertToDouble(numberOne), convertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/times/{numberOne}/{numberTwo}"])
    fun times(
        @PathVariable(value = "numberOne") numberOne: String?,
        @PathVariable(value = "numberTwo") numberTwo: String?
    ): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw UnsupportedMathOperationException("Please set a numeric value")
        }
        return calculator.times(convertToDouble(numberOne), convertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/div/{numberOne}/{numberTwo}"])
    fun div(
        @PathVariable(value = "numberOne") numberOne: String?,
        @PathVariable(value = "numberTwo") numberTwo: String?
    ): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw UnsupportedMathOperationException("Please set a numeric value")
        }
        if (convertToDouble(numberTwo) == 0.0) {
            throw UnsupportedMathOperationException("Second number cannot be zero")
        }
        return calculator.div(convertToDouble(numberOne), convertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/mean/{numberOne}/{numberTwo}"])
    fun mean(
        @PathVariable(value = "numberOne") numberOne: String?,
        @PathVariable(value = "numberTwo") numberTwo: String?
    ): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw UnsupportedMathOperationException("Please set a numeric value")
        }
        return calculator.mean(convertToDouble(numberOne), convertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/sqrt/{number}"])
    fun squareRoot(
        @PathVariable(value = "number") number: String?
    ): Double {
        if (!isNumeric(number)) {
            throw UnsupportedMathOperationException("Please set a numeric value")
        }
        return calculator.squareRoot(convertToDouble(number))
    }

}