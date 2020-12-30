package com.zzz.auvapp.room

import androidx.room.*
import com.zzz.auvapp.room.Person

@Dao
interface PersonDao {

    @Insert
    fun insertUser(person: Person): Long

    @Update
    fun updateUser(person: Person)

    @Query("SELECT * FROM user_table")
    fun queryUser(): List<Person>

    @Query("SELECT * FROM user_table WHERE age > :age")
    fun queryUserOlderThan(age: Int): List<Person>

    @Delete
    fun deleteUser(person: Person)

    @Query("DELETE FROM user_table WHERE lastName = :lastName")
    fun deleteUserByLastName(lastName: String): Int
}