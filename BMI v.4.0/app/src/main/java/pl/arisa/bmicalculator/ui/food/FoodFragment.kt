package pl.arisa.bmicalculator.ui.food

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import pl.arisa.bmicalculator.enum.BmiType
import pl.arisa.bmicalculator.enum.BmiType.*
import pl.arisa.bmicalculator.R
import java.util.*

class FoodFragment : Fragment() {
    private var text_foodRecipeName: TextView? = null
    private var list_foodRecipeList: ListView? = null
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_foodRecipeName = view.findViewById(R.id.food_recipe_name)
        list_foodRecipeList = view.findViewById(R.id.food_recipe_list)
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_name),
            Context.MODE_PRIVATE
        )
        if (sharedPreferences.contains(getString(R.string.key_bmi_category))) {
            val bmiType: BmiType = BmiType.valueOf(
                sharedPreferences.getString(
                    getString(R.string.key_bmi_category),
                    ""
                )!!
            )
            text_foodRecipeName?.text = getFoodRecipeName(bmiType)
            list_foodRecipeList?.adapter = getFoodRecipe(bmiType)
        }
    }

    private fun getFoodRecipeName(bmiType: BmiType): String {
        var recipeNameId = R.string.food_correct_title
        when (bmiType) {
            UNDERWEIGHT -> recipeNameId = R.string.food_underweight_title
            CORRECT -> recipeNameId = R.string.food_correct_title
            OVERWEIGHT -> recipeNameId = R.string.food_overweight_title
            OBESE -> recipeNameId = R.string.food_obese_title
        }
        return getString(recipeNameId)
    }

    private fun getFoodRecipe(bmiType: BmiType): ArrayAdapter<*> {
        val result: String? = null
        var recipeId = R.array.food_correct_recipe
        when (bmiType) {
            UNDERWEIGHT -> recipeId = R.array.food_underweight_recipe
            CORRECT -> recipeId = R.array.food_correct_recipe
            OVERWEIGHT -> recipeId = R.array.food_overweight_recipe
            OBESE -> recipeId = R.array.food_obese_recipe
        }
        val recipeElements = resources.getStringArray(recipeId)
        val recipe = ArrayList(Arrays.asList(*recipeElements))
        return ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            recipe
        )
    }
}