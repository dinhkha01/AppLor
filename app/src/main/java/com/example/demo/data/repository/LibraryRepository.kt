package com.example.demo.data.repository

import com.example.demo.data.database.AppDatabase
import com.example.demo.data.model.Sach
import com.example.demo.data.model.TaiLieu
import com.example.demo.data.model.PhanLoai
import kotlinx.coroutines.flow.Flow

class LibraryRepository(private val database: AppDatabase) {

    // Sách operations
    fun getAllSach(): Flow<List<Sach>> = database.sachDao().getAllSach()
    fun getSachByPhanLoai(phanLoaiId: Int): Flow<List<Sach>> = database.sachDao().getSachByPhanLoai(phanLoaiId)
    fun getSachByNam(nam: Int): Flow<List<Sach>> = database.sachDao().getSachByNam(nam)
    suspend fun insertSach(sach: Sach) = database.sachDao().insertSach(sach)
    suspend fun updateSach(sach: Sach) = database.sachDao().updateSach(sach)
    suspend fun deleteSach(sach: Sach) = database.sachDao().deleteSach(sach)

    // Phân loại operations
    fun getAllPhanLoai(): Flow<List<PhanLoai>> = database.phanLoaiDao().getAllPhanLoai()
    suspend fun insertPhanLoai(phanLoai: PhanLoai) = database.phanLoaiDao().insertPhanLoai(phanLoai)
    suspend fun updatePhanLoai(phanLoai: PhanLoai) = database.phanLoaiDao().updatePhanLoai(phanLoai)
    suspend fun deletePhanLoai(phanLoai: PhanLoai) = database.phanLoaiDao().deletePhanLoai(phanLoai)

    // Tài liệu operations
    fun getAllTaiLieu(): Flow<List<TaiLieu>> = database.taiLieuDao().getAllTaiLieu()
    suspend fun insertTaiLieu(taiLieu: TaiLieu) = database.taiLieuDao().insertTaiLieu(taiLieu)
    suspend fun updateTaiLieu(taiLieu: TaiLieu) = database.taiLieuDao().updateTaiLieu(taiLieu)
    suspend fun deleteTaiLieu(taiLieu: TaiLieu) = database.taiLieuDao().deleteTaiLieu(taiLieu)
}
