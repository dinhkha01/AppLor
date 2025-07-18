package com.example.demo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demo.ui.viewmodel.LibraryViewModel

@Composable
fun HomeScreen(viewModel: LibraryViewModel) {
    val allSach by viewModel.allSach.collectAsState(initial = emptyList())
    val allPhanLoai by viewModel.allPhanLoai.collectAsState(initial = emptyList())
    val allTaiLieu by viewModel.allTaiLieu.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Tổng quan Thư viện",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${allSach.size}", style = MaterialTheme.typography.headlineMedium)
                        Text("Sách", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.AccountBox, contentDescription = null, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${allPhanLoai.size}", style = MaterialTheme.typography.headlineMedium)
                        Text("Phân loại", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${allTaiLieu.size}", style = MaterialTheme.typography.headlineMedium)
                        Text("Tài liệu", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        item {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sách mới nhất", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (allSach.isNotEmpty()) {
                        allSach.take(3).forEach { sach ->
                            Text("• ${sach.tenSach} - ${sach.tacGia}")
                        }
                    } else {
                        Text("Chưa có sách nào")
                    }
                }
            }
        }

        item {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Thống kê theo năm 2023", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    val sach2023 = allSach.filter { it.namXuatBan == 2023 }
                    Text("Số sách xuất bản năm 2023: ${sach2023.size}")
                    Button(
                        onClick = { viewModel.getSachByNam(2023) }
                    ) {
                        Text("Xem chi tiết")
                    }
                }
            }
        }
    }
}