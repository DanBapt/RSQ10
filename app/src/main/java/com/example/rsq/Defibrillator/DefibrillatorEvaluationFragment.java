package com.example.rsq.Defibrillator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rsq.QuizViewModel;
import com.example.rsq.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefibrillatorEvaluationFragment extends Fragment {
    private QuizViewModel quizViewModel;
    private LinearLayout questionnaireLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_defibrillator_evaluation, container, false);
        questionnaireLayout = root.findViewById(R.id.questionnaire_layout);
        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        List<String> participantNames = ((DefibrillatorActivity) getActivity()).getParticipantNames();

        // Generate questionnaires for each participant
        for (String participantName : participantNames) {
            View participantQuestionnaire = inflater.inflate(R.layout.defibrillator_participant_questionnaire, questionnaireLayout, false);

            TextView participantNameTextView = participantQuestionnaire.findViewById(R.id.participant_name);
            participantNameTextView.setText(participantName);

            questionnaireLayout.addView(participantQuestionnaire, questionnaireLayout.getChildCount() - 1); // Added "- 1" to add questionnaire before button
        }

        Button validateButton = root.findViewById(R.id.button_submit);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This part collects the answers from each participant's questionnaire and stores them in the ViewModel.
                HashMap<String, List<String>> participantAnswers = new HashMap<>();
                for (int i = 0; i < questionnaireLayout.getChildCount() - 1; i++) { // Modified condition to "- 1" to ignore the button
                    View participantQuestionnaire = questionnaireLayout.getChildAt(i);

                    RadioGroup radioGroup1 = participantQuestionnaire.findViewById(R.id.radioGroup1);
                    RadioGroup radioGroup2 = participantQuestionnaire.findViewById(R.id.radioGroup2);
                    RadioGroup radioGroup3 = participantQuestionnaire.findViewById(R.id.radioGroup3);
                    RadioGroup radioGroup4 = participantQuestionnaire.findViewById(R.id.radioGroup4);
                    RadioGroup radioGroup5 = participantQuestionnaire.findViewById(R.id.radioGroup5);

                    int selectedId1 = radioGroup1.getCheckedRadioButtonId();
                    int selectedId2 = radioGroup2.getCheckedRadioButtonId();
                    int selectedId3 = radioGroup3.getCheckedRadioButtonId();
                    int selectedId4 = radioGroup4.getCheckedRadioButtonId();
                    int selectedId5 = radioGroup5.getCheckedRadioButtonId();

                    RadioButton radioButton1 = radioGroup1.findViewById(selectedId1);
                    RadioButton radioButton2 = radioGroup2.findViewById(selectedId2);
                    RadioButton radioButton3 = radioGroup3.findViewById(selectedId3);
                    RadioButton radioButton4 = radioGroup4.findViewById(selectedId4);
                    RadioButton radioButton5 = radioGroup5.findViewById(selectedId5);

                    // Collect the responses
                    String participantName = participantNames.get(i);  // You should make sure this matches the participant for the current questionnaire
                    List<String> answers = new ArrayList<>();
                    if (radioButton1 != null) {
                        answers.add(radioButton1.getText().toString());
                    }
                    if (radioButton2 != null) {
                        answers.add(radioButton2.getText().toString());
                    }
                    if (radioButton3 != null) {
                        answers.add(radioButton3.getText().toString());
                    }
                    if (radioButton4 != null) {
                        answers.add(radioButton4.getText().toString());
                    }
                    if (radioButton5 != null) {
                        answers.add(radioButton5.getText().toString());
                    }
                    participantAnswers.put(participantName, answers);
                }
                quizViewModel.setParticipantAnswers(participantAnswers);
            }
        });

        return root;
    }
}
