package com.example.demo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import com.example.demo.ui.screens.*
import com.example.demo.ui.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryApp(viewModel: LibraryViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý Thư viện") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
                    label = { Text("Trang chủ") },
                    selected = uiState.currentScreen == "home",
                    onClick = { viewModel.updateCurrentScreen("home") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MailOutline, contentDescription = "Sách") },
                    label = { Text("Sách") },
                    selected = uiState.currentScreen == "sach",
                    onClick = { viewModel.updateCurrentScreen("sach") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Phân loại") },
                    label = { Text("Phân loại") },
                    selected = uiState.currentScreen == "phanloai",
                    onClick = { viewModel.updateCurrentScreen("phanloai") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Done, contentDescription = "Tài liệu") },
                    label = { Text("Tài liệu") },
                    selected = uiState.currentScreen == "tailieu",
                    onClick = { viewModel.updateCurrentScreen("tailieu") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                "home" -> HomeScreen(viewModel)
                "sach" -> SachScreen(viewModel)
                "phanloai" -> PhanLoaiScreen(viewModel)
                "tailieu" -> TaiLieuScreen(viewModel)
            }
        }
    }
}