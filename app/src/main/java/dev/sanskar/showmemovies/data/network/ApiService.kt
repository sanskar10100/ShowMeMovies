package dev.sanskar.showmemovies.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.sanskar.showmemovies.BuildConfig
import dev.sanskar.showmemovies.data.PopularMovieResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

const val BASE_URL = "https://api.themoviedb.org/3/movie/"

val moshi = MoshiConverterFactory.create(
    Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
)

val retrofit: Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(moshi)
        .client(client)
        .build()

interface MoviesApi {
    @GET("popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") pageNumber: Int = 1
    ): PopularMovieResponse
}

val retrofitService: MoviesApi by lazy {
    retrofit.create(MoviesApi::class.java)
}

