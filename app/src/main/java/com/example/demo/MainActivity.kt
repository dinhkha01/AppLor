package com.example.demo

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.Color

import java.text.SimpleDateFormat
import java.util.*

data class Sach(
    val id: Int = 0,
    val tenSach: String = "",
    val tacGia: String = "",
    val namXuatBan: Int = 0,
    val moTa: String = "",
    val ngayNhap: String = ""
)

data class PhanLoai(
    val id: Int = 0,
    val tenPhanLoai: String = "",
    val moTa: String = ""
)

// Simple theme composable to replace DemoTheme
@Composable
fun DemoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF1976D2),
            onPrimary = Color.White,
            surface = Color.White,
            onSurface = Color.Black
        ),
        content = content
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoTheme {
                LibraryManagementApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LibraryManagementApp() {
    val context = LocalContext.current
    val databaseHelper = remember { DatabaseHelper(context) }

    var selectedTab by remember { mutableStateOf(0) }
    var danhSachSach by remember { mutableStateOf(listOf<Sach>()) }
    var danhSachPhanLoai by remember { mutableStateOf(listOf<PhanLoai>()) }

    // Load dữ liệu ban đầu
    LaunchedEffect(Unit) {
        danhSachSach = loadDanhSachSach(databaseHelper)
        danhSachPhanLoai = loadDanhSachPhanLoai(databaseHelper)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý Thư viện") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab bar
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Sách") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Phân loại") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Thống kê") }
                )
            }

            // Nội dung tab
            when (selectedTab) {
                0 -> QuanLySachTab(
                    danhSachSach = danhSachSach,
                    danhSachPhanLoai = danhSachPhanLoai,
                    databaseHelper = databaseHelper,
                    onDataChanged = {
                        danhSachSach = loadDanhSachSach(databaseHelper)
                    }
                )
                1 -> QuanLyPhanLoaiTab(
                    danhSachPhanLoai = danhSachPhanLoai,
                    databaseHelper = databaseHelper,
                    onDataChanged = {
                        danhSachPhanLoai = loadDanhSachPhanLoai(databaseHelper)
                    }
                )
                2 -> ThongKeTab(
                    danhSachSach = danhSachSach,
                    danhSachPhanLoai = danhSachPhanLoai,
                    databaseHelper = databaseHelper
                )
            }
        }
    }
}

