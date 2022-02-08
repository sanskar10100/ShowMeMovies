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
    val pageNumber = 1

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            val response = MoviesRepository.getPopularMovies(pageNumber)
            Log.d(TAG, "loadMovies: Response received; Forwarding;")
            addResponseToList(response)
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