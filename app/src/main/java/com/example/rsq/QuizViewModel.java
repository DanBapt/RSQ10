package com.example.rsq;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizViewModel extends ViewModel {
    private MutableLiveData<List<String>> participantNames = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<HashMap<String, List<String>>> participantAnswers = new MutableLiveData<>(new HashMap<>());

    public void setParticipantNames(List<String> names) {
        this.participantNames.setValue(names);
    }

    public LiveData<List<String>> getParticipantNames() {
        return this.participantNames;
    }

    public void setParticipantAnswers(HashMap<String, List<String>> answers) {
        this.participantAnswers.setValue(answers);
    }

    public LiveData<HashMap<String, List<String>>> getParticipantAnswers() {
        return this.participantAnswers;
    }
}
