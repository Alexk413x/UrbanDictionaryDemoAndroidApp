package com.alexk413x.mobile.urbandictionarydemo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexk413x.mobile.urbandictionarydemo.Models.WordDefinition;
import com.alexk413x.mobile.urbandictionarydemo.R;

import java.util.List;


public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private List<WordDefinition> searchResults;

    public SearchResultsAdapter(List<WordDefinition> searchResults){
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.search_result_word_row, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsAdapter.ViewHolder viewHolder, int position) {
        WordDefinition contact = searchResults.get(position);

        viewHolder.wordView.setText(contact.getWord());
        viewHolder.definitionView.setText(contact.getDefinition());
        viewHolder.thumbsUpCountView.setText(String.valueOf(contact.getThumbsUp()));
        viewHolder.thumbsDownCountView.setText(String.valueOf(contact.getThumbsDown()));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordView;
        TextView definitionView;
        TextView thumbsUpCountView;
        TextView thumbsDownCountView;

        ViewHolder(View rowView) {
            super(rowView);

            wordView = itemView.findViewById(R.id.word_text);
            definitionView = itemView.findViewById(R.id.definition_text);
            thumbsUpCountView = itemView.findViewById(R.id.thumbs_up_count);
            thumbsDownCountView = itemView.findViewById(R.id.thumbs_down_count);
        }
    }
}