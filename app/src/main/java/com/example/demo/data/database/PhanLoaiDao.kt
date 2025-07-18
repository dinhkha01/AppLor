package com.example.demo.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.demo.data.model.PhanLoai

@Dao
interface PhanLoaiDao {
    @Query("SELECT * FROM phan_loai ORDER BY tenPhanLoai ASC")
    fun getAllPhanLoai(): Flow<List<PhanLoai>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhanLoai(phanLoai: PhanLoai)

    @Update
    suspend fun updatePhanLoai(phanLoai: PhanLoai)

    @Delete
    suspend fun deletePhanLoai(phanLoai: PhanLoai)
}