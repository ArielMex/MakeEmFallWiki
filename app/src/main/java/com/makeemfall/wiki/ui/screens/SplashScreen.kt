package com.makeemfall.wiki.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makeemfall.wiki.ui.components.FondoWiki
import com.makeemfall.wiki.ui.components.LOGO_ARMADO_SEGUNDOS
import com.makeemfall.wiki.ui.components.LogoAnimado
import com.makeemfall.wiki.ui.components.TextoContorno
import com.makeemfall.wiki.ui.theme.AzulNoche
import com.makeemfall.wiki.ui.theme.BotonTexto
import kotlinx.coroutines.delay

/**
 * Portada / splash: el logo se arma letra por letra como en la pantalla de título del juego
 * y después aparece "WIKI".
 */
@Composable
fun SplashScreen(onTerminar: () -> Unit) {
    val wiki = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay((LOGO_ARMADO_SEGUNDOS * 1000).toLong())
        wiki.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow))
        delay(1300)
        onTerminar()
    }

    FondoWiki {
        // Oscurece un poco el fondo para que resalte el logo (como introShade en Unity)
        Box(Modifier.fillMaxSize().background(AzulNoche.copy(alpha = 0.45f)))
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LogoAnimado()
            Spacer(Modifier.height(4.dp))
            TextoContorno(
                "WIKI",
                MaterialTheme.typography.displayLarge.copy(fontSize = 56.sp),
                BotonTexto,
                modifier = Modifier
                    .alpha(wiki.value.coerceIn(0f, 1f))
                    .scale(wiki.value)
                    .rotate(-4f),
                grosor = 14f,
            )
        }
    }
}
