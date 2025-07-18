package com.example.demo.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.demo.data.model.Sach

@Dao
interface SachDao {
    @Query("SELECT * FROM sach ORDER BY tenSach ASC")
    fun getAllSach(): Flow<List<Sach>>

    @Query("SELECT * FROM sach WHERE phanLoaiId = :phanLoaiId")
    fun getSachByPhanLoai(phanLoaiId: Int): Flow<List<Sach>>

    @Query("SELECT * FROM sach WHERE namXuatBan = :nam")
    fun getSachByNam(nam: Int): Flow<List<Sach>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSach(sach: Sach)

    @Update
    suspend fun updateSach(sach: Sach)

    @Delete
    suspend fun deleteSach(sach: Sach)
}


