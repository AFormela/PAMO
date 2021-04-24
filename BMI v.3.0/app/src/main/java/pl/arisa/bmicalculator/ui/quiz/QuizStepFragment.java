package pl.arisa.bmicalculator.ui.quiz;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.arisa.bmicalculator.R;
import pl.arisa.bmicalculator.model.Answer;
import pl.arisa.bmicalculator.model.Question;
import pl.arisa.bmicalculator.model.Quiz;

public class QuizStepFragment extends Fragment {

    private static final String ARG_QUIZ = "quiz";

    private Button button_next, button_answer1, button_answer2, button_answer3, button_answer4;
    private TextView tv_question_counter, tv_question_name;
    private int result, questionCount;
    private String correctAnswer;
    private String quizParam;
    private Quiz quiz;
    private List<Button> buttons;

    public QuizStepFragment() {
        // Required empty public constructor
    }

    public static QuizStepFragment newInstance(Quiz quiz) {
        QuizStepFragment fragment = new QuizStepFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_QUIZ, quiz);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quiz = (Quiz) getArguments().getSerializable(ARG_QUIZ);
        }

        String json = "";
        try {
            json = loadJSONFromAssets("healthyCareQuiz.json");
            ObjectMapper mapper = new ObjectMapper();
            if(json.isEmpty()) return;

            quiz = mapper.readValue(json, Quiz.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz_step, container, false);

        tv_question_counter = view.findViewById(R.id.quiz_question_counter);
        tv_question_name = view.findViewById(R.id.quiz_question);

        button_answer1 = view.findViewById(R.id.button_answer_1);
        button_answer2 = view.findViewById(R.id.button_answer_2);
        button_answer3 = view.findViewById(R.id.button_answer_3);
        button_answer4 = view.findViewById(R.id.button_answer_4);
        button_next = view.findViewById(R.id.btn_quiz_next);

        buttons = Arrays.asList(button_answer1, button_answer2, button_answer3, button_answer4);

        buttons.forEach(button -> {
            button.setOnClickListener(new AnswerButtonListener());
        });


        button_next.setOnClickListener(new NextQuestionListener());
        prepareQuestions();



        return view;
    }

    private String loadJSONFromAssets(String fileName) throws IOException {
        String json;

        InputStream is = getActivity().getAssets().open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, StandardCharsets.UTF_8);

        return json;
    }

    private void loadNextQuestion() {
        button_next.setEnabled(false);

        Question question = quiz.getQuestions().get(questionCount);
        questionCount++;


        tv_question_counter.setText(getString(R.string.text_question_number, questionCount, quiz.getQuestions().size()));
        tv_question_name.setText(question.getName());

        for (int i = 0; i < question.getAnswersList().size(); i++) {
            Answer answer = question.getAnswersList().get(i);
            Button answerButton = buttons.get(i);
            answerButton.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
            answerButton.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.purple_500));
            answerButton.setEnabled(true);
            answerButton.setText(answer.getName());

            if(answer.isCorrect()) {
                correctAnswer = answer.getName();
            }
        }
    }

    private class AnswerButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Button answerButton = (Button) view;
            String answerText = answerButton.getText().toString();

            if (answerText.equals(correctAnswer)) {
                result++;
                answerButton.setBackgroundColor(Color.GREEN);
            } else {
                answerButton.setBackgroundColor(Color.RED);
                colorizeCorrectAnswerButton();
            }

            buttons.forEach(buttonAnswer -> {
                buttonAnswer.setEnabled(false);
            });

            button_next.setEnabled(true);
        }
    }

    public void prepareQuestions() {
        Collections.shuffle(quiz.getQuestions());
        result = 0;
        questionCount = 0;
        loadNextQuestion();
    }

    private void colorizeCorrectAnswerButton() {
        buttons.stream()
                .filter(b -> b.getText().toString().equals(correctAnswer))
                .findFirst()
                .ifPresent(correctButton -> {
                    correctButton.setBackgroundColor(Color.GREEN);
                    correctButton.setTextColor(Color.WHITE);
                });
    }

    private void showQuizResult() {
        Bundle bundle = new Bundle();
        bundle.putInt("result", result);
        bundle.putInt("questionsCount", quiz.getQuestions().size());

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(
                R.id.navigation_quiz_end,
                bundle
        );
    }


    private class NextQuestionListener implements  View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button button = ((Button) v);

            if (questionCount == quiz.getQuestions().size()) {
                showQuizResult();
                button.setVisibility(View.GONE);
            }

            if (questionCount < quiz.getQuestions().size()) {
                loadNextQuestion();
            }
        }
    }
}