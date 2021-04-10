package pl.arisa.bmicalculator.ui.food;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.res.AssetManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import pl.arisa.bmicalculator.Enum.BmiType;
import pl.arisa.bmicalculator.R;

import static pl.arisa.bmicalculator.Enum.BmiType.UNDERWEIGHT;

public class FoodFragment extends Fragment {
    private TextView text_foodRecipeName;
    private ListView list_foodRecipeList;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_food, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        text_foodRecipeName = view.findViewById(R.id.food_recipe_name);
        list_foodRecipeList = view.findViewById(R.id.food_recipe_list);

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_name), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(getString(R.string.key_bmi_category))) {
            BmiType bmiType = BmiType.valueOf(sharedPreferences.getString(getString(R.string.key_bmi_category), ""));
            text_foodRecipeName.setText(getFoodRecipeName(bmiType));
            list_foodRecipeList.setAdapter(getFoodRecipe(bmiType));
        }
    }

    private String getFoodRecipeName(BmiType bmiType){
        int recipeNameId = R.string.food_correct_title;
        switch(bmiType) {
            case UNDERWEIGHT:
                recipeNameId = R.string.food_underweight_title;
                break;
            case CORRECT:
                recipeNameId = R.string.food_correct_title;
                break;
            case OVERWEIGHT:
                recipeNameId = R.string.food_overweight_title;
                break;
            case OBESE:
                recipeNameId = R.string.food_obese_title;
                break;
        }

        return getString(recipeNameId);
    }

    private ArrayAdapter getFoodRecipe(BmiType bmiType) {
        String result = null;

        int recipeId = R.array.food_correct_recipe;
        switch(bmiType) {
            case UNDERWEIGHT:
                recipeId = R.array.food_underweight_recipe;
                break;
            case CORRECT:
                recipeId = R.array.food_correct_recipe;
                break;
            case OVERWEIGHT:
                recipeId = R.array.food_overweight_recipe;
                break;
            case OBESE:
                recipeId = R.array.food_obese_recipe;
                break;
        }

        String[] recipeElements = getResources().getStringArray(recipeId);

        ArrayList<String> recipe = new ArrayList<>(Arrays.asList(recipeElements));

        return new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, recipe);
    }
}