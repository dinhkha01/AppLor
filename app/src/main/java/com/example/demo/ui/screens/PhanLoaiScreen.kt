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
import com.example.demo.data.model.PhanLoai
import com.example.demo.ui.viewmodel.LibraryViewModel

@Composable
fun PhanLoaiScreen(viewModel: LibraryViewModel) {
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
                    text = "Danh sách Phân loại",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(allPhanLoai) { phanLoai ->
                PhanLoaiItem(phanLoai = phanLoai)
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm phân loại")
        }
    }

    if (showAddDialog) {
        AddPhanLoaiDialog(
            onDismiss = { showAddDialog = false },
            onSave = { phanLoai ->
                viewModel.addPhanLoai(phanLoai)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun PhanLoaiItem(phanLoai: PhanLoai) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = phanLoai.tenPhanLoai,
                style = MaterialTheme.typography.titleMedium
            )
            if (!phanLoai.moTa.isNullOrBlank()) {
                Text(
                    text = phanLoai.moTa,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun AddPhanLoaiDialog(
    onDismiss: () -> Unit,
    onSave: (PhanLoai) -> Unit
) {
    var tenPhanLoai by remember { mutableStateOf("") }
    var moTa by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Phân loại Mới") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = tenPhanLoai,
                    onValueChange = { tenPhanLoai = it },
                    label = { Text("Tên phân loại") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = moTa,
                    onValueChange = { moTa = it },
                    label = { Text("Mô tả") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (tenPhanLoai.isNotBlank()) {
                        onSave(
                            PhanLoai(
                                tenPhanLoai = tenPhanLoai,
                                moTa = moTa.takeIf { it.isNotBlank() }
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