package com.example.kzerman.projektwkotlinie

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_film.*
import org.json.JSONObject
import java.net.URL
import android.widget.CompoundButton




class Film : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)
        initialization()
    }
    fun initialization(){
        val extra: Bundle = intent.extras
        Log.d("Moj log", "pobrana wartosc: "+ extra.getInt("id"))
        // You have to input your apiKey here
        val url = "https://api.themoviedb.org/3/movie/"+extra.getInt("id").toString()+"?api_key=%s".format(getString(R.string.apiID))
        val response = URL (url).readText()
        Log.d("Moj log", "response "+ response)

        val  jsonn = JSONObject(response)
        val poster = "https://image.tmdb.org/t/p/w300%s".format(jsonn.getString("poster_path"))
        Log.d("Moj log", "plakat %s".format(poster))
        Glide.with(applicationContext).load(poster).into(imageView)

        textTytul.text = jsonn.getString("title")
        textTytul.movementMethod = ScrollingMovementMethod()
        textData.text = "Premiera: %s".format(jsonn.getString("release_date"))
        textOpis.text = jsonn.getString("overview")
        textGatunek.text = "Gatunek: %s".format(jsonn.getJSONArray("genres").getJSONObject(0).getString("name"))
        textOcena.setTextColor(Color.GREEN)
        textOcena.text = "Ocena: %s".format(jsonn.get("vote_average").toString())
        textGlosy.setTextColor(Color.GREEN)
        textGlosy.text = "Liczba glosow: %s".format(jsonn.get("vote_count").toString())
        textIMDB.setTextColor(Color.RED)
        textIMDB.setOnClickListener(View.OnClickListener {
            val internet = Intent (Intent.ACTION_VIEW,Uri.parse("http://www.imdb.com/title/%s%c".format(jsonn.getString("imdb_id"),'/')))
            startActivity(internet)
        })

        toggleButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                textChceZobaczyc.text = "Kliknij by usunac z \"chce zpbaczyc\""
            } else {
                textChceZobaczyc.text = "Kliknij by dodac do \"chce zpbaczyc\""
            }
        })

    }
}
