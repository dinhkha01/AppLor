package com.example.demo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.demo.data.repository.LibraryRepository
import com.example.demo.data.model.Sach
import com.example.demo.data.model.TaiLieu
import com.example.demo.data.model.PhanLoai
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    val allSach = repository.getAllSach()
    val allPhanLoai = repository.getAllPhanLoai()
    val allTaiLieu = repository.getAllTaiLieu()

    fun addSach(sach: Sach) {
        viewModelScope.launch {
            repository.insertSach(sach)
        }
    }

    fun updateSach(sach: Sach) {
        viewModelScope.launch {
            repository.updateSach(sach)
        }
    }

    fun deleteSach(sach: Sach) {
        viewModelScope.launch {
            repository.deleteSach(sach)
        }
    }

    fun addPhanLoai(phanLoai: PhanLoai) {
        viewModelScope.launch {
            repository.insertPhanLoai(phanLoai)
        }
    }

    fun addTaiLieu(taiLieu: TaiLieu) {
        viewModelScope.launch {
            repository.insertTaiLieu(taiLieu)
        }
    }

    fun getSachByNam(nam: Int) {
        viewModelScope.launch {
            repository.getSachByNam(nam).collect { sachList ->
                _uiState.value = _uiState.value.copy(filteredSach = sachList)
            }
        }
    }

    fun updateCurrentScreen(screen: String) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }
}

data class LibraryUiState(
    val currentScreen: String = "home",
    val filteredSach: List<Sach> = emptyList(),
    val selectedPhanLoai: PhanLoai? = null,
    val isLoading: Boolean = false
)

class LibraryViewModelFactory(private val repository: LibraryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}