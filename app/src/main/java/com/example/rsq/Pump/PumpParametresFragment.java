package com.example.rsq.Pump;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rsq.R;

import java.util.ArrayList;
import java.util.List;

public class PumpParametresFragment extends Fragment {

    private LinearLayout participantLayout;
    private EditText numParticipantsEditText;
    private Button submitButton;
    private Button submitNamesButton;  // Bouton supplémentaire

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pump_parametres, container, false);

        participantLayout = root.findViewById(R.id.participant_layout);
        numParticipantsEditText = root.findViewById(R.id.num_participants);
        submitButton = root.findViewById(R.id.submit_button_participant);

        // Initialisation du bouton submitNamesButton et rendre invisible par défaut
        submitNamesButton = new Button(getContext());
        submitNamesButton.setText("Valider les noms");
        submitNamesButton.setVisibility(View.GONE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numParticipants;
                try {
                    numParticipants = Integer.parseInt(numParticipantsEditText.getText().toString());
                    Log.d("NumParticipants", "Number of participants: " + numParticipants);
                } catch (NumberFormatException e) {
                    numParticipants = 0;
                }
                updateParticipantFields(numParticipants);
                submitNamesButton.setVisibility(View.VISIBLE); // Rendre le bouton visible après la validation

                submitNamesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int numParticipants = participantLayout.getChildCount() - 1;  // -1 pour exclure le bouton de validation
                        List<String> participantNames = new ArrayList<>();
                        for (int i = 0; i < numParticipants; i++) {
                            EditText participantNameEditText = (EditText) participantLayout.getChildAt(i);
                            participantNames.add(participantNameEditText.getText().toString());
                        }

                        ((PumpActivity) getActivity()).updateParticipantNames(participantNames);
                    }
                });
            }
        });

        participantLayout.addView(submitNamesButton); // Ajout du bouton au layout

        return root;
    }

    private void updateParticipantFields(int numParticipants) {
        participantLayout.removeAllViews();
        for (int i = 0; i < numParticipants; i++) {
            EditText participantNameEditText = new EditText(getContext());
            participantNameEditText.setHint("Participant " + (i + 1));
            participantLayout.addView(participantNameEditText);
        }
        participantLayout.addView(submitNamesButton);  // Assurez-vous que le bouton "Submit Names" est toujours en bas
    }
}
