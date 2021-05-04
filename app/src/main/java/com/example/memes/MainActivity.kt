package com.example.memes

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var currentMemeURL : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

   private fun loadMeme(){
//        val queue = Volley.newRequestQueue(this)  <<--- hiding this volley instance bcz we are creating a singleton pattern
        progressBar.visibility = View.VISIBLE
       val url = "https://meme-api.herokuapp.com/gimme"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    currentMemeURL = response.getString("url")
                    Glide.with(this).load(currentMemeURL).listener(object: RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            next.isEnabled = true
                            share.isEnabled = true
                            return  false
                        }

                    } ).into(imageView)
                },
                { error ->
                    Toast.makeText(this,"Something went wrong brother",Toast.LENGTH_LONG).show()
                }
        )

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

//        queue.add(jsonObjectRequest) <<-- volley instance :: singleton pattern

    }
    fun nextMeme(view: View) {
        loadMeme()
    }
    fun shareMeme(view: View) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, "wahh bette kattai moj krdi...dek le tu bi iss meme ko $currentMemeURL")
        val chooser = Intent.createChooser(share,"title to rhne he de yr tu bs")
        startActivity(chooser)
    }
}