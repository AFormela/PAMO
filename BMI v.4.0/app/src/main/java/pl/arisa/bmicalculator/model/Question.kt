package pl.arisa.bmicalculator.model

import java.io.Serializable
import java.util.*

class Question : Serializable {
    var name: String? = null

    //@JsonProperty("answers")
    //@JsonProperty("answers")
    var answersList: List<Answer> = ArrayList()

    companion object {
        private const val serialVersionUID = -1310138642534128712L
    }
}