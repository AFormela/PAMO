package pl.arisa.bmicalculator.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import pl.arisa.bmicalculator.R

class QuizEndFragment : Fragment() {
    private var result = 0
    private var questionsCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        result = requireArguments().getInt(ARG_RESULT)
        questionsCount = requireArguments().getInt(ARG_QUESTION_COUNT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz_end, container, false)
        val tv_result = view.findViewById<TextView>(R.id.quiz_result)
        tv_result.text = getString(
            R.string.text_result,
            result,
            questionsCount,
            result.toFloat() / questionsCount * 100
        )
        return view
    }

    companion object {
        private const val ARG_RESULT = "result"
        private const val ARG_QUESTION_COUNT = "questionsCount"
    }
}