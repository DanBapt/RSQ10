package com.example.rsq.Defibrillator;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
                generateAndDownloadPdf(resultsTextView.getText().toString());
            }
        });



        return root;
    }

    private void generateAndDownloadPdf(String content) {
        PdfDocument pdfDocument = new PdfDocument();

        // Create a new page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();

        // Start a page
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Draw content on the page
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawText(content, 10, 50, paint);

        // Finish the page
        pdfDocument.finishPage(page);

        // Write the document contents to a file
        String fileName = "results.pdf";
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), fileName);
        try {
            pdfDocument.writeTo(new FileOutputStream(outputFile));
            Toast.makeText(getContext(), "PDF téléchargé dans les téléchargements", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erreur lors de la création du PDF", Toast.LENGTH_LONG).show();
        }

        // Close the document
        pdfDocument.close();
    }
}