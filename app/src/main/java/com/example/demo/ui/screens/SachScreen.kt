package com.example.demo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.demo.data.model.Sach
import com.example.demo.ui.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SachScreen(viewModel: LibraryViewModel) {
    val allSach by viewModel.allSach.collectAsState(initial = emptyList())
    val allPhanLoai by viewModel.allPhanLoai.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Danh sách Sách",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(allSach) { sach ->
                SachItem(sach = sach, phanLoai = allPhanLoai.find { it.id == sach.phanLoaiId }?.tenPhanLoai ?: "")
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm sách")
        }
    }

    if (showAddDialog) {
        AddSachDialog(
            onDismiss = { showAddDialog = false },
            onSave = { sach ->
                viewModel.addSach(sach)
                showAddDialog = false
            },
            phanLoaiList = allPhanLoai
        )
    }
}

@Composable
fun SachItem(sach: Sach, phanLoai: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = sach.tenSach,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Tác giả: ${sach.tacGia}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Năm xuất bản: ${sach.namXuatBan}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Phân loại: $phanLoai",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Trạng thái: ${sach.trangThai}",
                style = MaterialTheme.typography.bodySmall,
                color = when(sach.trangThai) {
                    "Có sẵn" -> MaterialTheme.colorScheme.primary
                    "Đã mượn" -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.secondary
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSachDialog(
    onDismiss: () -> Unit,
    onSave: (Sach) -> Unit,
    phanLoaiList: List<com.example.demo.data.model.PhanLoai>
) {
    var tenSach by remember { mutableStateOf("") }
    var tacGia by remember { mutableStateOf("") }
    var namXuatBan by remember { mutableStateOf("") }
    var soTrang by remember { mutableStateOf("") }
    var selectedPhanLoai by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Sách Mới") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = tenSach,
                    onValueChange = { tenSach = it },
                    label = { Text("Tên sách") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tacGia,
                    onValueChange = { tacGia = it },
                    label = { Text("Tác giả") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = namXuatBan,
                    onValueChange = { namXuatBan = it },
                    label = { Text("Năm xuất bản") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = soTrang,
                    onValueChange = { soTrang = it },
                    label = { Text("Số trang") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = phanLoaiList.find { it.id == selectedPhanLoai }?.tenPhanLoai ?: "Chọn phân loại",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Phân loại") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        phanLoaiList.forEach { phanLoai ->
                            DropdownMenuItem(
                                text = { Text(phanLoai.tenPhanLoai) },
                                onClick = {
                                    selectedPhanLoai = phanLoai.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (tenSach.isNotBlank() && tacGia.isNotBlank() &&
                        namXuatBan.isNotBlank() && soTrang.isNotBlank() && selectedPhanLoai > 0) {
                        onSave(
                            Sach(
                                tenSach = tenSach,
                                tacGia = tacGia,
                                namXuatBan = namXuatBan.toIntOrNull() ?: 0,
                                soTrang = soTrang.toIntOrNull() ?: 0,
                                phanLoaiId = selectedPhanLoai
                            )
                        )
                    }
                }
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}