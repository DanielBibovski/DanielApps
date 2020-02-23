package com.danielbibovski.booksapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    void insert(Book book);

    @Query("DELETE FROM book_table")
    void deleteAllBooks();

    @Query("SELECT * FROM book_table ORDER BY anagramPairs DESC")
    LiveData<List<Book>> getAllBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(Book... books);
}
