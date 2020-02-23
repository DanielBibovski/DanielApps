package com.danielbibovski.booksapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private BookViewModel bookViewModel;
    private RecyclerView recyclerView;
    private static final long TWO_MINUTES_IN_MILLIS = 2 * 60 * 1000;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        bookAdapter = new BookAdapter();
        recyclerView.setAdapter(bookAdapter);
        requestQueue = Volley.newRequestQueue(this);
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBooks();
    }

    private void getBooks() {
        bookViewModel.getAllBooks().observe(this, getAllBooksObserver);
    }

    private Observer<List<Book>> getAllBooksObserver = new Observer<List<Book>>() {
        @Override
        public void onChanged(@Nullable List<Book> books) {
            if (books == null || books.isEmpty()) {
                getBooksFromApi();
            } else {
                if ((books.get(0).getCreatedAt() + TWO_MINUTES_IN_MILLIS < Calendar.getInstance().getTimeInMillis())) {
                    bookViewModel.deleteAllBooks();
                } else {
                    displayBooks(books);
                }
            }
        }
    };

    private void getBooksFromApi() {
        String bookUrl = "https://webfactory.mk/courseapi/books.json";
        CustomStringRequest  request = new CustomStringRequest(
                Request.Method.GET,
                bookUrl,
                new Response.Listener<CustomStringRequest.ResponseM>() {
                    @Override
                    public void onResponse(CustomStringRequest.ResponseM response) {

                        try {
                            BookWraper bookWraper = parseResponse(response);
                            if (bookWraper != null && bookWraper.items != null) {
                                List<Book> books = new ArrayList<>();
                                for (int i = 0; i < bookWraper.items.size(); i++) {
                                    String title = bookWraper.items.get(i).volumeInfo.title;
                                    String[] arrOfTitle = title.split(" ");
                                    String description = bookWraper.items.get(i).volumeInfo.description;
                                    String[] arrOfDescription = description.split(" ");

                                    int anagramPairs = 0;
                                    description = description + "\n\n";
                                    for (String titles : arrOfTitle) {
                                        for (String descriptions : arrOfDescription) {
                                            if (isAnagram(titles, descriptions)) {
                                                description += anagramPairs+1 + ". " +titles + " and " + descriptions + " are anagrams. \n";
                                                anagramPairs++;
                                            }
                                        }
                                    }
                                    Book book = new Book(title, description, anagramPairs, Calendar.getInstance().getTimeInMillis());
                                    books.add(book);
                                }
                                bookViewModel.insertAll(books);
                            }
                        } catch (Exception error) {
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("CALL", error.getMessage(), error);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong! Check your internet connection!", Toast.LENGTH_SHORT).show();
                        Log.e("CALL", error.getMessage(), error);
                    }
                });
        requestQueue.add(request);


    }

    @Override
    protected void onStop() {
        super.onStop();
        bookViewModel.getAllBooks().removeObserver(getAllBooksObserver);
    }

    private BookWraper parseResponse(CustomStringRequest.ResponseM response) {
        String contentType = response.headers.get("Content-Type");
        if (contentType != null && contentType.contains("json")) {
            Gson gson = new Gson();
            return gson.fromJson(response.payload, BookWraper.class);
        } else if (contentType != null && contentType.contains("xml")) {
            XmlParserCreator parserCreator = new XmlParserCreator() {
                @Override
                public XmlPullParser createParser() {
                    try {
                        return XmlPullParserFactory.newInstance().newPullParser();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            GsonXml gsonXml = new GsonXmlBuilder()
                    .setXmlParserCreator(parserCreator)
                    .create();
            return gsonXml.fromXml(response.payload, BookWraper.class);
        }
        throw new RuntimeException("Something went wrong! Try again!");
    }

    private void displayBooks(List<Book> books) {
        bookAdapter.addAllBooks(books);
    }

    public boolean isAnagram(String a, String b) {

        // test for invalid input
        if (a == null || b == null || a.equals("") || b.equals(""))
            throw new IllegalArgumentException();

        // initial quick test for non-anagrams
        if (a.length() != b.length())
            return false;

        a = a.toLowerCase();
        b = b.toLowerCase();

        // populate a map with letters and frequencies of String b
        Map<Character, Integer> map = new HashMap<>();

        for (int k = 0; k < b.length(); k++) {
            char letter = b.charAt(k);

            if (!map.containsKey(letter)) {
                map.put(letter, 1);
            } else {
                Integer frequency = map.get(letter);
                map.put(letter, ++frequency);
            }
        }

        // test each letter in String a against data in the map
        // return if letter is absent in the map or its  frequency is 0
        // otherwise decrease the frequency by 1

        for (int k = 0; k < a.length(); k++) {
            char letter = a.charAt(k);

            if (!map.containsKey(letter))
                return false;

            Integer frequency = map.get(letter);

            if (frequency == 0)
                return false;
            else
                map.put(letter, --frequency);
        }
        return true;
    }


}
