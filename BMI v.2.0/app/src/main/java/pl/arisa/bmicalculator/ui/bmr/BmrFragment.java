package pl.arisa.bmicalculator.ui.bmr;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

import pl.arisa.bmicalculator.Enum.Gender;
import pl.arisa.bmicalculator.R;
import pl.arisa.bmicalculator.ui.bmi.BmiFragment;

public class BmrFragment extends Fragment {
    private EditText input_height, input_weight, input_age;
    private RadioGroup radio_gender;
    private TextView text_result;
    private SharedPreferences sharedPreferences;
    Button button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bmr, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        input_height = (EditText)view.findViewById(R.id.input_height);
        input_weight = (EditText)view.findViewById(R.id.input_weight);
        input_age = (EditText)view.findViewById(R.id.input_age);
        text_result = (TextView)view.findViewById(R.id.text_result);
        button = (Button) view.findViewById(R.id.button_count);
        radio_gender = (RadioGroup) view.findViewById(R.id.radio_gender);
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_name), Context.MODE_PRIVATE);

        if(sharedPreferences.contains(getString(R.string.key_bmi_category))) {
            input_height.setText(String.valueOf(sharedPreferences.getFloat(getString(R.string.key_height), 0)));
            input_weight.setText(String.valueOf(sharedPreferences.getFloat(getString(R.string.key_weight), 0)));
        }

        button.setOnClickListener(new CountBmr());
    }

    public class CountBmr implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String heightString = input_height.getText().toString();
            String weightString = input_weight.getText().toString();
            String ageString = input_age.getText().toString();
            if(heightString.isEmpty() || weightString.isEmpty()) {
                text_result.setText(R.string.error_weight_height_required);
                text_result.setTextColor(Color.RED);
                return;
            }
            float height, weight;
            int age;
            try {
                height = Float.parseFloat(heightString) / 100;
                weight = Float.parseFloat(weightString);
                age = Integer.parseInt(ageString);
            } catch (NumberFormatException nfe) {
                text_result.setText(nfe.getMessage());
                text_result.setTextColor(Color.RED);
                return;
            }

            text_result.setTextColor(Color.BLACK);

            double bmr = getBmr(age, getGender(), height, weight);
            text_result.setText(String.format(Locale.US, "Potrzebujesz %.2f dziennie", bmr));
        }

    }

    private Gender getGender() {
        int selectedGender = radio_gender.getCheckedRadioButtonId();
        if(selectedGender == R.id.radio_gender_male) {
            return Gender.MALE;
        } else if(selectedGender == R.id.radio_gender_female) {
            return Gender.FEMALE;
        }

        return null;
    }

    private double getBmr(int age, Gender gender, float height, float weight) {

        if(gender.equals(Gender.FEMALE)) {
            return 655.1 + 9.567*weight + 1.85*height - 4.68*age;
        } else if(gender.equals(Gender.MALE)) {
            return 66.47 + 13.7*weight + 5*height - 6.76*age;
        }

        return 0;
    }
}