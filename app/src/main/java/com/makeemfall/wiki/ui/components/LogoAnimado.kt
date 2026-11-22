package com.makeemfall.wiki.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import com.makeemfall.wiki.R
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

/*
 * Animación del logo de la pantalla de título del juego, traducida de Unity:
 *  - Assets/_Game/UI/Title/LogoPiece.cs   (cómo se mueve cada pieza)
 *  - Assets/_Game/UI/Title/TitleMenu.cs   (temblor al caer las letras)
 *  - Assets/_Game/Resources/UI/Logo/parts.txt y Scenes/Title.unity (posición, retraso e inclinación)
 * Las letras de MAKE'EM caen una por una, luego las de FALL, después la pincelada,
 * los puntitos y las chispas. Al final todo queda flotando suavemente.
 */

private const val ANCHO = 1200f
private const val ALTO = 900f
private const val CAIDA = 0.28f        // LetterFall
private const val ALTURA_CAIDA = 1100f // LetterDrop
private const val TEMBLOR = 9f         // LandShake

private enum class Tipo { PUNTOS, PINCELADA, ABAJO, ARRIBA, CHISPAS }

private class Pieza(
    @DrawableRes val imagen: Int,
    val x: Float, val y: Float, val w: Float, val h: Float,
    val tipo: Tipo,
    val retraso: Float,
    val inclinacion: Float,
    val indice: Int,
)

// En el mismo orden de dibujo que en Unity (de atrás hacia adelante).
private val PIEZAS = listOf(
    Pieza(R.drawable.splash_logo_12, 82f, 5f, 1062f, 336f, Tipo.PUNTOS, 1.425f, -3.27f, 0),
    Pieza(R.drawable.splash_logo_11, 78f, 752f, 1025f, 143f, Tipo.PINCELADA, 1.325f, 10.40f, 0),
    Pieza(R.drawable.splash_logo_07, 23f, 296f, 397f, 509f, Tipo.ABAJO, 0.925f, 4.51f, 7),
    Pieza(R.drawable.splash_logo_08, 221f, 301f, 397f, 489f, Tipo.ABAJO, 1.0f, -12.54f, 8),
    Pieza(R.drawable.splash_logo_09, 578f, 283f, 301f, 505f, Tipo.ABAJO, 1.075f, -3.74f, 9),
    Pieza(R.drawable.splash_logo_10, 743f, 305f, 440f, 511f, Tipo.ABAJO, 1.15f, 4.93f, 10),
    Pieza(R.drawable.splash_logo_00, 62f, 17f, 379f, 311f, Tipo.ARRIBA, 0.25f, -12.81f, 0),
    Pieza(R.drawable.splash_logo_01, 349f, 22f, 149f, 296f, Tipo.ARRIBA, 0.325f, 12.71f, 1),
    Pieza(R.drawable.splash_logo_02, 460f, 20f, 164f, 291f, Tipo.ARRIBA, 0.4f, 9.62f, 2),
    Pieza(R.drawable.splash_logo_03, 560f, 20f, 151f, 306f, Tipo.ARRIBA, 0.475f, 9.94f, 3),
    Pieza(R.drawable.splash_logo_04, 670f, 18f, 69f, 120f, Tipo.ARRIBA, 0.55f, -1.43f, 4),
    Pieza(R.drawable.splash_logo_05, 699f, 19f, 163f, 342f, Tipo.ARRIBA, 0.625f, 12.10f, 5),
    Pieza(R.drawable.splash_logo_06, 805f, 17f, 264f, 393f, Tipo.ARRIBA, 0.7f, -1.55f, 6),
    Pieza(R.drawable.splash_logo_13, 54f, 141f, 1095f, 408f, Tipo.CHISPAS, 1.525f, -10.88f, 0),
)

/** Tiempo en que termina de armarse el logo (la última pieza ya apareció). */
const val LOGO_ARMADO_SEGUNDOS = 1.9f

private fun easeOut(t: Float) = 1f - (1f - t).pow(3)
private fun back(t0: Float, overshoot: Float): Float {
    val t = t0 - 1f
    return t * t * ((overshoot + 1f) * t + overshoot) + 1f
}

