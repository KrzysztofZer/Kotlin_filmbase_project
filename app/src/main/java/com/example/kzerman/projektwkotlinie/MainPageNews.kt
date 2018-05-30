package com.example.kzerman.projektwkotlinie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.StrictMode
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main_page_news.*
import kotlinx.android.synthetic.main.activity_strona_glowna.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.net.URL


class MainPageNews : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page_news)
        setNews()
        SearchButton.setOnClickListener{goToSearchActivity()}
        FavoriteButton.setOnClickListener { goToFavorite() }
        WantWatchButton.setOnClickListener { goToWantToWatch() }
        buttonAll.setOnClickListener { goToAll() }

    }

    fun goToSearchActivity()
    {
        val intent = Intent(this,StronaGlowna::class.java)
        startActivity(intent)
    }
    fun goToFavorite()
    {
        val intent = Intent(this,Favorite::class.java)
        startActivity(intent)
    }
    fun goToWantToWatch()
    {
        val intent = Intent(this,WantToWatch::class.java)
        startActivity(intent)
    }
    fun goToAll(){
        val intent = Intent(this,AllSaved::class.java)
        startActivity(intent)
    }

    fun setNews()
    {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Log.d("Moj log", "po dodaniu uprawnien")
        // You have to input your apiKey here

        val url = "https://newsapi.org/v2/everything?domains=filmweb.pl&apiKey=%s".format(getString(R.string.newsesApiID))
        Log.d("Moj log", url)

        val response = URL(url).readText()
        Log.d("Moj log", response)

        val listaNews = ArrayList<MainPageNews.News>()
        val jobiekt = JSONObject(response)

        var ilosc: Int = jobiekt.getInt("totalResults")
        //To many movies
        if (ilosc > 10) {
            ilosc = 10
        }
        // there is no data
        if (ilosc < 1) {
            alert("There is no newses") {
                yesButton { toast("OK") }
            }.show()
        }
        // Adding objects to list
        for (x in 0 until ilosc)
        {
            listaNews.add(MainPageNews.News(newsTitle = jobiekt.getJSONArray("articles").getJSONObject(x).getString("title"),newsDescrition = jobiekt.getJSONArray("articles").getJSONObject(x).getString("description"),author= jobiekt.getJSONArray("articles").getJSONObject(x).getString("author"), linkDoZdjecia = jobiekt.getJSONArray("articles").getJSONObject(x).getString("urlToImage"), filmwebURL = jobiekt.getJSONArray("articles").getJSONObject(x).getString("url"), id = x))
            val NewsAdapter = NewsesAdapter(listaNews, this, R.layout.news_cell)
            newsR.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            newsR.adapter = NewsAdapter

        }
    }
    class News(val newsTitle: String, val newsDescrition: String, val author: String, val linkDoZdjecia: String, val filmwebURL: String, val id: Int)
}

