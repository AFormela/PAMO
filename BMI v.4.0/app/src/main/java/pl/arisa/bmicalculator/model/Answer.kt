package pl.arisa.bmicalculator.model

import java.io.Serializable

class Answer : Serializable {
    var name: String? = null
    var isCorrect = false

    companion object {
        private const val serialVersionUID = 6835839561963301134L
    }
}