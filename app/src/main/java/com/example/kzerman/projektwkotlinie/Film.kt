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


class Film : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)
        initialization()
        Zapisz.setOnClickListener { saveToFavorites() }
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
            textData.text = list[5]
            textIMDB.setOnClickListener(View.OnClickListener {
                val internet = Intent (Intent.ACTION_VIEW,Uri.parse("http://www.imdb.com/title/%s%c".format(list[6],'/')))
                startActivity(internet)
            })
            this.poster=list[7]
            Glide.with(applicationContext).load(this.poster).into(imageView)
            if (list[8] == "0"){
//            TODO
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
            if (isChecked) {
                textChceZobaczyc.text = "Kliknij by usunac z \"chce zobaczyc\""
                val mydatabase = openOrCreateDatabase("Favorites", Context.MODE_PRIVATE, null)
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS favorites(Title VARCHAR PRIMARY KEY, Description VARCHAR, Type VARCHAR, Rating VARCHAR, Votes VARCHAR, Premier VARCHAR, Imdb VARCHAR, Img VARCHAR, WantSee TINYINT);")
                mydatabase.execSQL("INSERT INTO Favorites VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%d')ON DUPLICATE KEY UPDATE;".format(textTytul.text, textOpis.text, textGatunek.text, textOcena.text, textGlosy.text, textData.text, textIMDB.text, this.poster, 1))

            } else {
                textChceZobaczyc.text = "Kliknij by dodac do \"chce zobaczyc\""
            }
        })

    }}

    fun saveToFavorites()
    {
//        val mydatabase = openOrCreateDatabase("Favorites", Context.MODE_PRIVATE, null)
//        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS favorites(Title VARCHAR PRIMARY KEY, Description VARCHAR, Type VARCHAR, Rating VARCHAR, Votes VARCHAR, Premier VARCHAR, Imdb VARCHAR, Img VARCHAR, WantSee TINYINT);")
//        if (!toggleButton.isChecked)
//        {
//            mydatabase.execSQL("INSERT INTO Favorites (Title, Description, Type, Rating, Votes, Premier, Imdb, Img, WantSee) VALUES('%s','%s','%s','%s','%s','%s','%s','%s',%d) ON DUPLICATE KEY UPDATE WantSee = VALUES(WantSee);".format(textTytul.text.toString().replace("'"," "), textOpis.text.toString().replace("'"," "), textGatunek.text.toString(), textOcena.text.toString(), textGlosy.text.toString(), textData.text.toString(), textIMDB.text.toString(), this.poster, 0))
//        }
//        val resultSet = mydatabase.rawQuery("Select * from Favorites", null)
//        resultSet.moveToFirst()
//        toast(resultSet.getString(0))
//        resultSet.moveToFirst()
        var db = DataBaseHandler(baseContext)
        contentView?.let { db.insertData(it,poster,0) }
    }}

