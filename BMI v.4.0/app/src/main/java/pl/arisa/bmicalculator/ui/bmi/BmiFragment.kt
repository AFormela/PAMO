package pl.arisa.bmicalculator.ui.bmi

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import pl.arisa.bmicalculator.enum.BmiType
import pl.arisa.bmicalculator.R
import java.util.*

class BmiFragment : Fragment() {
    private var input_height: EditText? = null
    private var weight_input: EditText? = null
    private var text_result: TextView? = null
    private var sharedPreferences: SharedPreferences? = null
    private var button: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bmi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        input_height = view.findViewById<View>(R.id.input_height) as EditText
        weight_input = view.findViewById<View>(R.id.input_weight) as EditText
        text_result = view.findViewById<View>(R.id.text_result) as TextView
        button = view.findViewById<View>(R.id.button_count) as Button
        sharedPreferences = activity?.getSharedPreferences(
            getString(R.string.preference_name),
            Context.MODE_PRIVATE
        )
        button!!.setOnClickListener(CountBmi())
    }

    inner class CountBmi : View.OnClickListener {
        override fun onClick(v: View) {
            val heightString = input_height!!.text.toString()
            val weightString = weight_input!!.text.toString()
            if (heightString.isEmpty() || weightString.isEmpty()) {
                text_result!!.setText(R.string.error_weight_height_required)
                text_result!!.setTextColor(Color.RED)
                return
            }
            val height: Float
            val weight: Float
            try {
                height = heightString.toFloat() / 100
                weight = weightString.toFloat()
            } catch (nfe: NumberFormatException) {
                text_result!!.text = nfe.message
                text_result!!.setTextColor(Color.RED)
                return
            }
            val editor = sharedPreferences!!.edit()
            editor.putFloat(getString(R.string.key_height), height * 100)
            editor.putFloat(getString(R.string.key_weight), weight)
            val result = weight / (height * height)
            text_result!!.setTextColor(Color.BLACK)
            text_result!!.text =
                String.format(Locale.US, "BMI %.2f - %s", result, getBmiCategory(result))
            editor.putString(
                getString(R.string.key_bmi_category),
                getBmiCategory(result).toString()
            )
            editor.apply()
        }
    }

    private fun getBmiCategory(bmi: Float): BmiType {
        return if (bmi < 18.5) {
            BmiType.UNDERWEIGHT
        } else if (bmi <= 24.9) {
            BmiType.CORRECT
        } else if (bmi <= 29.9) {
            BmiType.OVERWEIGHT
        } else {
            BmiType.OBESE
        }
    }
}