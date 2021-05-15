package pl.arisa.bmicalculator.ui.bmr

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import pl.arisa.bmicalculator.enum.Gender
import pl.arisa.bmicalculator.R
import java.util.*

class BmrFragment : Fragment() {
    private var input_height: EditText? = null
    private var input_weight: EditText? = null
    private var input_age: EditText? = null
    private var radio_gender: RadioGroup? = null
    private var text_result: TextView? = null
    lateinit var sharedPreferences: SharedPreferences
    private var button: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bmr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        input_height = view.findViewById<View>(R.id.input_height) as EditText
        input_weight = view.findViewById<View>(R.id.input_weight) as EditText
        input_age = view.findViewById<View>(R.id.input_age) as EditText
        text_result = view.findViewById<View>(R.id.text_result) as TextView
        button = view.findViewById<View>(R.id.button_count) as Button
        radio_gender = view.findViewById<View>(R.id.radio_gender) as RadioGroup
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_name),
            Context.MODE_PRIVATE
        )
        if (sharedPreferences.contains(getString(R.string.key_bmi_category))) {
            input_height!!.setText(
                sharedPreferences.getFloat(getString(R.string.key_height), 0f).toString()
            )
            input_weight!!.setText(
                sharedPreferences.getFloat(getString(R.string.key_weight), 0f).toString()
            )
        }
        button!!.setOnClickListener(CountBmr())
    }

    inner class CountBmr : View.OnClickListener {
        override fun onClick(v: View) {
            val heightString = input_height!!.text.toString()
            val weightString = input_weight!!.text.toString()
            val ageString = input_age!!.text.toString()
            if (heightString.isEmpty() || weightString.isEmpty()) {
                text_result!!.setText(R.string.error_weight_height_required)
                text_result!!.setTextColor(Color.RED)
                return
            }
            val height: Float
            val weight: Float
            val age: Int
            try {
                height = heightString.toFloat() / 100
                weight = weightString.toFloat()
                age = ageString.toInt()
            } catch (nfe: NumberFormatException) {
                text_result!!.text = nfe.message
                text_result!!.setTextColor(Color.RED)
                return
            }
            text_result!!.setTextColor(Color.BLACK)
            val bmr = getBmr(age, gender, height, weight)
            text_result!!.text =
                String.format(Locale.US, "Potrzebujesz %.2f dziennie", bmr)
        }
    }

    private val gender: Gender
        get() {
            val selectedGender = radio_gender!!.checkedRadioButtonId

            if (selectedGender == R.id.radio_gender_female) {
                return Gender.FEMALE
            }

            return Gender.MALE
        }

    private fun getBmr(age: Int, gender: Gender, height: Float, weight: Float): Double {
        if (gender == Gender.FEMALE) {
            return 655.1 + 9.567 * weight + 1.85 * height - 4.68 * age
        } else if (gender == Gender.MALE) {
            return 66.47 + 13.7 * weight + 5 * height - 6.76 * age
        }
        return 0.0
    }
}