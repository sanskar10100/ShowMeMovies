package dev.sanskar.showmemovies.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.sanskar.showmemovies.data.MoviesRepository
import dev.sanskar.showmemovies.data.PopularMovieResponse
import dev.sanskar.showmemovies.data.Result
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {
    val movies = MutableLiveData<MutableList<Result>>()
    var pageNumber = 1
    val error = MutableLiveData("")

    init {
        loadMovies()
    }

    fun loadMovies() {
        Log.d(TAG, "loadMovies: called with page number $pageNumber")
        viewModelScope.launch {
            try {
                val response = MoviesRepository.getPopularMovies(pageNumber)
                Log.d(TAG, "loadMovies: Response received; Forwarding;")
                addResponseToList(response)
                pageNumber++ // For next call
            } catch (e: Exception) {
                error.value = e.message
                Log.d(TAG, "getPopularMovies: API called failed with ${e.message}")
            }
        }
    }

    private fun addResponseToList(response: PopularMovieResponse) {
        val result = response.results
        val movieList = mutableListOf<Result>()
        movieList.addAll(movies.value ?: emptyList())
        movieList.addAll(result)
        movies.value = movieList
    }
}