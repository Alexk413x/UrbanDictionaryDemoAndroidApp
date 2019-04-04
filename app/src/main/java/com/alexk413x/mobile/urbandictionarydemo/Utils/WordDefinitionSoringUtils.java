package com.alexk413x.mobile.urbandictionarydemo.Utils;

import com.alexk413x.mobile.urbandictionarydemo.Enums.WordDefinitionSortTypes;
import com.alexk413x.mobile.urbandictionarydemo.Models.WordDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WordDefinitionSoringUtils {

    public ArrayList<WordDefinition> SortWordDefinitions(ArrayList<WordDefinition> wordDefinitionList, WordDefinitionSortTypes sortType) {
        switch (sortType) {
            case MOST_THUMBS_UP:
                return sortWordDefinitionsByMostThumbsUp(wordDefinitionList);
            case LEAST_THUMBS_UP:
                return sortWordDefinitionsByLeastThumbsUp(wordDefinitionList);
            case MOST_THUMBS_DOWN:
                return sortWordDefinitionsByMostThumbsDown(wordDefinitionList);
            case LEAST_THUMBS_DOWN:
                return sortWordDefinitionsByLeastThumbsDown(wordDefinitionList);
            default:
                return sortWordDefinitionsByMostThumbsUp(wordDefinitionList);
        }
    }

    private ArrayList<WordDefinition> sortWordDefinitionsByMostThumbsUp(ArrayList<WordDefinition> wordDefinitionList){
        sortWordDefinitionsByLeastThumbsUp(wordDefinitionList);
        Collections.reverse(wordDefinitionList);
        return wordDefinitionList;
    }

    private ArrayList<WordDefinition> sortWordDefinitionsByLeastThumbsUp(ArrayList<WordDefinition> wordDefinitionList){
        Collections.sort(wordDefinitionList, new Comparator<WordDefinition>(){
            @Override
            public int compare(WordDefinition word1, WordDefinition word2) {
                return Integer.compare(word1.getThumbsUp(), word2.getThumbsUp());
            }
        });

        return wordDefinitionList;
    }

    private ArrayList<WordDefinition> sortWordDefinitionsByMostThumbsDown(ArrayList<WordDefinition> wordDefinitionList){
        sortWordDefinitionsByLeastThumbsDown(wordDefinitionList);
        Collections.reverse(wordDefinitionList);
        return wordDefinitionList;
    }

    private ArrayList<WordDefinition> sortWordDefinitionsByLeastThumbsDown(ArrayList<WordDefinition> wordDefinitionList){
        Collections.sort(wordDefinitionList, new Comparator<WordDefinition>(){
            @Override
            public int compare(WordDefinition word1, WordDefinition word2) {
                return Integer.compare(word1.getThumbsDown(), word2.getThumbsDown());
            }
        });

        return wordDefinitionList;
    }

}


