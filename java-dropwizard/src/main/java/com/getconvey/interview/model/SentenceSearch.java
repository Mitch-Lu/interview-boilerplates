package com.getconvey.interview.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SentenceSearch {
    private final String word;
    private final List<Sentence> sentences;

    @JsonCreator
    public SentenceSearch(
        @JsonProperty("word") String word,
        @JsonProperty("sentences") List<Sentence> sentences) {
        this.word = word;
        this.sentences = sentences;
    }

    public String getWord() {
        return word;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }
}
