package com.zzz.auvapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//@Database(version = 1, entities = [Person::class])
// 增加book表，进行数据库升级
@Database(version = 2, entities = [Person::class, Book::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun personDao() : PersonDao

    abstract fun bookDao(): BookDao

    companion object {
        private var instance: AppDatabase? = null

        // 升级
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("create table Book(id integer primary key autoincrement not null, name text not null, page integer not null)")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            instance?.let { return it }
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
                .addMigrations(MIGRATION_1_2)
                .build().apply { instance = this }
        }
    }
}