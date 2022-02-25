package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "MainActivity"
private const val NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
// constant

class MainActivity : AppCompatActivity() {

    private val movies = mutableListOf<Movie>() // will add newly parsed movies into this movies list from val movieJsonArray
    private lateinit var rvMovies: RecyclerView

    /*
    1. Define a data model class as the data source - DONE
                **Movie data class
    2. Add the RecyclerView to the layout - DONE
                ** layout: activity_main 0dp for layout width, height (match the constraints to take up the whole width and height of the UI, and margin constraints to
    3. Create a custom row layout XML file to visualize the item - DONE
                ** res > layout > item_move.xml (represents one row of the recyclerview)
    4. Create an Adapter and ViewHolder to render the item - DONE
                ** Define Adapter in separate file and get reference to it here as val movieAdapter
    5. Bind the adapter to the data source to populate the RecyclerView - DONE
                ** get reference to RecyclerView in MainActivity
    6. Bind a layout manager to the RecyclerView - DONE
                **
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMovies = findViewById(R.id.rvMovies) // reference

        val movieAdapter = MovieAdapter(this, movies) // (context (MainActivity), list of movies)
        rvMovies.adapter = movieAdapter
        rvMovies.layoutManager = LinearLayoutManager(this) // puts views vertical from top to bottom

        // new object to HTTP client
        val client = AsyncHttpClient()
        client.get(NOW_PLAYING_URL, object: JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode") // Log error(class name, print status code)
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) { // json: JSON? is nullable? no, we know its not null
                Log.i(TAG, "onSuccess: JSON data $json") // Log info level(class name, print json data)
                val moveJsonArray = json.jsonObject.getJSONArray("results") // inside the JSON file, results is stored inside an object and contains an array
                movieAdapter.notifyDataSetChanged()
                // translation process of JSON to a movie
                // delegate work into data class Movie
                try {
                    movies.addAll(Movie.fromJsonArray(moveJsonArray)) // might throw JSON exception
                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e")
                }

                Log.i(TAG, "Movie list $movies")
            }

        }) //url, response handler (anonymous class),
    }
}