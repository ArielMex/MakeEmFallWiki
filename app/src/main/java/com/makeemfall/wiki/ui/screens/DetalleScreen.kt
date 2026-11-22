package com.makeemfall.wiki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.makeemfall.wiki.data.model.Seccion
import com.makeemfall.wiki.data.model.entradas
import com.makeemfall.wiki.ui.components.BarraWiki
import com.makeemfall.wiki.ui.components.BotonMenu
import com.makeemfall.wiki.ui.components.EncabezadoSeccion
import com.makeemfall.wiki.ui.components.FondoWiki
import com.makeemfall.wiki.ui.components.FormaTarjeta
import com.makeemfall.wiki.ui.components.ImagenWiki
import com.makeemfall.wiki.ui.components.PanelWiki
import com.makeemfall.wiki.ui.components.PantallaCargando
import com.makeemfall.wiki.ui.components.PantallaError
import com.makeemfall.wiki.ui.components.TextoContorno
import com.makeemfall.wiki.ui.theme.AmarilloElectrico
import com.makeemfall.wiki.ui.theme.CremaCalido
import com.makeemfall.wiki.ui.theme.NaranjaAmbar
import com.makeemfall.wiki.ui.theme.NaranjaFuego
import com.makeemfall.wiki.ui.theme.NegroBerenjena
import com.makeemfall.wiki.ui.theme.RojoRebelion
import com.makeemfall.wiki.ui.theme.VinoProfundo
import com.makeemfall.wiki.ui.viewmodel.WikiUiState
import com.makeemfall.wiki.ui.viewmodel.WikiViewModel

/** Detalle de un personaje, enemigo, boon, mecánica o control. */
@Composable
fun DetalleScreen(
    seccion: Seccion,
    id: String,
    vm: WikiViewModel,
    onVolver: () -> Unit,
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val favoritos by vm.favoritos.collectAsStateWithLifecycle()

    FondoWiki {
        Column(Modifier.fillMaxSize()) {
            val entrada = (estado as? WikiUiState.Listo)?.wiki?.entradas(seccion)?.firstOrNull { it.id == id }
            val esFavorito = entrada != null && entrada.key in favoritos

            BarraWiki(onVolver) {
                if (entrada != null) {
                    IconButton(onClick = { vm.alternarFavorito(entrada.key) }) {
                        Icon(
                            if (esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Favorito",
                            tint = AmarilloElectrico,
                        )
                    }
                }
            }

            when (val e = estado) {
                WikiUiState.Cargando -> PantallaCargando()
                is WikiUiState.Error -> PantallaError(e.mensaje, vm::cargar)
                is WikiUiState.Listo -> if (entrada == null) {
                    PantallaError("No se encontró este elemento.", onVolver)
                } else Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    EncabezadoSeccion(entrada.titulo, imagen = null)

                    // Imagen principal: póster para los protagonistas, ícono para boons, etc.
                    when {
                        entrada.imagenGrande != null -> ImagenWiki(
                            imagen = entrada.imagenGrande, url = null, descripcion = entrada.titulo,
                            contentScale = ContentScale.Fit,
                            pixelado = entrada.pixelado,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(440.dp)
                                .clip(FormaTarjeta)
                                .background(NegroBerenjena),
                        )
                        entrada.imagen != null || entrada.imagenUrl != null -> ImagenWiki(
                            imagen = entrada.imagen, url = entrada.imagenUrl, descripcion = entrada.titulo,
                            pixelado = entrada.pixelado,
                            contentScale = if (entrada.seccion == Seccion.BOONS) ContentScale.Fit else ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .clip(FormaTarjeta)
                                .background(if (entrada.seccion == Seccion.BOONS) VinoProfundo else NegroBerenjena)
                                .padding(if (entrada.seccion == Seccion.BOONS) 24.dp else 0.dp),
                        )
                    }

                    if (entrada.subtitulo.isNotBlank()) {
                        Text(entrada.subtitulo, style = MaterialTheme.typography.titleMedium, color = NaranjaAmbar)
                    }
                    if (entrada.descripcion.isNotBlank()) {
                        PanelWiki(Modifier.fillMaxWidth()) {
                            Text(
                                entrada.descripcion,
                                style = MaterialTheme.typography.bodyLarge,
                                color = CremaCalido,
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                    }

                    if (entrada.detalles.isNotEmpty()) {
                        PanelWiki(Modifier.fillMaxWidth(), borde = RojoRebelion) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                entrada.detalles.forEach { (nombre, valor) ->
                                    Row(Modifier.fillMaxWidth()) {
                                        Text(
                                            nombre.uppercase(),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = NaranjaFuego,
                                            modifier = Modifier.weight(1f),
                                        )
                                        Text(
                                            valor,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = CremaCalido,
                                            modifier = Modifier.weight(1.6f),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (entrada.lista.isNotEmpty()) {
                        TextoContorno("HABILIDADES", MaterialTheme.typography.titleLarge, AmarilloElectrico, grosor = 6f)
                        entrada.lista.forEach {
                            Text("▸ $it", style = MaterialTheme.typography.bodyLarge, color = CremaCalido)
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    BotonMenu(
                        texto = if (esFavorito) "En favoritos" else "Agregar a favoritos",
                        onClick = { vm.alternarFavorito(entrada.key) },
                        seleccionado = esFavorito,
                        icono = Icons.Outlined.StarBorder,
                    )
                }
            }
        }
    }
}
