package com.example.rsq.Pump;

import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// import com.example.rsq.AndroidManifest;
import com.example.rsq.QuizViewModel;
import com.example.rsq.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
                SpannableStringBuilder resultsStringBuilder = new SpannableStringBuilder();

                for (String participantName : participantAnswers.keySet()) {
                    // Ajouter le nom du participant.
                    SpannableString participantSpannable = new SpannableString(participantName + "\n");
                    participantSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, participantName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);  // Couleur rouge pour le nom du participant.
                    participantSpannable.setSpan(new AbsoluteSizeSpan(20, true), 0, participantName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);  // Taille 20dp pour le nom du participant.
                    resultsStringBuilder.append(participantSpannable);

                    List<String> answers = participantAnswers.get(participantName);
                    for (int i = 0; i < answers.size(); i++) {
                        // Fetch the question from the list of questions stored in QuizViewModel.
                        String question = quizViewModel.questions.get(i);
                        SpannableString questionSpannable = new SpannableString(question + "\n- ");
                        questionSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, question.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);  // Couleur bleue pour la question.
                        resultsStringBuilder.append(questionSpannable);

                        // Ajouter la réponse.
                        String answer = answers.get(i);
                        SpannableString answerSpannable = new SpannableString(answer + "\n");
                        answerSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, answer.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);  // Couleur noire pour la réponse.
                        resultsStringBuilder.append(answerSpannable);
                    }
                    resultsStringBuilder.append("\n");
                }

                resultsTextView.setText(resultsStringBuilder);
            }
        });



        Button downloadPdfButton = root.findViewById(R.id.downloadPdfButton);
        downloadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndDownloadPdf(quizViewModel.getParticipantAnswers().getValue());
            }
        });




        return root;
    }

    private void generateAndDownloadPdf(Map<String, List<String>> participantAnswers) {
        try {
            // Create a new PDF document
            StringBuilder filename = new StringBuilder("Pump_Evaluation_");
            for (String participantName : participantAnswers.keySet()) {
                filename.append(participantName).append("_");
            }
            filename.append(".pdf");  // Append the file extension
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename.toString())));
            document.open();

            // Set fonts and colors for the document
            Font fontParticipant = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL, new BaseColor(255, 0, 0)); // Red for participant name
            Font fontQuestion = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, new BaseColor(0, 0, 255)); // Blue for question
            Font fontAnswer = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL, new BaseColor(0, 0, 0)); // Black for answer

            // Loop over the participant answers
            for (String participantName : participantAnswers.keySet()) {
                // Create a new Paragraph for each participant, question and answer
                Paragraph participantParagraph = new Paragraph(participantName + "\n", fontParticipant);
                document.add(participantParagraph);

                List<String> answers = participantAnswers.get(participantName);
                for (int i = 0; i < answers.size(); i++) {
                    String question = quizViewModel.questions.get(i);
                    Paragraph questionParagraph = new Paragraph(question + "\n- ", fontQuestion);
                    document.add(questionParagraph);

                    String answer = answers.get(i);
                    Paragraph answerParagraph = new Paragraph(answer + "\n", fontAnswer);
                    document.add(answerParagraph);
                }

                // Add a line break after each participant's answers
                document.add(new Paragraph("\n"));
            }

            // Close the document
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}