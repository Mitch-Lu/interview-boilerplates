package com.getconvey.interview.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Sentence {
    private final String sentence;
    private final int count;

    @JsonCreator
    public Sentence(@JsonProperty("sentence") String sentence, @JsonProperty("count") int count) {
        this.sentence = sentence;
        this.count = count;
    }

    public String getSentence() {
        return sentence;
    }

    public int getCount() {
        return count;
    }
}
