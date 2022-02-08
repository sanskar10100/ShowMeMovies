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
import coil.transform.RoundedCornersTransformation
import com.google.android.material.snackbar.Snackbar
import dev.sanskar.showmemovies.R
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
                hideShimmer()
                adapter.submitList(it)
            }
        }

        model.error.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                hideShimmer()
                Snackbar.make(binding.root, "Network call failed with $it. Swipe down to try again!", Snackbar.LENGTH_SHORT).show()
            }
        }

        detectRecyclerViewBottom()
    }

    private fun hideShimmer() {
        binding.shimmerView.visibility = View.GONE
    }

    private fun showShimmer() {
        binding.shimmerView.visibility = View.VISIBLE
    }

    private fun detectRecyclerViewBottom() {
        binding.root.viewTreeObserver.addOnScrollChangedListener {
            if (binding.shimmerView.visibility == View.GONE) {
                val view = binding.root.getChildAt(binding.root.childCount - 1) as View
                val diff: Int = view.bottom - (binding.root.height + binding.root.scrollY)
                if (diff == 0) {
                    showShimmer()
                    model.loadMovies()
                }
            }
        }
    }
}

class MoviesAdapter : ListAdapter<Result, MoviesAdapter.ViewHolder>(MovieDiffCallback()) {
    inner class ViewHolder(private val binding: LayoutPopularMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Result) {
            with (binding) {
                imageViewMovie.load(movie.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.sample)
                    transformations(RoundedCornersTransformation(10f))
                }
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