package com.example.kzerman.projektwkotlinie


import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cell.view.*
import org.jetbrains.anko.contentView


internal class MyAdapter (private var arrayList: ArrayList<MyItem>, private val context: Context, private val layout: Int): RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        holder.bindItems(arrayList[position])
holder.itemView.Sczegoly.setOnClickListener(View.OnClickListener {
  val intent = Intent(context,Film::class.java)
    Log.d("Moj log","ID filmu do wyslania %s".format(arrayList[position].id.toString()))
    if (arrayList[position].id!=-1)
        intent.putExtra("id",arrayList[position].id.toString())
    else
        intent.putExtra("id",arrayList[position].nazwaFilmu)
   context.startActivity(intent)
})
        holder.itemView.okladki.setOnLongClickListener {
            val db = DataBaseHandler(context)
        db.deleteMovie(holder.itemView.TytulProd.text.toString())
            arrayList.clear()
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val task = manager.getAppTasks()
            val componentInfo = task[0].taskInfo.topActivity
            if (componentInfo.getClassName().equals(Favorite::class.java.name)){
                val db = DataBaseHandler(context)
                arrayList = db.readWatched()
            }
            else if (componentInfo.getClassName().equals(AllSaved::class.java.name)){
                val db = DataBaseHandler(context)
                arrayList = db.readAll()
            }
            else{
                val db = DataBaseHandler(context)
                arrayList = db.readWantToWatch()
            }

            notifyDataSetChanged()

            true
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(items: MyItem) {
            itemView.TytulProd.text = items.nazwaFilmu
            itemView.DataProd.text = items.dataProdukcji
            itemView.idFilmu.text = items.id.toString()
            Glide.with(context).load(items.linkDoZdjecia).into(itemView.okladki)
        }}}













