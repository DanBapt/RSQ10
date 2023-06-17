package com.example.rsq.Pump;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.rsq.QuizViewModel;
import com.example.rsq.R;

public class PumpResultsFragment extends Fragment {
    private QuizViewModel quizViewModel;

    public PumpResultsFragment() {
        // Required empty public constructor
    }

    public static PumpResultsFragment newInstance() {
        return new PumpResultsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pump_results, container, false);

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        // Supposez que vous avez un TextView pour chaque réponse
        TextView answer1TextView = root.findViewById(R.id.answer1);
        TextView answer2TextView = root.findViewById(R.id.answer2);
        TextView answer3TextView = root.findViewById(R.id.answer3);



        return root;
    }
}
