package com.example.newsapp2.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp2.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun upsert(article: Article):Long

    @Delete
     fun delete(article: Article)

    @Query("Select * from articles_table")
    fun getAllArticles():LiveData<List<Article>>
}