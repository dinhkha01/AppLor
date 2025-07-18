package com.example.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo.data.database.AppDatabase
import com.example.demo.data.repository.LibraryRepository
import com.example.demo.ui.LibraryApp
import com.example.demo.ui.theme.DemoTheme
import com.example.demo.ui.viewmodel.LibraryViewModel
import com.example.demo.ui.viewmodel.LibraryViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = LibraryRepository(database)

        setContent {
            DemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: LibraryViewModel = viewModel(
                        factory = LibraryViewModelFactory(repository)
                    )
                    LibraryApp(viewModel = viewModel)
                }
            }
        }
    }
}