@Composable
fun QuanLySachTab(
    danhSachSach: List<Sach>,
    danhSachPhanLoai: List<PhanLoai>,
    databaseHelper: DatabaseHelper,
    onDataChanged: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var editingSach by remember { mutableStateOf<Sach?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Nút thêm sách
        Button(
            onClick = {
                editingSach = null
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Thêm Sách Mới")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Danh sách sách
        LazyColumn {
            items(danhSachSach) { sach ->
                SachItem(
                    sach = sach,
                    onEdit = {
                        editingSach = sach
                        showDialog = true
                    },
                    onDelete = {
                        deleteSach(databaseHelper, sach.id)
                        onDataChanged()
                    }
                )
            }
        }
    }

    if (showDialog) {
        SachDialog(
            sach = editingSach,
            danhSachPhanLoai = danhSachPhanLoai,
            databaseHelper = databaseHelper,
            onDismiss = { showDialog = false },
            onSave = {
                onDataChanged()
                showDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SachItem(
    sach: Sach,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = sach.tenSach,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tác giả: ${sach.tacGia}",
                fontSize = 14.sp
            )
            Text(
                text = "Năm XB: ${sach.namXuatBan}",
                fontSize = 14.sp
            )
            if (sach.moTa.isNotEmpty()) {
                Text(
                    text = "Mô tả: ${sach.moTa}",
                    fontSize = 12.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Sửa")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Xóa")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SachDialog(
    sach: Sach?,
    danhSachPhanLoai: List<PhanLoai>,
    databaseHelper: DatabaseHelper,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var tenSach by remember { mutableStateOf(sach?.tenSach ?: "") }
    var tacGia by remember { mutableStateOf(sach?.tacGia ?: "") }
    var namXuatBan by remember { mutableStateOf(sach?.namXuatBan?.toString() ?: "") }
    var moTa by remember { mutableStateOf(sach?.moTa ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = if (sach == null) "Thêm Sách Mới" else "Sửa Sách",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = tenSach,
                    onValueChange = { tenSach = it },
                    label = { Text("Tên Sách") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = tacGia,
                    onValueChange = { tacGia = it },
                    label = { Text("Tác Giả") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = namXuatBan,
                    onValueChange = { namXuatBan = it },
                    label = { Text("Năm Xuất Bản") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = moTa,
                    onValueChange = { moTa = it },
                    label = { Text("Mô Tả") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (tenSach.isNotBlank() && tacGia.isNotBlank() && namXuatBan.isNotBlank()) {
                                val sachMoi = Sach(
                                    id = sach?.id ?: 0,
                                    tenSach = tenSach,
                                    tacGia = tacGia,
                                    namXuatBan = namXuatBan.toIntOrNull() ?: 0,
                                    moTa = moTa,
                                    ngayNhap = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                )

                                if (sach == null) {
                                    insertSach(databaseHelper, sachMoi)
                                } else {
                                    updateSach(databaseHelper, sachMoi)
                                }
                                onSave()
                            }
                        }
                    ) {
                        Text("Lưu")
                    }
                }
            }
        }
    }
}

@Composable
fun QuanLyPhanLoaiTab(
    danhSachPhanLoai: List<PhanLoai>,
    databaseHelper: DatabaseHelper,
    onDataChanged: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var editingPhanLoai by remember { mutableStateOf<PhanLoai?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = {
                editingPhanLoai = null
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Thêm Phân Loại Mới")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(danhSachPhanLoai) { phanLoai ->
                PhanLoaiItem(
                    phanLoai = phanLoai,
                    onEdit = {
                        editingPhanLoai = phanLoai
                        showDialog = true
                    },
                    onDelete = {
                        deletePhanLoai(databaseHelper, phanLoai.id)
                        onDataChanged()
                    }
                )
            }
        }
    }

    if (showDialog) {
        PhanLoaiDialog(
            phanLoai = editingPhanLoai,
            databaseHelper = databaseHelper,
            onDismiss = { showDialog = false },
            onSave = {
                onDataChanged()
                showDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhanLoaiItem(
    phanLoai: PhanLoai,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = phanLoai.tenPhanLoai,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (phanLoai.moTa.isNotEmpty()) {
                Text(
                    text = phanLoai.moTa,
                    fontSize = 14.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Sửa")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Xóa")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhanLoaiDialog(
    phanLoai: PhanLoai?,
    databaseHelper: DatabaseHelper,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var tenPhanLoai by remember { mutableStateOf(phanLoai?.tenPhanLoai ?: "") }
    var moTa by remember { mutableStateOf(phanLoai?.moTa ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = if (phanLoai == null) "Thêm Phân Loại Mới" else "Sửa Phân Loại",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = tenPhanLoai,
                    onValueChange = { tenPhanLoai = it },
                    label = { Text("Tên Phân Loại") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = moTa,
                    onValueChange = { moTa = it },
                    label = { Text("Mô Tả") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (tenPhanLoai.isNotBlank()) {
                                val phanLoaiMoi = PhanLoai(
                                    id = phanLoai?.id ?: 0,
                                    tenPhanLoai = tenPhanLoai,
                                    moTa = moTa
                                )

                                if (phanLoai == null) {
                                    insertPhanLoai(databaseHelper, phanLoaiMoi)
                                } else {
                                    updatePhanLoai(databaseHelper, phanLoaiMoi)
                                }
                                onSave()
                            }
                        }
                    ) {
                        Text("Lưu")
                    }
                }
            }
        }
    }
}

@Composable
fun ThongKeTab(
    danhSachSach: List<Sach>,
    danhSachPhanLoai: List<PhanLoai>,
    databaseHelper: DatabaseHelper
) {
    val sachTrongNam2023 = danhSachSach.filter { it.namXuatBan == 2023 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Thống kê tổng quan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tổng số sách: ${danhSachSach.size}")
                Text("Tổng số phân loại: ${danhSachPhanLoai.size}")
                Text("Sách xuất bản năm 2023: ${sachTrongNam2023.size}")
            }
        }

        if (sachTrongNam2023.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Sách xuất bản năm 2023",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    sachTrongNam2023.forEach { sach ->
                        Text("• ${sach.tenSach} - ${sach.tacGia}")
                    }
                }
            }
        }
    }
}

// Các hàm helper để tương tác với database
fun loadDanhSachSach(databaseHelper: DatabaseHelper): List<Sach> {
    val db = databaseHelper.readableDatabase
    val cursor = db.query(
        DatabaseHelper.TABLE_SACH,
        null, null, null, null, null, null
    )

    val danhSach = mutableListOf<Sach>()
    if (cursor.moveToFirst()) {
        do {
            val sach = Sach(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SACH_ID)),
                tenSach = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SACH_TEN)),
                tacGia = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SACH_TAC_GIA)),
                namXuatBan = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SACH_NAM_XB)),
                moTa = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SACH_MO_TA)) ?: "",
                ngayNhap = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SACH_NGAY_NHAP))
            )
            danhSach.add(sach)
        } while (cursor.moveToNext())
    }
    cursor.close()
    return danhSach
}

