package com.limhh0823.ex12_recyclerweather02.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SetRetrofit {
	public static Retrofit retrofit() {
		Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(ApiService.BASEURL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
		return retrofit;
	}
}
