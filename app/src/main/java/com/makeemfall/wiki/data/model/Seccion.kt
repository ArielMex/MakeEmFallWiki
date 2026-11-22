package com.makeemfall.wiki.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Botones del menú de la wiki (los del lado izquierdo en el diseño de Figma).
 * "ruta" se usa en la navegación y en las llaves de favoritos.
 */
enum class Seccion(
    val ruta: String,
    val titulo: String,
    val encabezado: String,
    val descripcion: String,
    val icono: ImageVector,
    /** Nombre del filtro que se muestra en la lista (null = sin filtros). */
    val nombreFiltro: String?,
    /** Imagen que acompaña al encabezado de la sección. */
    val imagenEncabezado: String,
) {
    HISTORIA("historia", "Historia", "La historia", "2060: la rebelión contra Nuddle", Icons.AutoMirrored.Filled.MenuBook, null, "personaje_niko"),
    PERSONAJES("personajes", "Personajes", "Personajes", "La resistencia y su enemigo", Icons.Filled.Person, "Rol", "personaje_mateo"),
    ENEMIGOS("enemigos", "Enemigos", "Enemigos", "Guardias, drones, élites y el jefe", Icons.Filled.SportsMma, "Tipo", "personaje_niko"),
    BOONS("boons", "Boons", "Boons", "Objetos de oficina que te hacen más fuerte", Icons.Filled.AutoAwesome, "Rareza", "boon_extintor"),
    MECANICAS("mecanicas", "Mecánicas", "Mecánicas", "Porcentaje, ventanas, pisos y Café Corp", Icons.Filled.Psychology, "Categoría", "personaje_mateo"),
    CONTROLES("controles", "Controles", "Controles", "Teclado y control", Icons.Filled.Gamepad, "Categoría", "personaje_niko");

    companion object {
        fun desdeRuta(ruta: String?): Seccion = entries.firstOrNull { it.ruta == ruta } ?: HISTORIA
    }
}
