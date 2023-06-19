package com.example.rsq.Defibrillator;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.rsq.QuizViewModel;
import com.example.rsq.R;

import java.util.List;
import java.util.Map;

public class DefibrillatorResultsFragment extends Fragment {
    private QuizViewModel quizViewModel;

    public DefibrillatorResultsFragment() {
        // Required empty public constructor
    }

    public static DefibrillatorResultsFragment newInstance() {
        return new DefibrillatorResultsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_defibrillator_results, container, false);

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        TextView resultsTextView = root.findViewById(R.id.resultsTextView);
        ForegroundColorSpan colorSpanParticipant = new ForegroundColorSpan(Color.RED);  // Couleur bleue pour le nom du participant.
        ForegroundColorSpan colorSpanQuestion = new ForegroundColorSpan(Color.BLUE);  // Couleur rouge pour la question.
        ForegroundColorSpan colorSpanAnswer = new ForegroundColorSpan(Color.BLACK);  // Couleur verte pour la réponse.

    //    Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.your_font);  // Remplacez "your_font" par le nom de votre police.
     //   TypefaceSpan typefaceSpan = new TypefaceSpan(typeface);
        // Observe the participant answers in the ViewModel
        quizViewModel.getParticipantAnswers().observe(getViewLifecycleOwner(), new Observer<Map<String, List<String>>>() {
            @Override
            public void onChanged(Map<String, List<String>> participantAnswers) {
                StringBuilder resultsStringBuilder = new StringBuilder();

                for (String participantName : participantAnswers.keySet()) {
                    resultsStringBuilder.append(participantName);
                    resultsStringBuilder.append("\n");

                    List<String> answers = participantAnswers.get(participantName);
                    for (int i = 0; i < answers.size(); i++) {
                        // Fetch the question from the list of questions stored in QuizViewModel.
                        String question = quizViewModel.questions.get(i);
                        resultsStringBuilder.append(question);
                        resultsStringBuilder.append("\n- ");
                        resultsStringBuilder.append(answers.get(i));
                        resultsStringBuilder.append("\n");
                    }
                    resultsStringBuilder.append("\n");
                }

                resultsTextView.setText(resultsStringBuilder.toString());
            }
        });

        return root;
    }
}
