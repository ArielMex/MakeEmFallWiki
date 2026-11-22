package com.makeemfall.wiki.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.makeemfall.wiki.data.model.Seccion
import com.makeemfall.wiki.data.model.WikiEntry
import com.makeemfall.wiki.data.model.entradas
import com.makeemfall.wiki.ui.components.BarraWiki
import com.makeemfall.wiki.ui.components.BloqueHistoria
import com.makeemfall.wiki.ui.components.EncabezadoSeccion
import com.makeemfall.wiki.ui.components.EntradaCard
import com.makeemfall.wiki.ui.components.FondoWiki
import com.makeemfall.wiki.ui.components.FormaBotonMenu
import com.makeemfall.wiki.ui.components.PantallaCargando
import com.makeemfall.wiki.ui.components.PantallaError
import com.makeemfall.wiki.ui.theme.AmarilloElectrico
import com.makeemfall.wiki.ui.theme.BotonNaranja
import com.makeemfall.wiki.ui.theme.BotonRojo
import com.makeemfall.wiki.ui.theme.BotonTexto
import com.makeemfall.wiki.ui.theme.BotonTextoActivo
import com.makeemfall.wiki.ui.theme.CremaCalido
import com.makeemfall.wiki.ui.theme.PanelContenido
import com.makeemfall.wiki.ui.theme.RojoRebelion
import com.makeemfall.wiki.ui.viewmodel.WikiUiState
import com.makeemfall.wiki.ui.viewmodel.WikiViewModel

/** Lista de una sección con búsqueda y filtros (funcionalidades adicionales de la API). */
@Composable
fun ListaScreen(
    seccion: Seccion,
    vm: WikiViewModel,
    onEntrada: (WikiEntry) -> Unit,
    onVolver: () -> Unit,
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val favoritos by vm.favoritos.collectAsStateWithLifecycle()
    var busqueda by rememberSaveable { mutableStateOf("") }
    var filtro by rememberSaveable { mutableStateOf<String?>(null) }

    FondoWiki {
        Column(Modifier.fillMaxSize()) {
            BarraWiki(onVolver)
            when (val e = estado) {
                WikiUiState.Cargando -> PantallaCargando()
                is WikiUiState.Error -> PantallaError(e.mensaje, vm::cargar)
                is WikiUiState.Listo -> {
                    val todas = remember(e.wiki, seccion) { e.wiki.entradas(seccion) }
                    val categorias = remember(todas) { todas.map { it.etiqueta }.filter(String::isNotBlank).distinct() }
                    val visibles = todas.filter { entrada ->
                        (filtro == null || entrada.etiqueta == filtro) &&
                            (busqueda.isBlank() ||
                                entrada.titulo.contains(busqueda, ignoreCase = true) ||
                                entrada.subtitulo.contains(busqueda, ignoreCase = true) ||
                                entrada.descripcion.contains(busqueda, ignoreCase = true))
                    }
                    val conBusqueda = seccion.nombreFiltro != null

                    LazyColumn(
                        Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 28.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        item { EncabezadoSeccion(seccion.encabezado, seccion.imagenEncabezado) }

                        if (conBusqueda) {
                            item {
                                OutlinedTextField(
                                    value = busqueda,
                                    onValueChange = { busqueda = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("Buscar en ${seccion.titulo.lowercase()}…", style = MaterialTheme.typography.bodyLarge) },
                                    leadingIcon = { Icon(Icons.Filled.Search, null, tint = AmarilloElectrico) },
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.bodyLarge,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = AmarilloElectrico,
                                        unfocusedBorderColor = RojoRebelion,
                                        focusedContainerColor = PanelContenido,
                                        unfocusedContainerColor = PanelContenido,
                                        cursorColor = AmarilloElectrico,
                                        focusedTextColor = CremaCalido,
                                        unfocusedTextColor = CremaCalido,
                                    ),
                                )
                            }
                            if (categorias.size > 1) {
                                item {
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        item { Chip("Todos", filtro == null) { filtro = null } }
                                        items(categorias) { c -> Chip(c, filtro == c) { filtro = if (filtro == c) null else c } }
                                    }
                                }
                            }
                            item {
                                Text(
                                    "Mostrando ${visibles.size} de ${todas.size} · ${seccion.nombreFiltro}: ${filtro ?: "todos"}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = CremaCalido.copy(alpha = 0.7f),
                                )
                            }
                        }

                        if (visibles.isEmpty()) {
                            item {
                                Text(
                                    "No hay resultados para \"$busqueda\"",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = CremaCalido,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                )
                            }
                        }

                        items(visibles, key = { it.key }) { entrada ->
                            if (seccion == Seccion.HISTORIA) {
                                BloqueHistoria(entrada)
                            } else {
                                EntradaCard(
                                    entrada = entrada,
                                    esFavorito = entrada.key in favoritos,
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
}

/** Filtro con la misma forma que los botones del menú. */
@Composable
private fun Chip(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto, style = MaterialTheme.typography.labelLarge) },
        shape = FormaBotonMenu,
        border = null,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = BotonRojo,
            labelColor = BotonTexto,
            selectedContainerColor = BotonNaranja,
            selectedLabelColor = BotonTextoActivo,
        ),
    )
}
