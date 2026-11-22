package com.makeemfall.wiki.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.makeemfall.wiki.R
import com.makeemfall.wiki.ui.theme.AmarilloElectrico
import com.makeemfall.wiki.ui.theme.BotonNaranja
import com.makeemfall.wiki.ui.theme.BotonRojo
import com.makeemfall.wiki.ui.theme.BotonSombra
import com.makeemfall.wiki.ui.theme.BotonTexto
import com.makeemfall.wiki.ui.theme.BotonTextoActivo
import com.makeemfall.wiki.ui.theme.ChispaAmarilla
import com.makeemfall.wiki.ui.theme.CremaCalido
import com.makeemfall.wiki.ui.theme.NegroBerenjena
import com.makeemfall.wiki.ui.theme.PanelContenido
import com.makeemfall.wiki.ui.theme.RojoRebelion
import com.makeemfall.wiki.ui.theme.VioletaIntenso

/** Fondo morado con destellos del juego (imagen del Figma) para todas las pantallas. */
@Composable
fun FondoWiki(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.fondo_wiki),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        content()
    }
}

/** Texto con contorno oscuro, como los títulos del juego. */
@Composable
fun TextoContorno(
    texto: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    contorno: Color = NegroBerenjena,
    grosor: Float = 8f,
    maxLines: Int = 2,
    textAlign: TextAlign? = null,
) {
    Box(modifier) {
        Text(
            texto, maxLines = maxLines, overflow = TextOverflow.Ellipsis, textAlign = textAlign,
            style = style.copy(color = contorno, drawStyle = Stroke(width = grosor, join = StrokeJoin.Round)),
        )
        Text(texto, maxLines = maxLines, overflow = TextOverflow.Ellipsis, textAlign = textAlign, style = style.copy(color = color))
    }
}

/**
 * Botón del menú de la wiki (componente "Botón Menú" del Figma).
 * Rojo normalmente; naranja con flecha y chispas cuando está seleccionado o presionado.
 */
@Composable
fun BotonMenu(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    seleccionado: Boolean = false,
    icono: ImageVector? = null,
    contador: String? = null,
) {
    val interaccion = remember { MutableInteractionSource() }
    val presionado by interaccion.collectIsPressedAsState()
    val activo = seleccionado || presionado

    Box(
        modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(interactionSource = interaccion, indication = null, onClick = onClick),
    ) {
        // Sombra
        Box(
            Modifier
                .matchParentSize()
                .padding(end = 8.dp)
                .offset(7.dp, 6.dp)
                .background(BotonSombra, FormaBotonMenu)
        )
        // Fondo
        Row(
            Modifier
                .matchParentSize()
                .padding(end = 8.dp)
                .background(if (activo) BotonNaranja else BotonRojo, FormaBotonMenu)
                .padding(horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (activo) {
                Flecha(Modifier.size(18.dp, 22.dp))
                Spacer(Modifier.width(12.dp))
            } else if (icono != null) {
                Icon(icono, null, tint = BotonTexto, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
            }
            Text(
                texto,
                style = MaterialTheme.typography.headlineMedium,
                color = if (activo) BotonTextoActivo else BotonTexto,
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )
            if (contador != null) {
                Text(
                    contador,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (activo) BotonTextoActivo else AmarilloElectrico,
                )
            }
        }
        if (activo) Chispas(Modifier.align(Alignment.TopEnd).size(26.dp, 30.dp))
    }
}

/** Flecha amarilla del botón seleccionado. */
@Composable
fun Flecha(modifier: Modifier = Modifier, color: Color = ChispaAmarilla) {
    Canvas(modifier) {
        val p = Path().apply { moveTo(0f, 0f); lineTo(size.width, size.height / 2); lineTo(0f, size.height); close() }
        drawPath(p, color)
    }
}

/** Las 3 chispas que salen de la esquina del botón seleccionado (vectores "Chispa" del Figma). */
@Composable
fun Chispas(modifier: Modifier = Modifier, color: Color = ChispaAmarilla) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val datos = listOf(
            floatArrayOf(0.05f, 0.05f, 0.75f, 0.22f, 40f),
            floatArrayOf(0.25f, 0.45f, 0.75f, 0.20f, 5f),
            floatArrayOf(0.10f, 0.75f, 0.65f, 0.18f, -30f),
        )
        for (d in datos) {
            val x = w * d[0]; val y = h * d[1]; val largo = w * d[2]; val grueso = h * d[3]
            val p = Path().apply {
                moveTo(x, y); lineTo(x + largo, y + grueso * 0.45f); lineTo(x + grueso * 0.45f, y + grueso); close()
            }
            rotate(d[4], Offset(x, y)) { drawPath(p, color) }
        }
    }
}

