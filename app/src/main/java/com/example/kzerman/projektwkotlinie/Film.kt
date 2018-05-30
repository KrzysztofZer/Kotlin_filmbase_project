package com.example.kzerman.projektwkotlinie

import android.content.Context
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
import org.jetbrains.anko.contentView
import android.widget.Toast
import android.widget.RatingBar




class Film : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)
        initialization()
        Zapisz.setOnClickListener { saveToFavorites() }
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)

        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, v, b ->
            changeRating()
        }
    }
    var poster = ""

    fun initialization(){
        val extra: Bundle = intent.extras
        Log.d("Moj log", "pobrana wartosc: "+ extra.getString("id"))
        // You have to input your apiKey here
        if (!android.text.TextUtils.isDigitsOnly(extra.getString("id"))){
            val db = DataBaseHandler(baseContext)
            val list = db.readMovie(extra.getString("id"))

            textTytul.text = list[0]
            textOpis.text = list[1]
            textGatunek.text = list[2]
            textOcena.text = list[3]
            textGlosy.text = list[4]
            var rateForFilm = list[5].toFloat()
            ratingBar.rating = rateForFilm
            textData.text = list[6]
            textIMDB.setOnClickListener(View.OnClickListener {
                val internet = Intent (Intent.ACTION_VIEW,Uri.parse("http://www.imdb.com/title/%s%c".format(list[7],'/')))
                startActivity(internet)
            })
            this.poster=list[8]
            Glide.with(applicationContext).load(this.poster).into(imageView)
            if (list[9] == "0"){
                toggleButton.isChecked = false
            }
            else{
                toggleButton.isChecked = true
            }



        }

        else{
            val url = "https://api.themoviedb.org/3/movie/"+extra.getString("id").toString()+"?api_key=%s".format(getString(R.string.apiID))
            val response = URL (url).readText()
            Log.d("Moj log", "response "+ response)

            val  jsonn = JSONObject(response)
            this.poster = "https://image.tmdb.org/t/p/w300%s".format(jsonn.getString("poster_path"))
            Log.d("Moj log", "plakat %s".format(this.poster))
            Glide.with(applicationContext).load(this.poster).into(imageView)

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

        }


        )

        toggleButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            var db = DataBaseHandler(baseContext)
            if (isChecked) {
                textChceZobaczyc.text = "Kliknij by usunac z \"chce zobaczyc\""
                contentView?.let { db.updateWantToWatch(it,poster,1) }
            } else {
                textChceZobaczyc.text = "Kliknij by dodac do \"chce zobaczyc\""
                contentView?.let { db.updateWantToWatch(it,poster,0) }

            }
        })

    }}

    fun saveToFavorites()
    {
        var db = DataBaseHandler(baseContext)
        contentView?.let { db.insertData(it,poster,0, textTytul.text.toString()) }
    }

    fun changeRating(){
        var db = DataBaseHandler(baseContext)
        if (toggleButton.isChecked){
            contentView?.let { db.updateRating(it, poster,1 ) }
        }

        else{
            contentView?.let { db.updateRating(it, poster,0 )}
        }
    }
}

