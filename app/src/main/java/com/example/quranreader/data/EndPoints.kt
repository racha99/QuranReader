package com.example.quranreader.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EndPoints {


    @GET("by_key/{verse_key}?fields=page_number")
    fun getDoctor(@Path("verse_key") verse_key:String?): Call<Ayah>
}