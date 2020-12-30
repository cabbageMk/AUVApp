package com.zzz.auvapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zzz.auvapp.room.Book

@Dao
interface BookDao {

    @Insert
    fun insertBook(book: Book): Long

    @Query("select * from Book")
    fun queryBooks(): List<Book>
}