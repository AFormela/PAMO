package pl.arisa.bmicalculator.ui.quiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pl.arisa.bmicalculator.MainActivity;
import pl.arisa.bmicalculator.R;

public class QuizEndFragment extends Fragment {

    private static final String ARG_RESULT = "result";
    private static final String ARG_QUESTION_COUNT = "questionsCount";
    private int result;
    private int questionsCount;

    public QuizEndFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            result = getArguments().getInt(ARG_RESULT);
            questionsCount = getArguments().getInt(ARG_QUESTION_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz_end, container, false);
        TextView tv_result = view.findViewById(R.id.quiz_result);
        tv_result.setText(getString(R.string.text_result, result, questionsCount, (float) result / questionsCount * 100));

        return view;
    }

}