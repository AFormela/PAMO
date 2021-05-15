package pl.arisa.bmicalculator.model

import java.io.Serializable
import java.util.*

class Quiz : Serializable {
    var questions: List<Question> = ArrayList()
    var correctCounter = 0

    companion object {
        private const val serialVersionUID = 2230778689516013709L
    }
}