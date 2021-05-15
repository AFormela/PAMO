package pl.arisa.bmicalculator.ui.quiz

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.fasterxml.jackson.databind.ObjectMapper
import pl.arisa.bmicalculator.R
import pl.arisa.bmicalculator.model.Quiz
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.Collections.shuffle
import java.util.function.Consumer

class QuizStepFragment : Fragment() {
    private lateinit var button_next: Button
    private lateinit var button_answer1: Button
    private lateinit var button_answer2: Button
    private lateinit var button_answer3: Button
    private lateinit var button_answer4: Button
    private var tv_question_counter: TextView? = null
    private var tv_question_name: TextView? = null
    private var result = 0
    private var questionCount = 0
    private var correctAnswer: String? = null
    private val quizParam: String? = null
    private var quiz: Quiz? = null
    private lateinit var buttons : List<Button>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            quiz = requireArguments().getSerializable(ARG_QUIZ) as Quiz?
        }
        var json = ""
        try {
            json = loadJSONFromAssets("healthyCareQuiz.json")
            val mapper = ObjectMapper()
            if (json.isEmpty()) return
            quiz = mapper.readValue(json, Quiz::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz_step, container, false)
        tv_question_counter = view.findViewById(R.id.quiz_question_counter)
        tv_question_name = view.findViewById(R.id.quiz_question)
        button_answer1 = view.findViewById(R.id.button_answer_1)
        button_answer2 = view.findViewById(R.id.button_answer_2)
        button_answer3 = view.findViewById(R.id.button_answer_3)
        button_answer4 = view.findViewById(R.id.button_answer_4)
        button_next = view.findViewById(R.id.btn_quiz_next)
        buttons = listOf(button_answer1, button_answer2, button_answer3, button_answer4)
        for (button in buttons) {
            button.setOnClickListener(
                AnswerButtonListener()
            )
        }
        button_next.setOnClickListener(NextQuestionListener())
        prepareQuestions()
        return view
    }

    @Throws(IOException::class)
    private fun loadJSONFromAssets(fileName: String): String {
        val json: String
        val `is` = requireActivity().assets.open(fileName)
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        json = String(buffer, StandardCharsets.UTF_8)
        return json
    }

    private fun loadNextQuestion() {
        button_next.isEnabled = false
        val question = quiz!!.questions[questionCount]
        questionCount++
        tv_question_counter!!.text =
            getString(R.string.text_question_number, questionCount, quiz!!.questions.size)
        tv_question_name!!.text = question.name
        for (i in question.answersList.indices) {
            val answer = question.answersList[i]
            val answerButton = buttons[i]
            answerButton.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            answerButton.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.purple_500
                )
            )
            answerButton.isEnabled = true
            answerButton.text = answer.name
            if (answer.isCorrect) {
                correctAnswer = answer.name
            }
        }
    }

    private inner class AnswerButtonListener : View.OnClickListener {
        override fun onClick(view: View) {
            val answerButton = view as Button
            val answerText = answerButton.text.toString()
            if (answerText == correctAnswer) {
                result++
                answerButton.setBackgroundColor(Color.GREEN)
            } else {
                answerButton.setBackgroundColor(Color.RED)
                colorizeCorrectAnswerButton()
            }
            buttons.forEach(Consumer { buttonAnswer: Button ->
                buttonAnswer.isEnabled = false
            })
            button_next.isEnabled = true
        }
    }

    fun prepareQuestions() {
        shuffle(quiz!!.questions)
        result = 0
        questionCount = 0
        loadNextQuestion()
    }

    private fun colorizeCorrectAnswerButton() {
        buttons.stream()
            .filter { b: Button ->
                b.text.toString() == correctAnswer
            }
            .findFirst()
            .ifPresent { correctButton: Button ->
                correctButton.setBackgroundColor(Color.GREEN)
                correctButton.setTextColor(Color.WHITE)
            }
    }

    private fun showQuizResult() {
        val bundle = Bundle()
        bundle.putInt("result", result)
        bundle.putInt("questionsCount", quiz!!.questions.size)
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        navController.navigate(
            R.id.navigation_quiz_end,
            bundle
        )
    }

    private inner class NextQuestionListener : View.OnClickListener {
        override fun onClick(v: View) {
            val button = v as Button
            if (questionCount == quiz!!.questions.size) {
                showQuizResult()
                button.visibility = View.GONE
            }
            if (questionCount < quiz!!.questions.size) {
                loadNextQuestion()
            }
        }
    }

    companion object {
        private const val ARG_QUIZ = "quiz"
        fun newInstance(quiz: Quiz?): QuizStepFragment {
            val fragment = QuizStepFragment()
            val args = Bundle()
            args.putSerializable(ARG_QUIZ, quiz)
            fragment.arguments = args
            return fragment
        }
    }
}