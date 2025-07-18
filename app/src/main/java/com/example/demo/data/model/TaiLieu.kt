package com.example.demo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tai_lieu")
data class TaiLieu(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tenTaiLieu: String,
    val loaiTaiLieu: String, // Sách, Tạp chí, Báo, CD/DVD
    val nguonGoc: String,
    val ngayThem: String
)