package com.danielbibovski.booksapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_table")
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int anagramPairs;

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    private long createdAt;

    public Book(String title, String description, int anagramPairs, long createdAt) {

        this.title = title;
        this.description = description;
        this.anagramPairs = anagramPairs;
        this.createdAt = createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    public int getAnagramPairs() {
        return anagramPairs;
    }

}
