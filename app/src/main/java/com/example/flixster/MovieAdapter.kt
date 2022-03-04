package com.example.flixster

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val MOVIE_EXTRA = "MOVIE_EXTRA"
private const val TAG = "MovieAdapter"

class MovieAdapter(private val context: Context, private val movies: List<Movie>)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() { // extends RecyclerView Adapter

    // Expensive operation; creates a view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false) // result of function will be a view
        return ViewHolder(view) // inner class ViewHolder takes in this parameter
    }

    // Cheap operation; simply binds data to viewholder passed in
    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // take the data at that position and bind it into the viewholder
        Log.i(TAG, "onBindViewHolder position $position") // Logcat, onBindViewHolder only binds
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount() = movies.size // return number of items in our data set (one liner allows us to use this syntax instead of "return movies.size"


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener { // our class ViewHolder extends from RecyclerView viewholder
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)
        private val ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)

        init {  // register click listener
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie) { // get references to individual components in the itemView (image view, 2 text views) and populate it with the correct data in the movie
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            // populate imageview
            Glide.with(context).load(movie.posterImageUrl).into(ivPoster) // context is MainActivity, ivPoster is the id from item_movie
        }

        override fun onClick(p0: View?) {
            // 1. Get notified of the particular movie which was clicked on
            val movie = movies[adapterPosition] // position of this particular ViewHolder among the data set
            //Toast.makeText(context, movie.title, Toast.LENGTH_SHORT).show() // Toast message appears at bottom of screen when movie is clicked on

            // 2. Use Intent system to navigate to the new screen (which movie was tapped on?)
            // create this new activity as DetailActivity
            val intent = Intent(context, DetailActivity::class.java) // we want to navigate to DetailActivity
                // pass in the movie as an extra into the Intent so DetailActivity can get the movie data out and display it
                // intent.putExtra("movie_title", movie.title) // would need to pass each individual attribute one by one instead of passing the whole movie object. Instead:
            intent.putExtra("MOVIE_EXTRA", movie) // implement Parseable/Serializable
            context.startActivity(intent)
        }
    }

}
