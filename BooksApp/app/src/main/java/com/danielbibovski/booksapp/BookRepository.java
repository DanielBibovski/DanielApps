package com.danielbibovski.booksapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {

    private BookDao bookDao;
    private LiveData<List<Book>> allBooks;

    public BookRepository(Application application){
        BookDatabase database = BookDatabase.getInstance(application);
        bookDao = database.bookDao();
        allBooks = bookDao.getAllBooks();
    }
    public void insert(Book book){
        new InsertBookAsyncTask(bookDao).execute(book);

    }

    public void insertAll(List<Book> books) {
        new InsertBookAsyncTask(bookDao).execute(books.toArray(new Book[]{}));
    }

    public void deleteAllBooks(){
        new DeleteAllBooksAsyncTask(bookDao).execute();

    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

    private static class InsertBookAsyncTask extends AsyncTask<Book, Void, Void>{

        private BookDao bookDao;

        private InsertBookAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.insertAll(books);
            return null;
        }
    }

    private static class DeleteAllBooksAsyncTask extends AsyncTask<Void, Void, Void>{

        private BookDao bookDao;

        private DeleteAllBooksAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            bookDao.deleteAllBooks();
            return null;
        }
    }
}
