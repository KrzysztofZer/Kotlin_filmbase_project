package com.example.kzerman.projektwkotlinie


import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_strona_glowna.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.net.URL
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import com.example.kzerman.projektwkotlinie.R.id.editText




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

        fun afterClick(){
            // Hide keyboard
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Log.d("Moj log", "po dodaniu uprawnien")
            // You have to input your apiKey here
            val apiKey = ""

            val url = "https://api.themoviedb.org/3/search/movie?api_key=%s&query=%s".format(getString(R.string.apiID), editText.text.toString().replace(' ', '+'))
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
        // Clear edit text after tap on it
        editText.setOnClickListener{
            editText.setText("")
        }
        editText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(view: View, keyCode: Int, keyevent: KeyEvent): Boolean {
                //If the keyevent is a key-down event on the "enter" button
                return if (keyevent.getAction() === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                   afterClick()
                    true
                } else false
            }
        })

        button.setOnClickListener{
            afterClick()
        }



    }

    // Class for adapter
    internal class MyItem(val nazwaFilmu: String, val dataProdukcji: String, val linkDoZdjecia: String, val id: Int)


}