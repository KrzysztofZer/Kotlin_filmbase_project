package com.example.kzerman.projektwkotlinie

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide


import kotlinx.android.synthetic.main.item_cell.view.*




internal class MyAdapter (private val arrayList: ArrayList<StronaGlowna.MyItem>, private val context: Context, private val layout: Int): RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
holder.itemView.Sczegoly.setOnClickListener(View.OnClickListener {
  val intent = Intent(context,Film::class.java)
    Log.d("Moj log","ID filmu do wyslania "+arrayList[position].id.toString())
    intent.putExtra("id",arrayList[position].id)
   context.startActivity(intent)
})
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(items: StronaGlowna.MyItem) {
            itemView.TytulProd.text = items.nazwaFilmu
            itemView.DataProd.text = items.dataProdukcji
            itemView.idFilmu.text = items.id.toString()
            Glide.with(context).load(items.linkDoZdjecia).into(itemView.okladki)
        }}}












