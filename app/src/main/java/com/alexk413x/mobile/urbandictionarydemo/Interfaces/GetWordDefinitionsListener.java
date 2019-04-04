package com.alexk413x.mobile.urbandictionarydemo.Interfaces;

import com.alexk413x.mobile.urbandictionarydemo.Models.WordDefinition;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public interface GetWordDefinitionsListener {

    void getResult(ArrayList<WordDefinition> wordDefinitionList);

    void jsonException(String error);

    void getError(VolleyError error);

    void isCacheRefresh();
}
