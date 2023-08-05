package aandrosov.api.books;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksApi {

    public static String API_URL = "https://openlibrary.org/";

    private final BookApiService service;

    public BooksApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(BookApiService.class);
    }

    public String[][] search(String query, int limit, int offset) throws IOException, BooksException {
        Response<JsonObject> jsonResponse = service.search(query, "isbn", limit, offset).execute();

        if(!jsonResponse.isSuccessful()) {
            throw new BooksException(jsonResponse.toString());
        }

        JsonObject jsonResponseObject = jsonResponse.body();

        if(jsonResponseObject == null) {
            throw new BooksException("Json response is empty!\n" + jsonResponse);
        }

        JsonArray results = jsonResponseObject.getAsJsonArray("docs");
        Gson gson = new Gson();

        String[][] isbns = new String[results.size()][];
        for(int i = 0; i < isbns.length; i++) {
            JsonArray bookIsbns = results.get(i).getAsJsonObject().getAsJsonArray("isbn");
            isbns[i] = gson.fromJson(bookIsbns, String[].class);
        }

        return isbns;
    }

    public Book fetchBook(String isbn) throws IOException, BooksException {
        Response<JsonObject> jsonResponse = service.fetchBook("ISBN:" + isbn).execute();

        if(!jsonResponse.isSuccessful()) {
            throw new BooksException(jsonResponse.toString());
        }

        JsonObject jsonResponseObject = jsonResponse.body();

        if(jsonResponseObject == null) {
            throw new BooksException("Json response is empty!\n" + jsonResponse);
        }

        return new Gson().fromJson(jsonResponseObject.get("ISBN:" + isbn), Book.class);
    }
}
