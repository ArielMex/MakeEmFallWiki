package com.makeemfall.wiki.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.makeemfall.wiki.R
import com.makeemfall.wiki.ui.theme.AmarilloElectrico
import com.makeemfall.wiki.ui.theme.BotonTexto

/** Barra superior: flecha para volver, logo pequeño y "WIKI". */
@Composable
fun BarraWiki(onVolver: () -> Unit, acciones: @Composable RowScope.() -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = AmarilloElectrico)
        }
        Image(
            painterResource(R.drawable.logo_make_em_fall),
            contentDescription = "Make 'Em Fall",
            modifier = Modifier.height(44.dp),
        )
        Spacer(Modifier.width(6.dp))
        TextoContorno("WIKI", MaterialTheme.typography.titleLarge, BotonTexto, grosor = 6f)
        Spacer(Modifier.weight(1f))
        acciones()
    }
}
