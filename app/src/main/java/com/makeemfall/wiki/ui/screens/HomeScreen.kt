package com.makeemfall.wiki.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.makeemfall.wiki.R
import com.makeemfall.wiki.data.model.Seccion
import com.makeemfall.wiki.data.model.entradas
import com.makeemfall.wiki.ui.components.BotonMenu
import com.makeemfall.wiki.ui.components.FondoWiki
import com.makeemfall.wiki.ui.components.FormaTarjeta
import com.makeemfall.wiki.ui.components.PanelWiki
import com.makeemfall.wiki.ui.components.TextoContorno
import com.makeemfall.wiki.ui.theme.AmarilloElectrico
import com.makeemfall.wiki.ui.theme.BotonTexto
import com.makeemfall.wiki.ui.theme.CremaCalido
import com.makeemfall.wiki.ui.viewmodel.WikiUiState
import com.makeemfall.wiki.ui.viewmodel.WikiViewModel

/** Pantalla de inicio: banner del juego, introducción y botones de cada sección. */
@Composable
fun HomeScreen(
    vm: WikiViewModel,
    onSeccion: (Seccion) -> Unit,
    onFavoritos: () -> Unit,
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val favoritos by vm.favoritos.collectAsStateWithLifecycle()
    val wiki = (estado as? WikiUiState.Listo)?.wiki

    FondoWiki {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Banner principal (igual que en itch.io y el Figma)
            item {
                Box {
                    Image(
                        painterResource(R.drawable.banner_wiki),
                        contentDescription = "Make 'Em Fall",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(960f / 300f),
                    )
                    TextoContorno(
                        "WIKI",
                        MaterialTheme.typography.headlineLarge.copy(fontSize = 34.sp),
                        BotonTexto,
                        grosor = 10f,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 28.dp, bottom = 4.dp),
                    )
                }
            }

            item {
                PanelWiki(Modifier.padding(horizontal = 18.dp)) {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = AmarilloElectrico)) { append("Nuddle") }
                            append(" lo sabe todo de ti: tus búsquedas, tus compras, tus fotos. Su CEO, ")
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = AmarilloElectrico)) { append("Mr. Big Data") }
                            append(", vive en lo más alto de un rascacielos lleno de guardias y drones. La resistencia tiene un plan muy simple: ")
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, color = AmarilloElectrico)) { append("destruir Nuddle desde dentro.") }
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = CremaCalido,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }

            items(Seccion.entries) { seccion ->
                BotonMenu(
                    texto = seccion.titulo,
                    onClick = { onSeccion(seccion) },
                    icono = seccion.icono,
                    contador = wiki?.entradas(seccion)?.size?.takeIf { seccion != Seccion.HISTORIA }?.toString(),
                    modifier = Modifier.padding(horizontal = 18.dp),
                )
            }
            item {
                BotonMenu(
                    texto = "Favoritos",
                    onClick = onFavoritos,
                    icono = Icons.Filled.Star,
                    contador = "${favoritos.size}",
                    modifier = Modifier.padding(horizontal = 18.dp),
                )
            }

            // Pósters de los protagonistas (columna derecha del Figma)
            item {
                Column(Modifier.padding(horizontal = 18.dp)) {
                    Text("LA RESISTENCIA", style = MaterialTheme.typography.titleLarge, color = AmarilloElectrico)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf(R.drawable.personaje_niko_panel to "Niko", R.drawable.personaje_mateo_panel to "Mateo Cruz").forEach { (img, nombre) ->
                            Image(
                                painterResource(img),
                                contentDescription = nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(413f / 1102f)
                                    .clip(FormaTarjeta)
                                    .clickable { onSeccion(Seccion.PERSONAJES) },
                            )
                        }
                    }
                }
            }
        }
    }
}
