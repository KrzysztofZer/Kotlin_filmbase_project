package com.example.kzerman.projektwkotlinie

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.TypedValue
import android.widget.LinearLayout


import kotlinx.android.synthetic.main.activity_strona_glowna.*
import org.json.JSONObject
import java.net.URL
import android.os.StrictMode
import android.support.annotation.RequiresApi
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast


class StronaGlowna : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strona_glowna)
        initialization()
    }


    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun initialization() {
        Tytul.text = "Wyszukaj film"
        Tytul.setTextColor(Color.RED)
        Tytul.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24F)





        button.setOnClickListener {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Log.d("Moj log", "po dodaniu uprawnien")
            // You have to input your apiKey here
            val apiKey = ""

            val url = "https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s".format(apiKey, editText.text.toString().replace(' ', '+'))
            Log.d("Moj log", url)

            val response = URL(url).readText()
            Log.d("Moj log", response)

            val lista = ArrayList<MyItem>()
            val jobiekt = JSONObject(response)

            var ilosc: Int = jobiekt.getInt("total_results")
            //To many movies
            if (ilosc > 19) {
                ilosc = 19
            }
            // there is no data
            if (ilosc < 1) {
                alert("There is no any results for %s :(".format(editText.text.toString())) {
                    yesButton { toast("OK") }
                }.show()
            }
            // Adding objects to list
            for (x in 0 until ilosc)
            {
                lista.add(MyItem(jobiekt.getJSONArray("results").getJSONObject(x).getString("title"), jobiekt.getJSONArray("results").getJSONObject(x).getString("release_date"), "https://image.tmdb.org/t/p/w300" + jobiekt.getJSONArray("results").getJSONObject(x).getString("poster_path"), jobiekt.getJSONArray("results").getJSONObject(x).getInt("id")))
                val myAdapter = MyAdapter(lista, this, R.layout.item_cell)
                MyR.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
                MyR.adapter = myAdapter

            }

        }


    }


    internal class MyItem(val nazwaFilmu: String, val dataProdukcji: String, val linkDoZdjecia: String, val id: Int)


}