package com.example.rsq.Defibrillator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rsq.QuizViewModel;
import com.example.rsq.R;


public class DefibrillatorEvaluationFragment extends Fragment {
    private QuizViewModel quizViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_defibrillator_evaluation, container, false);

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        RadioGroup radioGroup1 = root.findViewById(R.id.radioGroup1);
        RadioGroup radioGroup2 = root.findViewById(R.id.radioGroup2);
        RadioGroup radioGroup3 = root.findViewById(R.id.radioGroup3);
        RadioGroup radioGroup4 = root.findViewById(R.id.radioGroup4);
        RadioGroup radioGroup5 = root.findViewById(R.id.radioGroup5);

        Button validateButton = root.findViewById(R.id.button_submit);

        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId1 = radioGroup1.getCheckedRadioButtonId();
                int selectedId2 = radioGroup2.getCheckedRadioButtonId();
                int selectedId3 = radioGroup3.getCheckedRadioButtonId();
                int selectedId4 = radioGroup4.getCheckedRadioButtonId();
                int selectedId5 = radioGroup5.getCheckedRadioButtonId();

                RadioButton radioButton1 = root.findViewById(selectedId1);
                RadioButton radioButton2 = root.findViewById(selectedId2);
                RadioButton radioButton3 = root.findViewById(selectedId3);
                RadioButton radioButton4 = root.findViewById(selectedId4);
                RadioButton radioButton5 = root.findViewById(selectedId5);

                if (radioButton1 != null) {
                    quizViewModel.setAnswer1(radioButton1.getText().toString());
                }

                if (radioButton2 != null) {
                    quizViewModel.setAnswer2(radioButton2.getText().toString());
                }

                if (radioButton3 != null) {
                    quizViewModel.setAnswer3(radioButton3.getText().toString());
                }

                if (radioButton4 != null) {
                    quizViewModel.setAnswer4(radioButton4.getText().toString());
                }

                if (radioButton5 != null) {
                    quizViewModel.setAnswer5(radioButton5.getText().toString());
                }
            }
        });

        return root;
    }
}
