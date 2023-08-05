package aandrosov.api.books;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.Map;

interface BookApiService {

    @GET("search.json")
    Call<JsonObject> search(@Query("q") String query, @Query("fields") String fields, @Query("limit") int limit, @Query("offset") int offset);

    @GET("api/books?format=json&jscmd=data")
    Call<JsonObject> fetchBook(@Query("bibkeys") String isbn);
}
