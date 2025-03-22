package ru.pifpaf;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PifPafAPI {
    @GET("/pifpaf.php")
    Call<List<DataFromDB>> sendQuery(@Query("q") String s);

    @GET("/pifpaf.php")
    Call<List<DataFromDB>> sendQuery(@Query("name") String name, @Query("score") int score, @Query("kills") int kills);
}
