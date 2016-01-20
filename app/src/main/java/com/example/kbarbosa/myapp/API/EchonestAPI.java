package com.example.kbarbosa.myapp.API;


import com.example.kbarbosa.myapp.model.ArtistResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by kbarbosa on 14/12/2015.
 */
public interface EchonestAPI {

    String api_key = "";
    String BASE_URL = "http://developer.echonest.com/api/v4/";

    @GET("artist/search")
    Call<ArtistResponse> searchArtist(@Query("api_key") String api_key,
                                      @Query("format") String format,
                                      @Query("name") String name,
                                      @Query("bucket") String bucket,
                                      @Query("results") int results);

}
