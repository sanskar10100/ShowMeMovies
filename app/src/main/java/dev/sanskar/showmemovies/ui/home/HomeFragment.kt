package dev.sanskar.showmemovies.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.sanskar.showmemovies.R
import dev.sanskar.showmemovies.data.Result
import dev.sanskar.showmemovies.databinding.FragmentHomeBinding
import dev.sanskar.showmemovies.databinding.LayoutPopularMovieBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

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
    }
}

class MovieAdapter(private val context: Context) : ListAdapter<Result, MovieAdapter.ViewHolder>(MovieDiffCallback()) {
    inner class ViewHolder(private val binding: LayoutPopularMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Result) {
            with (binding) {
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