/** Estado de una pieza en un instante: desplazamiento (en unidades del logo), giro y escala. */
private class Pose(
    var visible: Boolean = false,
    var dy: Float = 0f,
    var giro: Float = 0f,
    var escalaX: Float = 1f,
    var escalaY: Float = 1f,
    var impacto: Float = 0f,
)

private fun pose(p: Pieza, reloj: Float, ahora: Float): Pose {
    val local = reloj - p.retraso
    val r = Pose()
    if (local < 0f) return r
    r.visible = true
    when (p.tipo) {
        Tipo.ARRIBA, Tipo.ABAJO -> {
            if (local < CAIDA) {
                val k = local / CAIDA
                r.dy = ALTURA_CAIDA * (1f - k * k)
                r.giro = p.inclinacion * (1f - k)
                r.escalaX = 0.92f; r.escalaY = 1.12f
            } else {
                val desde = local - CAIDA
                val aplasta = exp(-desde * 9f) * cos(desde * 26f)
                r.dy = sin(ahora * 2.2f + p.indice * 0.7f) * 3f * (desde * 2f).coerceIn(0f, 1f)
                r.escalaX = 1f + 0.16f * aplasta
                r.escalaY = 1f - 0.2f * aplasta
                r.impacto = exp(-desde * 14f)
            }
        }
        Tipo.PINCELADA -> r.escalaX = easeOut((local / 0.3f).coerceIn(0f, 1f))
        Tipo.PUNTOS, Tipo.CHISPAS -> {
            val pop = back((local / 0.35f).coerceIn(0f, 1f), 2.4f)
            val pulso = if (p.tipo == Tipo.CHISPAS) 1f + 0.05f * sin(ahora * 7f) else 1f
            r.escalaX = pop * pulso; r.escalaY = pop * pulso
            r.giro = if (p.tipo == Tipo.PUNTOS) sin(ahora * 0.8f) * 2f else 0f
        }
    }
    return r
}

/**
 * Logo de MAKE 'EM FALL armándose pieza por pieza, como en la pantalla de título del juego.
 * Ocupa todo el ancho disponible con proporción 4:3.
 */
@Composable
fun LogoAnimado(modifier: Modifier = Modifier) {
    var reloj by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        val inicio = withFrameNanos { it }
        while (true) {
            withFrameNanos { t -> reloj = (t - inicio) / 1_000_000_000f }
        }
    }

    BoxWithConstraints(modifier.fillMaxWidth().aspectRatio(ANCHO / ALTO)) {
        val densidad = LocalDensity.current
        val escala = with(densidad) { maxWidth.toPx() } / ANCHO // px por unidad del logo
        val poses = PIEZAS.map { pose(it, reloj, reloj) }
        // Temblor del logo completo cuando aterrizan las letras (TitleMenu.AnimateLogo)
        val golpe = min(poses.sumOf { it.impacto.toDouble() }.toFloat(), 1.5f) * TEMBLOR * escala
        val angulo = Random.nextFloat() * 6.2832f
        val radio = Random.nextFloat() * golpe
        val temblor = IntOffset((cos(angulo) * radio).roundToInt(), (sin(angulo) * radio).roundToInt())

        Box(Modifier.offset { temblor }) {
            PIEZAS.forEachIndexed { i, p ->
                val pose = poses[i]
                if (!pose.visible) return@forEachIndexed
                val origen = when (p.tipo) {
                    Tipo.ARRIBA, Tipo.ABAJO -> TransformOrigin(0.5f, 1f)   // pivote abajo al centro
                    Tipo.PINCELADA -> TransformOrigin(0f, 0.5f)            // crece de izquierda a derecha
                    else -> TransformOrigin.Center
                }
                Image(
                    painter = painterResource(p.imagen),
                    contentDescription = null,
                    modifier = Modifier
                        .offset { IntOffset((p.x * escala).roundToInt(), (p.y * escala).roundToInt()) }
                        .size(with(densidad) { (p.w * escala).toDp() }, with(densidad) { (p.h * escala).toDp() })
                        .graphicsLayer {
                            transformOrigin = origen
                            translationY = -pose.dy * escala   // en Unity "arriba" es positivo
                            rotationZ = -pose.giro             // y el giro va al revés que en Compose
                            scaleX = pose.escalaX
                            scaleY = pose.escalaY
                        },
                )
            }
        }
    }
}
