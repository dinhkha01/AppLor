package com.example.demo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phan_loai")
data class PhanLoai(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val tenPhanLoai: String,
    val moTa: String? = null
)