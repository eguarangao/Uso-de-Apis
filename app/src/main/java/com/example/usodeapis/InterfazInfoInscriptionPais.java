package com.example.usodeapis;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfazInfoInscriptionPais {
    @GET("/public/v1/users?")
    Call<List<data>> find(@Query("id")String id);
    @GET("/public/v1/users")
    Call<List<data>> getAll();
}
