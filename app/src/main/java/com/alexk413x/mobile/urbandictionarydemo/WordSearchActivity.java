package com.alexk413x.mobile.urbandictionarydemo;

import android.content.DialogInterface;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexk413x.mobile.urbandictionarydemo.API.ApiManager;
import com.alexk413x.mobile.urbandictionarydemo.API.WordDefinitionApi;
import com.alexk413x.mobile.urbandictionarydemo.Adapters.SearchResultsAdapter;
import com.alexk413x.mobile.urbandictionarydemo.Enums.WordDefinitionSortTypes;
import com.alexk413x.mobile.urbandictionarydemo.Interfaces.GetWordDefinitionsListener;
import com.alexk413x.mobile.urbandictionarydemo.Models.WordDefinition;
import com.alexk413x.mobile.urbandictionarydemo.Utils.NetworkUtils;
import com.alexk413x.mobile.urbandictionarydemo.Utils.WordDefinitionSoringUtils;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class WordSearchActivity extends AppCompatActivity {

    SearchView searchBox;
    TextView noNetworkConnectionView;
    ProgressBar loadingSpinner;
    TextView noResultsView;
    RecyclerView resultsRecyclerView;
    ImageButton sortResultsButton;

    String previousQuery = "";
    Parcelable recyclerViewState = null;
    WordDefinitionSortTypes sortResultsBy = WordDefinitionSortTypes.MOST_THUMBS_UP;
    ArrayList<WordDefinition> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiManager.getInstance(this);

        setContentView(R.layout.activity_word_search);

        searchBox = findViewById(R.id.search_box);
        noNetworkConnectionView = findViewById(R.id.no_network_text);
        loadingSpinner = findViewById(R.id.loading_spinner);
        noResultsView = findViewById(R.id.no_results_text);
        resultsRecyclerView = findViewById(R.id.search_result_list);
        sortResultsButton = findViewById(R.id.sort_results_button);

        sortResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortResultsDialog();
            }
        });

        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public  boolean onQueryTextSubmit(String query) {
                searchUrbanDictionary(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchBox.setQueryHint(getResources().getString(R.string.search_hint));

        resultsRecyclerView.setAdapter(new SearchResultsAdapter(searchResults));
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_word_search_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sortResultsBy = (WordDefinitionSortTypes) savedInstanceState.get("sort_order");
        updateSortButtonState();
        searchBox.setQuery(savedInstanceState.getString("query"), true);
        searchBox.clearFocus();
        int focusId = savedInstanceState.getInt("focused_view");
        if(focusId != 0){
            findViewById(focusId).requestFocus();
        }
        recyclerViewState = savedInstanceState.getParcelable("scroll_position");
    }

    @Override
    public void onSaveInstanceState(Bundle unsavedInstanceState) {
        super.onSaveInstanceState(unsavedInstanceState);
        unsavedInstanceState.putString("query", searchBox.getQuery().toString());
        unsavedInstanceState.putSerializable("sort_order", sortResultsBy);

        if(getCurrentFocus() != null) {
            unsavedInstanceState.putInt("focused_view", getCurrentFocus().getId());
        }

        if(resultsRecyclerView.getLayoutManager() != null) {
            unsavedInstanceState.putParcelable("scroll_position", resultsRecyclerView.getLayoutManager().onSaveInstanceState());
        }
    }

    private void searchUrbanDictionary(final String query) {
        showLoadingSpinner();
        searchBox.clearFocus();

        WordDefinitionApi.get(query, new GetWordDefinitionsListener() {

            boolean isCacheRefresh = false;

            @Override
            public void getResult(ArrayList<WordDefinition> wordDefinitionList) {
                checkNetwork();
                updateSearchResults(wordDefinitionList, !previousQuery.equals(query));

                previousQuery = query;

                if(!isCacheRefresh || !NetworkUtils.isConnectedToNetwork(getApplicationContext())) {
                    hideLoadingSpinner();
                } else {
                    isCacheRefresh = false;
                }
            }

            @Override
            public void jsonException(String error) {
                checkNetwork();
                clearSearchResults();
                hideLoadingSpinner();
            }

            @Override
            public void getError(VolleyError error) {
                if(error instanceof NoConnectionError || error instanceof NetworkError) {
                    checkNetwork();
                }
                hideLoadingSpinner();
            }

            @Override
            public void isCacheRefresh() {
                isCacheRefresh = true;
            }
        });
    }

    private void updateSearchResults(ArrayList<WordDefinition> wordDefinitionList, boolean scrollToTop){

        new WordDefinitionSoringUtils().SortWordDefinitions(wordDefinitionList, sortResultsBy);

        searchResults.clear();
        searchResults.addAll(wordDefinitionList);
        checkSearchResultsEmpty();

        if(resultsRecyclerView.getAdapter() != null) {
            resultsRecyclerView.getAdapter().notifyDataSetChanged();
        }

        if(recyclerViewState != null && resultsRecyclerView.getLayoutManager() != null) {
            resultsRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            recyclerViewState = null;
        } else {
            if(scrollToTop) {
                resultsRecyclerView.scrollToPosition(0);
            }
        }
    }

    private void clearSearchResults(){
        searchResults.clear();
        checkSearchResultsEmpty();

        if(resultsRecyclerView.getAdapter() != null) {
            resultsRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public void checkSearchResultsEmpty(){
        boolean isEmpty = searchResults.isEmpty();

        if(isEmpty){
            resultsRecyclerView.setVisibility(View.GONE);
            noResultsView.setVisibility(View.VISIBLE);
        } else {
            resultsRecyclerView.setVisibility(View.VISIBLE);
            noResultsView.setVisibility(View.GONE);
        }
    }

    private void updateSortButtonState(){
        switch (sortResultsBy) {
            case MOST_THUMBS_UP:
                sortResultsButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green_dark));
                sortResultsButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_thumb_up_24px));
                break;
            case LEAST_THUMBS_UP:
                sortResultsButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red_dark));
                sortResultsButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_thumb_up_24px));
                break;
            case MOST_THUMBS_DOWN:
                sortResultsButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green_dark));
                sortResultsButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_thumb_down_24px));
                break;
            case LEAST_THUMBS_DOWN:
                sortResultsButton.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red_dark));
                sortResultsButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_thumb_down_24px));
                break;
        }
    }

    private void showSortResultsDialog(){
        CharSequence[] radioOptions = {
            getResources().getString(R.string.most_thumbs_up),
            getResources().getString(R.string.most_thumbs_down),
            getResources().getString(R.string.least_thumbs_up),
            getResources().getString(R.string.least_thumbs_down)
        };

        int radioSelected = -1;
        switch (sortResultsBy) {
            case MOST_THUMBS_UP:
                radioSelected = 0;
                break;
            case LEAST_THUMBS_UP:
                radioSelected = 2;
                break;
            case MOST_THUMBS_DOWN:
                radioSelected = 1;
                break;
            case LEAST_THUMBS_DOWN:
                radioSelected = 3;
                break;
        }

        AlertDialog.Builder resultsDialog = new AlertDialog.Builder(this);
        resultsDialog.setTitle(R.string.sort_by);
        resultsDialog.setSingleChoiceItems(radioOptions, radioSelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        sortResultsBy = WordDefinitionSortTypes.MOST_THUMBS_UP;
                        break;
                    case 1:
                        sortResultsBy = WordDefinitionSortTypes.MOST_THUMBS_DOWN;
                        break;
                    case 2:
                        sortResultsBy = WordDefinitionSortTypes.LEAST_THUMBS_UP;
                        break;
                    case 3:
                        sortResultsBy = WordDefinitionSortTypes.LEAST_THUMBS_DOWN;
                        break;
                }
                updateSortButtonState();
                ArrayList<WordDefinition> tempUnsortedWordList = new ArrayList<>(searchResults);
                updateSearchResults(tempUnsortedWordList, true);
                dialog.dismiss();
            }
        });
        resultsDialog.create().show();
    }

    private void showAboutDialog(){
        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this);
        aboutDialog.setMessage(R.string.created_by)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        aboutDialog.create().show();
    }

    private void showLoadingSpinner(){
        noResultsView.setVisibility(View.GONE);
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    private void hideLoadingSpinner(){
        loadingSpinner.setVisibility(View.GONE);
    }

    private void checkNetwork(){
        if(NetworkUtils.isConnectedToNetwork(this)){
            hideNetworkConnection();
        } else {
            showNetworkConnection();
        }
    }

    private void showNetworkConnection(){
        noNetworkConnectionView.setVisibility(View.VISIBLE);
    }

    private void hideNetworkConnection(){
        noNetworkConnectionView.setVisibility(View.GONE);
    }
}
