package com.limhh0823.ex12_recyclerweather02.Utils;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
	static final String BASEURL = "https://api.openweathermap.org/data/2.5/";
	static final String APPID = "af953120ad77a693a698f823b8f11649";
	static final String UNIT = "metric";
	@GET("wether")
	Call<JsonObject> getDailyWeather(
            @Query("appid") String appid,
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("units") String units);
	@GET("forecast")
	Call<JsonObject> getWeeklyWeather(
			@Query("appid") String appid,
			@Query("lat") double lat,
			@Query("lon") double lon,
			@Query("units") String units);
}


