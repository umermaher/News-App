package com.example.newsapp2.db

import android.content.Context
import androidx.room.*
import androidx.room.Room.databaseBuilder
import com.example.newsapp2.models.Article


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Article::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticleRoomDatabase :RoomDatabase(){
    abstract fun getArticleDao():ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleRoomDatabase? = null
        private val Lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(Lock) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext, ArticleRoomDatabase::class.java, "article_database.db"
            ).fallbackToDestructiveMigration().build()
    }
}