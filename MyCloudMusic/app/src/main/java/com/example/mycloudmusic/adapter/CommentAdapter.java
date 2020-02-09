package com.example.mycloudmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context context;
    List<Comment> datum = new ArrayList<>();
    private final LayoutInflater inflater;

    public CommentAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(inflater.inflate(R.layout.item_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return datum.size();
    }

    public void setDatum(List<Comment> datum) {
        this.datum.clear();
        this.datum.addAll(datum);
        notifyDataSetChanged();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
