package com.makeemfall.wiki.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.makeemfall.wiki.data.model.Seccion
import com.makeemfall.wiki.ui.screens.DetalleScreen
import com.makeemfall.wiki.ui.screens.FavoritosScreen
import com.makeemfall.wiki.ui.screens.HomeScreen
import com.makeemfall.wiki.ui.screens.ListaScreen
import com.makeemfall.wiki.ui.screens.SplashScreen
import com.makeemfall.wiki.ui.viewmodel.WikiViewModel

object Rutas {
    const val SPLASH = "splash"
    const val INICIO = "inicio"
    const val LISTA = "lista/{seccion}"
    const val DETALLE = "detalle/{seccion}/{id}"
    const val FAVORITOS = "favoritos"

    fun lista(s: Seccion) = "lista/${s.ruta}"
    fun detalle(s: Seccion, id: String) = "detalle/${s.ruta}/$id"
}

@Composable
fun WikiNavGraph() {
    val nav = rememberNavController()
    // Un solo ViewModel para toda la app: el GET se hace una vez y se comparte.
    val vm: WikiViewModel = viewModel()

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(onTerminar = {
                nav.navigate(Rutas.INICIO) { popUpTo(Rutas.SPLASH) { inclusive = true } }
            })
        }
        composable(Rutas.INICIO) {
            HomeScreen(
                vm = vm,
                onSeccion = { nav.navigate(Rutas.lista(it)) },
                onFavoritos = { nav.navigate(Rutas.FAVORITOS) },
            )
        }
        composable(
            Rutas.LISTA,
            arguments = listOf(navArgument("seccion") { type = NavType.StringType }),
        ) { back ->
            val seccion = Seccion.desdeRuta(back.arguments?.getString("seccion"))
            ListaScreen(
                seccion = seccion,
                vm = vm,
                onEntrada = { nav.navigate(Rutas.detalle(it.seccion, it.id)) },
                onVolver = { nav.popBackStack() },
            )
        }
        composable(
            Rutas.DETALLE,
            arguments = listOf(
                navArgument("seccion") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType },
            ),
        ) { back ->
            DetalleScreen(
                seccion = Seccion.desdeRuta(back.arguments?.getString("seccion")),
                id = back.arguments?.getString("id").orEmpty(),
                vm = vm,
                onVolver = { nav.popBackStack() },
            )
        }
        composable(Rutas.FAVORITOS) {
            FavoritosScreen(
                vm = vm,
                onEntrada = { nav.navigate(Rutas.detalle(it.seccion, it.id)) },
                onVolver = { nav.popBackStack() },
            )
        }
    }
}
