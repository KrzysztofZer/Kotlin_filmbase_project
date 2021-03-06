package com.example.kzerman.projektwkotlinie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_all_saved.*
import kotlinx.android.synthetic.main.activity_fevorite.*
import org.jetbrains.anko.contentView

class AllSaved : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_saved)
        val db = DataBaseHandler(baseContext)
        val lista = contentView?.let { db.readAll() }
        val myAdapter = lista?.let { MyAdapter(it, this, R.layout.item_cell) }
        MyR_All.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        MyR_All.adapter = myAdapter
    }
}
