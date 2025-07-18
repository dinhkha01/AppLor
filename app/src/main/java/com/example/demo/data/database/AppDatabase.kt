package com.example.demo.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.demo.data.model.Sach
import com.example.demo.data.model.TaiLieu
import com.example.demo.data.model.PhanLoai

@Database(
    entities = [Sach::class, TaiLieu::class, PhanLoai::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sachDao(): SachDao
    abstract fun taiLieuDao(): TaiLieuDao
    abstract fun phanLoaiDao(): PhanLoaiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "library_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}