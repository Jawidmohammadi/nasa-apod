package com.jawidmohammadi.nasaapod.service;

import com.jawidmohammadi.nasaapod.model.Apod;
import java.util.Date;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApodService {

  @GET("planetary/apod")
  Call<Apod> get(@Query("api_key")String apikey, @Query("date") String Date);

}
