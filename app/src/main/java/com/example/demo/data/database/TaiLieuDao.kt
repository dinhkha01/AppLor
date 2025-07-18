package com.example.demo.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.demo.data.model.TaiLieu

@Dao
interface TaiLieuDao {
    @Query("SELECT * FROM tai_lieu ORDER BY tenTaiLieu ASC")
    fun getAllTaiLieu(): Flow<List<TaiLieu>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaiLieu(taiLieu: TaiLieu)

    @Update
    suspend fun updateTaiLieu(taiLieu: TaiLieu)

    @Delete
    suspend fun deleteTaiLieu(taiLieu: TaiLieu)
}