/** Puntitos de semitono que llevan los encabezados y paneles del juego. */
@Composable
fun Semitono(modifier: Modifier = Modifier, color: Color, paso: Dp = 9.dp, radio: Dp = 2.dp) {
    Canvas(modifier) {
        val p = paso.toPx(); val r = radio.toPx()
        var y = 0f; var fila = 0
        while (y < size.height + p) {
            var x = if (fila % 2 == 0) 0f else p / 2
            while (x < size.width + p) { drawCircle(color, r, Offset(x, y)); x += p }
            y += p * 0.87f; fila++
        }
    }
}

/**
 * Encabezado de sección como "LA HISTORIA" del Figma: banda roja inclinada con semitono,
 * sombra amarilla, imagen a la izquierda y texto crema con contorno.
 */
@Composable
fun EncabezadoSeccion(texto: String, imagen: String?, modifier: Modifier = Modifier) {
    val forma = formaInclinada(0.05f)
    Box(modifier.fillMaxWidth().height(70.dp)) {
        Box(Modifier.matchParentSize().padding(end = 6.dp).offset(6.dp, 6.dp).background(AmarilloElectrico, forma))
        Box(
            Modifier
                .matchParentSize()
                .padding(end = 6.dp)
                .clip(forma)
                .background(RojoRebelion)
                .border(3.dp, NegroBerenjena, forma)
        ) {
            Semitono(Modifier.matchParentSize(), color = Color(0xFFD4002F), paso = 10.dp, radio = 2.5.dp)
            Row(
                Modifier.matchParentSize().padding(start = 18.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (imagen != null) {
                    ImagenWiki(
                        imagen = imagen, url = null, descripcion = null,
                        modifier = Modifier.size(52.dp).clip(FormaTarjeta).background(NegroBerenjena),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(Modifier.width(12.dp))
                }
                TextoContorno(
                    texto.uppercase(),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                    color = CremaCalido,
                    maxLines = 1,
                )
            }
        }
    }
}

/** Panel azul oscuro donde va el contenido (columna central del Figma). */
@Composable
fun PanelWiki(modifier: Modifier = Modifier, borde: Color = VioletaIntenso, content: @Composable () -> Unit) {
    Box(modifier) {
        Box(Modifier.matchParentSize().offset(5.dp, 5.dp).background(NegroBerenjena, FormaTarjeta))
        Box(
            Modifier
                .fillMaxWidth()
                .background(PanelContenido.copy(alpha = 0.94f), FormaTarjeta)
                .border(2.dp, borde, FormaTarjeta)
        ) { content() }
    }
}

/**
 * Muestra una imagen guardada en la app (nombre de res/drawable-nodpi) o, si no existe,
 * una imagen de internet (imagenUrl). Si no hay ninguna, muestra el ícono de respaldo.
 */
@SuppressLint("DiscouragedApi")
@Composable
fun ImagenWiki(
    imagen: String?,
    url: String?,
    descripcion: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    respaldo: ImageVector? = null,
    pixelado: Boolean = false,
) {
    val context = LocalContext.current
    val id = remember(imagen) {
        imagen?.let { context.resources.getIdentifier(it, "drawable", context.packageName) } ?: 0
    }
    when {
        id != 0 && pixelado -> Image(
            bitmap = ImageBitmap.imageResource(id),
            contentDescription = descripcion,
            modifier = modifier,
            contentScale = contentScale,
            filterQuality = FilterQuality.None,
        )
        id != 0 -> Image(painterResource(id), descripcion, modifier, contentScale = contentScale)
        url != null -> AsyncImage(model = url, contentDescription = descripcion, modifier = modifier, contentScale = contentScale)
        respaldo != null -> Box(modifier, contentAlignment = Alignment.Center) {
            Icon(respaldo, descripcion, tint = AmarilloElectrico, modifier = Modifier.fillMaxSize(0.55f))
        }
        else -> Box(modifier)
    }
}

@Composable
fun PantallaCargando() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(color = AmarilloElectrico)
        Spacer(Modifier.height(14.dp))
        Text("Hackeando los servidores de Nuddle…", style = MaterialTheme.typography.titleMedium, color = CremaCalido)
    }
}

@Composable
fun PantallaError(mensaje: String, onReintentar: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextoContorno("GAME!", MaterialTheme.typography.displayLarge, RojoRebelion)
        Spacer(Modifier.height(12.dp))
        Text(mensaje, style = MaterialTheme.typography.bodyLarge, color = CremaCalido, textAlign = TextAlign.Center)
        Spacer(Modifier.height(20.dp))
        BotonMenu("Reintentar", onReintentar, Modifier.width(240.dp))
    }
}
