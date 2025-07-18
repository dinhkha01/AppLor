package com.example.demo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "sach",
    foreignKeys = [
        ForeignKey(
            entity = PhanLoai::class,
            parentColumns = ["id"],
            childColumns = ["phanLoaiId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("phanLoaiId")]
)
data class Sach(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tenSach: String,
    val tacGia: String,
    val namXuatBan: Int,
    val soTrang: Int,
    val phanLoaiId: Int,
    val trangThai: String = "Có sẵn" // Có sẵn, Đã mượn, Bảo trì
)