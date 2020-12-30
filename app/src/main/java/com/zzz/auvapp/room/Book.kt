package com.zzz.auvapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(var name: String, var page: Int) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
