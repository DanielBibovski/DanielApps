package com.danielbibovski.booksapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private BookRepository repository;
    private LiveData<List<Book>> allBooks;

    public BookViewModel(@NonNull Application application) {
        super(application);

        repository = new BookRepository(application);
        allBooks = repository.getAllBooks();
    }

    public void insert(Book book){
        repository.insert(book);
    }

    public void deleteAllBooks(){
        repository.deleteAllBooks();
    }
    public LiveData<List<Book>> getAllBooks(){
        return allBooks;
    }

    public void insertAll(List<Book> books){
        repository.insertAll(books);
    }
}
