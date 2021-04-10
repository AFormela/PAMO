package pl.arisa.bmicalculator.ui.bmi;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import pl.arisa.bmicalculator.Enum.BmiType;
import pl.arisa.bmicalculator.R;

public class BmiFragment extends Fragment {
    private EditText input_height, weight_input;
    private TextView text_result;
    private SharedPreferences sharedPreferences;
    Button button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bmi, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input_height = (EditText)view.findViewById(R.id.input_height);
        weight_input = (EditText)view.findViewById(R.id.input_weight);
        text_result = (TextView)view.findViewById(R.id.text_result);
        button = (Button) view.findViewById(R.id.button_count);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_name), Context.MODE_PRIVATE);
        button.setOnClickListener(new CountBmi());
    }

    public class CountBmi implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String heightString = input_height.getText().toString();
            String weightString = weight_input.getText().toString();
            if(heightString.isEmpty() || weightString.isEmpty()) {
                text_result.setText(R.string.error_weight_height_required);
                text_result.setTextColor(Color.RED);
                return;
            }
            float height;
            float weight;
            try {
                height = Float.parseFloat(heightString) / 100;
                weight = Float.parseFloat(weightString);
            } catch (NumberFormatException nfe) {
                text_result.setText(nfe.getMessage());
                text_result.setTextColor(Color.RED);
                return;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(getString(R.string.key_height), height * 100);
            editor.putFloat(getString(R.string.key_weight), weight);

            Float result = weight / (height*height);
            text_result.setTextColor(Color.BLACK);
            text_result.setText(String.format(Locale.US, "BMI %.2f - %s", result, getBmiCategory(result)));

            editor.putString(getString(R.string.key_bmi_category), getBmiCategory(result).toString());
            editor.apply();
        }

    }

    private BmiType getBmiCategory(Float bmi) {
        if(bmi < 18.5) {
            return BmiType.UNDERWEIGHT;
        } else if(bmi <= 24.9) {
            return BmiType.CORRECT;
        } else if(bmi <= 29.9) {
            return BmiType.OVERWEIGHT;
        } else {
            return BmiType.OBESE;
        }
    }
}