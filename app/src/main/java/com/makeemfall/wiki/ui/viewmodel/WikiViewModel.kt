package com.makeemfall.wiki.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.makeemfall.wiki.data.local.FavoritosStore
import com.makeemfall.wiki.data.model.WikiResponse
import com.makeemfall.wiki.data.repository.WikiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface WikiUiState {
    data object Cargando : WikiUiState
    data class Listo(val wiki: WikiResponse) : WikiUiState
    data class Error(val mensaje: String) : WikiUiState
}

class WikiViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = WikiRepository()
    private val favoritosStore = FavoritosStore(app)

    private val _estado = MutableStateFlow<WikiUiState>(WikiUiState.Cargando)
    val estado: StateFlow<WikiUiState> = _estado.asStateFlow()

    val favoritos: StateFlow<Set<String>> = favoritosStore.favoritos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    init { cargar() }

    fun cargar() {
        _estado.value = WikiUiState.Cargando
        viewModelScope.launch {
            repo.obtenerWiki()
                .onSuccess { _estado.value = WikiUiState.Listo(it) }
                .onFailure {
                    _estado.value = WikiUiState.Error(
                        "No se pudo cargar la wiki. Revisa tu conexión.\n(${it.message})"
                    )
                }
        }
    }

    fun alternarFavorito(key: String) {
        viewModelScope.launch { favoritosStore.alternar(key) }
    }
}
