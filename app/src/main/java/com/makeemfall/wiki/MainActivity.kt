package com.makeemfall.wiki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier
import com.makeemfall.wiki.ui.navigation.WikiNavGraph
import com.makeemfall.wiki.ui.theme.AzulNoche
import com.makeemfall.wiki.ui.theme.MakeEmFallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MakeEmFallTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(AzulNoche)
                        .navigationBarsPadding()
                ) {
                    WikiNavGraph()
                }
            }
        }
    }
}
