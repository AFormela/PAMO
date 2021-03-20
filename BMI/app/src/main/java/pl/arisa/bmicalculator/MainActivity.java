package pl.arisa.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button_count);
        button.setOnClickListener(new CountBmi());
    }

    public class CountBmi implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            EditText heightInput = (EditText)findViewById(R.id.input_height);
            EditText weightInput = (EditText)findViewById(R.id.input_weight);
            TextView resultText = (TextView)findViewById(R.id.text_result);
            String heightString = heightInput.getText().toString();
            String weightString = weightInput.getText().toString();
            if(heightString.isEmpty() || weightString.isEmpty()) {
                resultText.setText(R.string.error_weight_height_required);
                resultText.setTextColor(Color.RED);
                return;
            }
            Float height;
            Float weight;
            try {
                height = Float.parseFloat(heightString) / 100;
                weight = Float.parseFloat(weightString);
            } catch (NumberFormatException nfe) {
                resultText.setText(nfe.getMessage());
                resultText.setTextColor(Color.RED);
                return;
            }

            Float result = weight / (height*height);
            resultText.setTextColor(Color.BLACK);
            resultText.setText(String.format(Locale.US, "BMI %.2f - %s", result, getBmiCategory(result)));
        }

    }

    private String getBmiCategory(Float bmi) {
        if(bmi < 18.5) {
            return "Niedowaga";
        } else if(bmi <= 24.9) {
            return "Normalna waga";
        } else if(bmi <= 29.9) {
            return "Nadwaga";
        } else {
            return "Otyłość";
        }
    }
}