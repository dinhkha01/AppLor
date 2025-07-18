package com.example.demo
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quanlytailieu.db"
        private const val DATABASE_VERSION = 1

        // Bảng Sách_Tài liệu
        const val TABLE_SACH = "Sach_Tailieu"
        const val COLUMN_SACH_ID = "id"
        const val COLUMN_SACH_TEN = "ten_sach"
        const val COLUMN_SACH_TAC_GIA = "tac_gia"
        const val COLUMN_SACH_NAM_XB = "nam_xuat_ban"
        const val COLUMN_SACH_MO_TA = "mo_ta"
        const val COLUMN_SACH_NGAY_NHAP = "ngay_nhap"

        // Bảng Phân loại
        const val TABLE_PHAN_LOAI = "PhanLoai"
        const val COLUMN_PHAN_LOAI_ID = "id"
        const val COLUMN_PHAN_LOAI_TEN = "ten_phan_loai"
        const val COLUMN_PHAN_LOAI_MO_TA = "mo_ta"

        // Bảng Phân loại Sách
        const val TABLE_PHAN_LOAI_SACH = "PhanLoaiSach"
        const val COLUMN_PLS_ID = "id"
        const val COLUMN_PLS_SACH_ID = "sach_id"
        const val COLUMN_PLS_PHAN_LOAI_ID = "phan_loai_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tạo bảng Sách
        val createSachTable = """
            CREATE TABLE $TABLE_SACH (
                $COLUMN_SACH_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SACH_TEN TEXT NOT NULL,
                $COLUMN_SACH_TAC_GIA TEXT NOT NULL,
                $COLUMN_SACH_NAM_XB INTEGER NOT NULL,
                $COLUMN_SACH_MO_TA TEXT,
                $COLUMN_SACH_NGAY_NHAP TEXT NOT NULL
            )
        """.trimIndent()

        // Tạo bảng Phân loại
        val createPhanLoaiTable = """
            CREATE TABLE $TABLE_PHAN_LOAI (
                $COLUMN_PHAN_LOAI_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PHAN_LOAI_TEN TEXT NOT NULL,
                $COLUMN_PHAN_LOAI_MO_TA TEXT
            )
        """.trimIndent()

        // Tạo bảng Phân loại Sách
        val createPhanLoaiSachTable = """
            CREATE TABLE $TABLE_PHAN_LOAI_SACH (
                $COLUMN_PLS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PLS_SACH_ID INTEGER NOT NULL,
                $COLUMN_PLS_PHAN_LOAI_ID INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_PLS_SACH_ID) REFERENCES $TABLE_SACH($COLUMN_SACH_ID),
                FOREIGN KEY ($COLUMN_PLS_PHAN_LOAI_ID) REFERENCES $TABLE_PHAN_LOAI($COLUMN_PHAN_LOAI_ID)
            )
        """.trimIndent()

        db.execSQL(createSachTable)
        db.execSQL(createPhanLoaiTable)
        db.execSQL(createPhanLoaiSachTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PHAN_LOAI_SACH")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PHAN_LOAI")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SACH")
        onCreate(db)
    }
}