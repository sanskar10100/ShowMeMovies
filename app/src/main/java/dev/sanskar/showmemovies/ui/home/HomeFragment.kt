package dev.sanskar.showmemovies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.sanskar.showmemovies.data.Result
import dev.sanskar.showmemovies.databinding.FragmentHomeBinding
import dev.sanskar.showmemovies.databinding.LayoutPopularMovieBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val model by viewModels<HomeViewModel>()
    private lateinit var adapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MoviesAdapter().also {
            binding.listPopularMovies.adapter = it
            adapter = it
        }

        model.movies.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                adapter.submitList(it)
            }
        }

        detectRecyclerViewBottom()
    }

    private fun detectRecyclerViewBottom() {
        binding.listPopularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    model.pageNumber++
                    model.loadMovies()
                }
            }
        })
    }
}

class MoviesAdapter : ListAdapter<Result, MoviesAdapter.ViewHolder>(MovieDiffCallback()) {
    inner class ViewHolder(private val binding: LayoutPopularMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Result) {
            with (binding) {
                imageViewMovie.load(movie.imageUrl)
                textViewMovieTitle.text = movie.title
                textViewMovieOverview.text = movie.overview
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutPopularMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Result>() {
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }

}