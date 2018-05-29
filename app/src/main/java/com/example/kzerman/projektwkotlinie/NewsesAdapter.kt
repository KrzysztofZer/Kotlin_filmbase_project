package com.example.kzerman.projektwkotlinie

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cell.view.*
import kotlinx.android.synthetic.main.news_cell.view.*


internal class NewsesAdapter (private val arrayList: ArrayList<MainPageNews.News>, private val context: Context, private val layout: Int): RecyclerView.Adapter<NewsesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsesAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewsesAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
        holder.itemView.newsCell.setOnClickListener(View.OnClickListener {
            val internet = Intent (Intent.ACTION_VIEW, Uri.parse(arrayList[position].filmwebURL))
            context.startActivity(internet)
//            val intent = Intent(context,Film::class.java)
//            Log.d("Moj log","ID filmu do wyslania %s".format(arrayList[position].id.toString()))
//            intent.putExtra("id",arrayList[position].id)
//            context.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(items: MainPageNews.News) {
            itemView.TytulNews.text = items.newsTitle
            itemView.OpisNews.text = items.newsDescrition
            itemView.AutorNews.text = items.author
            Glide.with(context).load(items.linkDoZdjecia).into(itemView.ZdjecieNews)
        }}}