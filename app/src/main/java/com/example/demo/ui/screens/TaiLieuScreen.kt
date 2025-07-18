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
import com.example.demo.data.model.TaiLieu
import com.example.demo.ui.viewmodel.LibraryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaiLieuScreen(viewModel: LibraryViewModel) {
    val allTaiLieu by viewModel.allTaiLieu.collectAsState(initial = emptyList())
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
                    text = "Danh sách Tài liệu",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(allTaiLieu) { taiLieu ->
                TaiLieuItem(taiLieu = taiLieu)
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm tài liệu")
        }
    }

    if (showAddDialog) {
        AddTaiLieuDialog(
            onDismiss = { showAddDialog = false },
            onSave = { taiLieu ->
                viewModel.addTaiLieu(taiLieu)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TaiLieuItem(taiLieu: TaiLieu) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = taiLieu.tenTaiLieu,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Loại: ${taiLieu.loaiTaiLieu}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Nguồn gốc: ${taiLieu.nguonGoc}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Ngày thêm: ${taiLieu.ngayThem}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaiLieuDialog(
    onDismiss: () -> Unit,
    onSave: (TaiLieu) -> Unit
) {
    var tenTaiLieu by remember { mutableStateOf("") }
    var loaiTaiLieu by remember { mutableStateOf("") }
    var nguonGoc by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val loaiTaiLieuList = listOf("Sách", "Tạp chí", "Báo", "CD/DVD", "Tài liệu điện tử")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Tài liệu Mới") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = tenTaiLieu,
                    onValueChange = { tenTaiLieu = it },
                    label = { Text("Tên tài liệu") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = loaiTaiLieu,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Loại tài liệu") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        loaiTaiLieuList.forEach { loai ->
                            DropdownMenuItem(
                                text = { Text(loai) },
                                onClick = {
                                    loaiTaiLieu = loai
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = nguonGoc,
                    onValueChange = { nguonGoc = it },
                    label = { Text("Nguồn gốc") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (tenTaiLieu.isNotBlank() && loaiTaiLieu.isNotBlank() && nguonGoc.isNotBlank()) {
                        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                        onSave(
                            TaiLieu(
                                tenTaiLieu = tenTaiLieu,
                                loaiTaiLieu = loaiTaiLieu,
                                nguonGoc = nguonGoc,
                                ngayThem = currentDate
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