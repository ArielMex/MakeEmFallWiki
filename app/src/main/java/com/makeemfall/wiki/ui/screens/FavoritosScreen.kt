package com.makeemfall.wiki.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.makeemfall.wiki.data.model.WikiEntry
import com.makeemfall.wiki.data.model.todasLasEntradas
import com.makeemfall.wiki.ui.components.BarraWiki
import com.makeemfall.wiki.ui.components.EncabezadoSeccion
import com.makeemfall.wiki.ui.components.EntradaCard
import com.makeemfall.wiki.ui.components.FondoWiki
import com.makeemfall.wiki.ui.components.PantallaCargando
import com.makeemfall.wiki.ui.components.PantallaError
import com.makeemfall.wiki.ui.theme.CremaCalido
import com.makeemfall.wiki.ui.viewmodel.WikiUiState
import com.makeemfall.wiki.ui.viewmodel.WikiViewModel

/** Favoritos guardados en el almacenamiento del celular (DataStore). */
@Composable
fun FavoritosScreen(
    vm: WikiViewModel,
    onEntrada: (WikiEntry) -> Unit,
    onVolver: () -> Unit,
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val favoritos by vm.favoritos.collectAsStateWithLifecycle()

    FondoWiki {
        Column(Modifier.fillMaxSize()) {
            BarraWiki(onVolver)
            when (val e = estado) {
                WikiUiState.Cargando -> PantallaCargando()
                is WikiUiState.Error -> PantallaError(e.mensaje, vm::cargar)
                is WikiUiState.Listo -> {
                    val lista = e.wiki.todasLasEntradas().filter { it.key in favoritos }
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 28.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        item { EncabezadoSeccion("Favoritos", "boon_gato") }
                        if (lista.isEmpty()) {
                            item {
                                Text(
                                    "Aún no tienes favoritos. Toca la ★ en cualquier personaje, enemigo, boon o mecánica. Se guardan en tu celular.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = CremaCalido,
                                )
                            }
                        }
                        items(lista, key = { it.key }) { entrada ->
                            EntradaCard(
                                entrada = entrada,
                                esFavorito = true,
                                onClick = { onEntrada(entrada) },
                                onFavorito = { vm.alternarFavorito(entrada.key) },
                            )
                        }
                    }
                }
            }
        }
    }
}
