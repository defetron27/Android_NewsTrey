package com.max.def.newstrey.Interface;

import com.max.def.newstrey.Models.NewsQueryResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService
{
    @GET("everything")
    Call<NewsQueryResponseModel> getQueryNews(@Query("q") String q, @Query("apiKey") String apiKey);
}
