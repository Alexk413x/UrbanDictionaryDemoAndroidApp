package com.alexk413x.mobile.urbandictionarydemo.Models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WordDefinition {
    private String word;
    private String definition;
    private int thumbsUp;
    private int thumbsDown;
    private int definitionId;
    private String author;
    private Calendar createdDate;
    private String permalink;
    private String example;
    private String currentVote;
    private ArrayList<String> soundUrls;

    public WordDefinition(String word, String definition, int thumbsUp, int thumbsDown, int definitionId, String author, Calendar createdDate, String permalink, String example, String currentVote, ArrayList<String> soundUrls) {
        this.word = word;
        this.definition = definition;
        this.thumbsUp = thumbsUp;
        this.thumbsDown = thumbsDown;
        this.definitionId = definitionId;
        this.author = author;
        this.createdDate = createdDate;
        this.permalink = permalink;
        this.example = example;
        this.currentVote = currentVote;
        this.soundUrls = soundUrls;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        String definition = this.definition;
        definition = definition.replace("[", "");
        definition = definition.replace("]", "");
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }


    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public int getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(int thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public int getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(int definitionId) {
        this.definitionId = definitionId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getCurrentVote() {
        return currentVote;
    }

    public void setCurrentVote(String currentVote) {
        this.currentVote = currentVote;
    }

    public ArrayList<String> getSoundUrls() {
        return soundUrls;
    }

    public void setSoundUrls(ArrayList<String> soundUrls) {
        this.soundUrls = soundUrls;
    }
}
