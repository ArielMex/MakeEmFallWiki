package com.makeemfall.wiki.ui.components

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Shape

/**
 * Forma del botón del menú, copiada del vector "Fondo" del Figma (258 × 59).
 * Los puntos están normalizados (0..1) para que se adapte a cualquier tamaño.
 */
private val PUNTOS_BOTON = listOf(
    11.53f to 4.21f, 64.86f to 2.11f, 129.72f to 3.51f, 258f to 0f, 253.68f to 17.70f,
    257.28f to 32.45f, 247.91f to 54.79f, 155.66f to 57.60f, 77.83f to 59f, 2.88f to 56.89f,
    7.21f to 35.40f, 0f to 20.65f,
).map { (x, y) -> x / 258f to y / 59f }

val FormaBotonMenu: Shape = GenericShape { size, _ ->
    PUNTOS_BOTON.forEachIndexed { i, (x, y) ->
        if (i == 0) moveTo(x * size.width, y * size.height) else lineTo(x * size.width, y * size.height)
    }
    close()
}

/** Paralelogramo inclinado (encabezados tipo "LA HISTORIA"). */
fun formaInclinada(inclinacion: Float = 0.04f): Shape = GenericShape { size, _ ->
    val dx = size.height * inclinacion * 6
    moveTo(dx, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width - dx, size.height)
    lineTo(0f, size.height)
    close()
}

/** Tarjeta con esquinas ligeramente torcidas, como las cartas de boon. */
val FormaTarjeta: Shape = GenericShape { size, _ ->
    moveTo(size.width * 0.01f, 0f)
    lineTo(size.width, size.height * 0.015f)
    lineTo(size.width * 0.99f, size.height)
    lineTo(0f, size.height * 0.985f)
    close()
}