fun loadDanhSachPhanLoai(databaseHelper: DatabaseHelper): List<PhanLoai> {
    val db = databaseHelper.readableDatabase
    val cursor = db.query(
        DatabaseHelper.TABLE_PHAN_LOAI,
        null, null, null, null, null, null
    )

    val danhSach = mutableListOf<PhanLoai>()
    if (cursor.moveToFirst()) {
        do {
            val phanLoai = PhanLoai(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHAN_LOAI_ID)),
                tenPhanLoai = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHAN_LOAI_TEN)),
                moTa = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHAN_LOAI_MO_TA)) ?: ""
            )
            danhSach.add(phanLoai)
        } while (cursor.moveToNext())
    }
    cursor.close()
    return danhSach
}

fun insertSach(databaseHelper: DatabaseHelper, sach: Sach) {
    val db = databaseHelper.writableDatabase
    val values = ContentValues().apply {
        put(DatabaseHelper.COLUMN_SACH_TEN, sach.tenSach)
        put(DatabaseHelper.COLUMN_SACH_TAC_GIA, sach.tacGia)
        put(DatabaseHelper.COLUMN_SACH_NAM_XB, sach.namXuatBan)
        put(DatabaseHelper.COLUMN_SACH_MO_TA, sach.moTa)
        put(DatabaseHelper.COLUMN_SACH_NGAY_NHAP, sach.ngayNhap)
    }
    db.insert(DatabaseHelper.TABLE_SACH, null, values)
}

fun updateSach(databaseHelper: DatabaseHelper, sach: Sach) {
    val db = databaseHelper.writableDatabase
    val values = ContentValues().apply {
        put(DatabaseHelper.COLUMN_SACH_TEN, sach.tenSach)
        put(DatabaseHelper.COLUMN_SACH_TAC_GIA, sach.tacGia)
        put(DatabaseHelper.COLUMN_SACH_NAM_XB, sach.namXuatBan)
        put(DatabaseHelper.COLUMN_SACH_MO_TA, sach.moTa)
    }
    db.update(DatabaseHelper.TABLE_SACH, values, "${DatabaseHelper.COLUMN_SACH_ID} = ?", arrayOf(sach.id.toString()))
}

fun deleteSach(databaseHelper: DatabaseHelper, id: Int) {
    val db = databaseHelper.writableDatabase
    db.delete(DatabaseHelper.TABLE_SACH, "${DatabaseHelper.COLUMN_SACH_ID} = ?", arrayOf(id.toString()))
}

fun insertPhanLoai(databaseHelper: DatabaseHelper, phanLoai: PhanLoai) {
    val db = databaseHelper.writableDatabase
    val values = ContentValues().apply {
        put(DatabaseHelper.COLUMN_PHAN_LOAI_TEN, phanLoai.tenPhanLoai)
        put(DatabaseHelper.COLUMN_PHAN_LOAI_MO_TA, phanLoai.moTa)
    }
    db.insert(DatabaseHelper.TABLE_PHAN_LOAI, null, values)
}

fun updatePhanLoai(databaseHelper: DatabaseHelper, phanLoai: PhanLoai) {
    val db = databaseHelper.writableDatabase
    val values = ContentValues().apply {
        put(DatabaseHelper.COLUMN_PHAN_LOAI_TEN, phanLoai.tenPhanLoai)
        put(DatabaseHelper.COLUMN_PHAN_LOAI_MO_TA, phanLoai.moTa)
    }
    db.update(DatabaseHelper.TABLE_PHAN_LOAI, values, "${DatabaseHelper.COLUMN_PHAN_LOAI_ID} = ?", arrayOf(phanLoai.id.toString()))
}

fun deletePhanLoai(databaseHelper: DatabaseHelper, id: Int) {
    val db = databaseHelper.writableDatabase
    db.delete(DatabaseHelper.TABLE_PHAN_LOAI, "${DatabaseHelper.COLUMN_PHAN_LOAI_ID} = ?", arrayOf(id.toString()))
}

@Preview(showBackground = true)
@Composable
fun LibraryManagementPreview() {
    DemoTheme {
        LibraryManagementApp()
    }
}