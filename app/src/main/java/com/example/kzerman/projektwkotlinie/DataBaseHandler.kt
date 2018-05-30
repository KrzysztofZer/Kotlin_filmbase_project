package com.example.kzerman.projektwkotlinie

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import java.lang.reflect.Type
import kotlin.reflect.jvm.internal.impl.utils.StringsKt

val DATABASE_NAME = "SavedMovies"
val TABLE_NAME = "Saved"

val COL_TITLE = "Title"
val COL_DESCRIPTION = "Description"
val COL_TYPE = "Type"
val COL_RATING = "Rating"
val COL_USER_RATE = "UserRate"
val COL_VOTES = "Votes"
val COL_PREMIERE = "Premiere"
val COL_IMDB = "Imdb"
val COL_IMG = "IMG"
val COL_WANT_SEE = "WantSee"
val SQL_STRING = "VARCHAR (256)"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1) {
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {


    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable = "CREATE TABLE %s (%s %s, %s %s, %s %s, %s %s, %s %s, %s %s, %s %s, %s %s, %s %s, %s %s)".format(TABLE_NAME,
                COL_TITLE, SQL_STRING,
                COL_DESCRIPTION, SQL_STRING,
                COL_TYPE, SQL_STRING,
                COL_RATING, SQL_STRING,
                COL_VOTES, SQL_STRING,
                COL_USER_RATE, SQL_STRING,
                COL_PREMIERE, SQL_STRING,
                COL_IMDB, SQL_STRING,
                COL_IMG, SQL_STRING,
                COL_WANT_SEE, "INTEGER")

        p0?.execSQL(createTable)
    }

    @SuppressLint("WrongConstant", "ShowToast")
    fun insertData(view: View, poster: String, want_see: Int, title: String)
    {

        val db = this.writableDatabase

        val query = "SELECT * from %s WHERE %s like '%s'".format(TABLE_NAME, COL_TITLE, title)
        val testResult = db.rawQuery(query, null)

        if(testResult.count == 0){
            testResult.close()
            val cv = ContentValues()
            cv.put(COL_TITLE, view.findViewById<TextView>(R.id.textTytul).text.toString())
            cv.put(COL_DESCRIPTION, view.findViewById<TextView>(R.id.textOpis).text.toString())
            cv.put(COL_TYPE, view.findViewById<TextView>(R.id.textGatunek).text.toString())
            cv.put(COL_RATING, view.findViewById<TextView>(R.id.textOcena).text.toString())
            cv.put(COL_USER_RATE, view.findViewById<RatingBar>(R.id.ratingBar).rating.toString())
            cv.put(COL_VOTES, view.findViewById<TextView>(R.id.textGlosy).text.toString())
            cv.put(COL_PREMIERE, view.findViewById<TextView>(R.id.textData).text.toString())
            cv.put(COL_IMDB, view.findViewById<TextView>(R.id.textIMDB).text.toString())
            cv.put(COL_IMG, poster)
            cv.put(COL_WANT_SEE, want_see)

            val result = db.insert(TABLE_NAME, null, cv)
            if (result == (-1).toLong())
            {
                Log.d("Moj log","Baza sie nie stworzyla")
            }
            else
            {
                Log.d("Moj log","Baza sie stworzyla")
            }
        }
        else{
            testResult.close()
            Toast.makeText(context, "Database exist already",5000 )
        }


    }

    fun readAll(): ArrayList<MyItem> {
        val db = this.writableDatabase
        val query = "SELECT * from %s".format(TABLE_NAME)
        val result = db.rawQuery(query, null)
        val lista = ArrayList<MyItem>()
        if (result.moveToFirst()){
            do {
                var title = result.getString(result.getColumnIndex(COL_TITLE)).toString()
                var premiere = result.getString(result.getColumnIndex(COL_PREMIERE)).toString()
                var photoLink= result.getString(result.getColumnIndex(COL_IMG)).toString()

                var tempObject = MyItem(title,premiere,photoLink,-1)

                lista.add(tempObject)

            }while (result.moveToNext())
        }
        db.close()
        result.close()
        return lista
    }

    fun readMovie(movieName: String): ArrayList<String> {
        val db = this.writableDatabase
        val query = "SELECT * from %s WHERE %s like '%s'".format(TABLE_NAME, COL_TITLE, movieName)
        val result = db.rawQuery(query, null)
        val lista = ArrayList<String>()
        if (result.moveToFirst()){


            val title = result.getString(result.getColumnIndex(COL_TITLE)).toString()
            lista.add(title)
            val description = result.getString(result.getColumnIndex(COL_DESCRIPTION)).toString()
            lista.add(description)
            val type = result.getString(result.getColumnIndex(COL_TYPE)).toString()
            lista.add(type)
            val rating = result.getString(result.getColumnIndex(COL_RATING)).toString()
            lista.add(rating)
            val votes = result.getString(result.getColumnIndex(COL_VOTES)).toString()
            lista.add(votes)
            val userRating = result.getString(result.getColumnIndex(COL_USER_RATE)).toString()
            lista.add(userRating)
            val premiere = result.getString(result.getColumnIndex(COL_PREMIERE)).toString()
            lista.add(premiere)
            val imdb = result.getString(result.getColumnIndex(COL_IMDB)).toString()
            lista.add(imdb)
            val img = result.getString(result.getColumnIndex(COL_IMG)).toString()
            lista.add(img)
            val want_see = result.getString(result.getColumnIndex(COL_WANT_SEE)).toString()
            lista.add(want_see)


        }
        db.close()
        result.close()
        return lista
    }

    fun readWantToWatch(): ArrayList<MyItem> {
        val db = this.writableDatabase
        val query = "SELECT * from %s WHERE %s = '%s'".format(TABLE_NAME, COL_WANT_SEE, "1")
        val result = db.rawQuery(query, null)
        val lista = ArrayList<MyItem>()
        if (result.moveToFirst()){
            do {
                var title = result.getString(result.getColumnIndex(COL_TITLE)).toString()
                var premiere = result.getString(result.getColumnIndex(COL_PREMIERE)).toString()
                var photoLink= result.getString(result.getColumnIndex(COL_IMG)).toString()

                var tempObject = MyItem(title,premiere,photoLink,-1)

                lista.add(tempObject)

            }while (result.moveToNext())
        }
        db.close()
        result.close()
        return lista
    }

    fun readWatched(): ArrayList<MyItem> {
        val db = this.writableDatabase
        val query = "SELECT * from %s WHERE %s = '%s'".format(TABLE_NAME, COL_WANT_SEE, "0")
        val result = db.rawQuery(query, null)
        val lista = ArrayList<MyItem>()
        if (result.moveToFirst()){
            do {
                var title = result.getString(result.getColumnIndex(COL_TITLE)).toString()
                var premiere = result.getString(result.getColumnIndex(COL_PREMIERE)).toString()
                var photoLink= result.getString(result.getColumnIndex(COL_IMG)).toString()

                var tempObject = MyItem(title,premiere,photoLink,-1)

                lista.add(tempObject)

            }while (result.moveToNext())
        }
        db.close()
        result.close()
        return lista
    }

    fun deleteMovie(name: String){
        val db = this.writableDatabase
        val where = "%s = '%s'".format(COL_TITLE,name)
        db.delete(TABLE_NAME,where,null)

    }

    fun updateRating(view: View, poster: String, want_see: Int){
        val db = this.writableDatabase
        var query = "SELECT * from %s WHERE %s like '%s'".format(TABLE_NAME, COL_TITLE, view.findViewById<TextView>(R.id.textTytul).text.toString())
        val testResult = db.rawQuery(query, null)
        if(testResult.count != 0){
            val cv = ContentValues()
            cv.put(COL_USER_RATE, view.findViewById<RatingBar>(R.id.ratingBar).rating.toString())
            val where = "%s = '%s'".format(COL_TITLE, view.findViewById<TextView>(R.id.textTytul).text.toString())

            db.update(TABLE_NAME, cv, where, null)
        }
        else{
            insertData(view,poster,want_see,view.findViewById<TextView>(R.id.textTytul).text.toString())
        }

        db.close()
    }
    fun updateWantToWatch(view: View, poster: String, want_see: Int){
        val db = this.writableDatabase
        var query = "SELECT * from %s WHERE %s like '%s'".format(TABLE_NAME, COL_TITLE, view.findViewById<TextView>(R.id.textTytul).text.toString())
        val testResult = db.rawQuery(query, null)
        if(testResult.count != 0){
            val cv = ContentValues()
            cv.put(COL_WANT_SEE, want_see.toString())
            val where = "%s = '%s'".format(COL_TITLE, view.findViewById<TextView>(R.id.textTytul).text.toString())

            db.update(TABLE_NAME, cv, where, null)
        }
        else{
            insertData(view,poster,want_see,view.findViewById<TextView>(R.id.textTytul).text.toString())
        }

        db.close()
    }
}


