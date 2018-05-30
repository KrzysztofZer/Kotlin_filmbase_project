package com.example.kzerman.projektwkotlinie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_fevorite.*
import kotlinx.android.synthetic.main.activity_want_to_watch.*
import org.jetbrains.anko.contentView

class WantToWatch : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_want_to_watch)
        val db = DataBaseHandler(baseContext)
        val lista = contentView?.let { db.readWantToWatch() }
        val myAdapter = lista?.let { MyAdapter(it, this, R.layout.item_cell) }
        MyR_wantWatch.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        MyR_wantWatch.adapter = myAdapter
    }
}
