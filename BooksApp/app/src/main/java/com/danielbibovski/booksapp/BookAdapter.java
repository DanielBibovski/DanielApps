package com.danielbibovski.booksapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {

    private List<Book> books;

    public BookAdapter() {
        books = new ArrayList<>();
    }

    public void addAllBooks(List<Book> bookList) {
        books.clear();
        books.addAll(bookList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new BookHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        Book currentBook = books.get(position);
        holder.textViewTitle.setText(currentBook.getTitle());
        holder.textViewDescription.setText(currentBook.getDescription());
        holder.textViewAnagramPairs.setText(String.valueOf(currentBook.getAnagramPairs()));
        holder.textViewAnagramPairs2.setText("Total number of anagram pairs: " + String.valueOf(currentBook.getAnagramPairs()));

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewAnagramPairs;
        private TextView textViewAnagramPairs2;


        public BookHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_discription);
            textViewAnagramPairs = itemView.findViewById(R.id.text_view_anagrampairs);
            textViewAnagramPairs2 = itemView.findViewById(R.id.text_view_anagrampairs2);

        }
    }
}
