package dev.sanskar.showmemovies.data

import android.util.Log
import dev.sanskar.showmemovies.data.network.retrofitService

private const val TAG = "MoviesRepository"
object MoviesRepository {
    suspend fun getPopularMovies(pageNumber: Int): PopularMovieResponse {
        // Get retrofit response
        Log.d(TAG, "getPopularMovies: Making popular movies API call")
        return retrofitService.getPopularMovies(pageNumber = pageNumber)
    }
}