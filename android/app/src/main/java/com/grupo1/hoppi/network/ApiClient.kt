package com.grupo1.hoppi.network

import com.grupo1.hoppi.network.auth.AuthApiService
import com.grupo1.hoppi.network.likes.LikesApiService
import com.grupo1.hoppi.network.posts.PostsApiService
import com.grupo1.hoppi.network.users.UsersApiService
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:3000/"
    //private const val BASE_URL = "https://hoppi.onrender.com/"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
        coerceInputValues = true
        encodeDefaults = true
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val auth: AuthApiService = retrofit.create(AuthApiService::class.java)
    val users: UsersApiService = retrofit.create(UsersApiService::class.java)
    val posts: PostsApiService = retrofit.create(PostsApiService::class.java)
    val likes: LikesApiService = retrofit.create(LikesApiService::class.java)
}
