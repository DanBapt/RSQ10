package com.example.rsq.Pump;

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

public class PumpEvaluationFragment extends Fragment {
    private QuizViewModel quizViewModel;
    private LinearLayout questionnaireLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pump_evaluation, container, false);
        questionnaireLayout = root.findViewById(R.id.questionnaire_layout);
        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        List<String> participantNames = ((PumpActivity) getActivity()).getParticipantNames();

        // Generate questionnaires for each participant
        for (String participantName : participantNames) {
            View participantQuestionnaire = inflater.inflate(R.layout.pump_participant_questionnaire, questionnaireLayout, false);

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


                    // Collect the responses
                    String participantName = participantNames.get(i);  // You should make sure this matches the participant for the current questionnaire
                    List<String> answers = new ArrayList<>();
                    answers.add(getAnswerFromRadioGroup(radioGroup1));
                    answers.add(getAnswerFromRadioGroup(radioGroup2));
                    answers.add(getAnswerFromRadioGroup(radioGroup3));
                    answers.add(getAnswerFromRadioGroup(radioGroup4));



                    participantAnswers.put(participantName, answers);
                }
                quizViewModel.setParticipantAnswers(participantAnswers);
            }
        });

        return root;
    }

    private String getAnswerFromRadioGroup(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId != -1) {
            RadioButton radioButton = radioGroup.findViewById(selectedId);
            return radioButton.getText().toString();
        } else {
            return "Non évalué";
        }
    }
}
