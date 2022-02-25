package com.example.flixster

import org.json.JSONArray

data class Movie ( // represent one movie object in our UI
    // what attributes do we want to parse out from result?
    val movieID: Int,
    val title: String,
    val overview: String,
    private val posterPath: String, // we only need this for posterImageUrl
) {
    val posterImageUrl = "https://image.tmdb.org/t/p/w342/$posterPath" // https://image.tmdb.org/t/p/w342/6bCplVkhowCjTHXWv49UjRPn0eK.jpg
    companion object { // allows us to call methods on the Movie class without having an instance
        fun fromJsonArray(movieJsonArray: JSONArray): List<Movie>  { // iterate thru array and return list of Movie data classes
            val movies = mutableListOf<Movie>()
            for (i in 0 until movieJsonArray.length()) {
                val movieJson = movieJsonArray.getJSONObject(i) // at position i
                movies.add(
                    Movie( // has to be in order !
                        movieJson.getInt("id"), // exact "name" from JSON file
                        movieJson.getString("title"),
                        movieJson.getString("overview"),
                        movieJson.getString("poster_path"), // relative URL, not a full URL
                    )
                )
            }
            return movies
        }
    }
}