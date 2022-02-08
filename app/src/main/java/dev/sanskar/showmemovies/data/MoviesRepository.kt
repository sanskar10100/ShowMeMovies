package dev.sanskar.showmemovies.data

import android.util.Log
import dev.sanskar.showmemovies.data.network.retrofitService
import java.lang.Exception

private const val TAG = "MoviesRepository"
object MoviesRepository {
    suspend fun getPopularMovies(pageNumber: Int): PopularMovieResponse {
        // Get retrofit response
        Log.d(TAG, "getPopularMovies: Making popular movies API call")
        try {
            return retrofitService.getPopularMovies(pageNumber = pageNumber)
        } catch (e: Exception) {
            Log.d(TAG, "getPopularMovies: API called failed with ${e.message}")
        }

        return PopularMovieResponse(1, emptyList(), 1, 1)
    }
}