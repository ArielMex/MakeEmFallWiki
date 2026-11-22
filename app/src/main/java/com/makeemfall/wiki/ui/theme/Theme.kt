package com.makeemfall.wiki.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EsquemaJuego = darkColorScheme(
    primary = RojoRebelion,
    onPrimary = CremaCalido,
    secondary = NaranjaFuego,
    onSecondary = NegroBerenjena,
    tertiary = AmarilloElectrico,
    onTertiary = NegroBerenjena,
    background = AzulNoche,
    onBackground = CremaCalido,
    surface = NegroBerenjena,
    onSurface = CremaCalido,
    surfaceVariant = VinoProfundo,
    onSurfaceVariant = CremaCalido,
    outline = CarmesiOscuro,
    error = RojoRebelion,
)

/** La app siempre usa el tema oscuro del juego. */
@Composable
fun MakeEmFallTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EsquemaJuego, typography = Typography, content = content)
}
