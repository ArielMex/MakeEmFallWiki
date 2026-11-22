package com.makeemfall.wiki.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.makeemfall.wiki.data.model.Seccion
import com.makeemfall.wiki.data.model.WikiEntry
import com.makeemfall.wiki.ui.theme.AmarilloElectrico
import com.makeemfall.wiki.ui.theme.CremaCalido
import com.makeemfall.wiki.ui.theme.NaranjaAmbar
import com.makeemfall.wiki.ui.theme.NegroBerenjena
import com.makeemfall.wiki.ui.theme.RojoRebelion
import com.makeemfall.wiki.ui.theme.VinoProfundo

/** Elemento de la lista: texto a la izquierda e imagen a la derecha, como en el Figma. */
@Composable
fun EntradaCard(
    entrada: WikiEntry,
    esFavorito: Boolean,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
) {
    PanelWiki(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        borde = if (esFavorito) AmarilloElectrico else RojoRebelion,
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                TextoContorno(entrada.titulo.uppercase(), MaterialTheme.typography.titleLarge, CremaCalido, grosor = 6f)
                if (entrada.subtitulo.isNotBlank()) {
                    Text(entrada.subtitulo, style = MaterialTheme.typography.labelLarge, color = NaranjaAmbar)
                }
                if (entrada.descripcion.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        entrada.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CremaCalido.copy(alpha = 0.9f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (entrada.seccion != Seccion.CONTROLES) {
                    ImagenWiki(
                        imagen = entrada.imagen,
                        url = entrada.imagenUrl,
                        descripcion = entrada.titulo,
                        respaldo = entrada.seccion.icono,
                        pixelado = entrada.pixelado,
                        contentScale = if (entrada.seccion == Seccion.PERSONAJES) ContentScale.Crop else ContentScale.Fit,
                        modifier = Modifier
                            .size(84.dp)
                            .clip(FormaTarjeta)
                            .background(if (entrada.seccion == Seccion.BOONS) VinoProfundo else NegroBerenjena),
                    )
                }
                IconButton(onClick = onFavorito) {
                    Icon(
                        if (esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = if (esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = AmarilloElectrico,
                    )
                }
            }
        }
    }
}

/** Bloque de la sección Historia: título, texto completo y captura (como el Figma). */
@Composable
fun BloqueHistoria(entrada: WikiEntry) {
    PanelWiki(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            TextoContorno(entrada.titulo.uppercase(), MaterialTheme.typography.titleLarge, AmarilloElectrico, grosor = 6f)
            Spacer(Modifier.height(6.dp))
            Text(entrada.descripcion, style = MaterialTheme.typography.bodyLarge, color = CremaCalido)
            if (entrada.imagen != null || entrada.imagenUrl != null) {
                Spacer(Modifier.height(12.dp))
                ImagenWiki(
                    imagen = entrada.imagen, url = entrada.imagenUrl, descripcion = entrada.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(FormaTarjeta),
                )
            }
        }
    }
}
