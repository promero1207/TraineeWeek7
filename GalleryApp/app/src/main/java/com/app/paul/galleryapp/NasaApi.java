package com.app.paul.galleryapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface for querying api returning request call
 */
public interface NasaApi {

    @GET("photos")
    Call<RoverImage> getRocketImages(@Query("sol") int sol,
                                     @Query("page") int page,
                                     @Query("api_key") String key);

}
