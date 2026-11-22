package com.makeemfall.wiki.data.model

import kotlinx.serialization.Serializable

/**
 * Respuesta completa del GET a la API (archivo wiki.json).
 * Todos los campos tienen valor por defecto para que la app no truene
 * si a la API le falta alguna sección o algún dato.
 *
 * "imagen" es el nombre de una imagen guardada en la app (res/drawable-nodpi),
 * por ejemplo "boon_extintor". "imagenUrl" es opcional, para usar una imagen de internet.
 */
@Serializable
data class WikiResponse(
    val historia: List<Historia> = emptyList(),
    val personajes: List<Personaje> = emptyList(),
    val enemigos: List<Enemigo> = emptyList(),
    val boons: List<Boon> = emptyList(),
    val mecanicas: List<Mecanica> = emptyList(),
    val controles: List<Control> = emptyList(),
)

@Serializable
data class Historia(
    val id: String,
    val titulo: String,
    val texto: String = "",
    val imagen: String? = null,
    val imagenUrl: String? = null,
)

@Serializable
data class Personaje(
    val id: String,
    val nombre: String,
    val rol: String = "",
    val estiloPelea: String = "",
    val descripcion: String = "",
    val apariencia: String = "",
    val edad: Int? = null,
    /** Peso del personaje en el juego (entre más pesado, más difícil de sacar volando). */
    val peso: Int? = null,
    /** Velocidad máxima al correr. */
    val velocidad: Float? = null,
    /** Puntos de vida (solo el jefe tiene barra de vida). */
    val vida: Int? = null,
    val habilidades: List<String> = emptyList(),
    val imagen: String? = null,
    val imagenPanel: String? = null,
    val imagenUrl: String? = null,
)

@Serializable
data class Enemigo(
    val id: String,
    val nombre: String,
    val tipo: String = "",
    val descripcion: String = "",
    val vida: Int? = null,
    val piso: String = "",
    val habilidades: List<String> = emptyList(),
    val imagen: String? = null,
    val imagenUrl: String? = null,
)

@Serializable
data class Boon(
    val id: String,
    val nombre: String,
    val objeto: String = "",
    val tipo: String = "",
    val elemento: String = "",
    val rareza: String = "",
    /** Efecto en el nivel 1. */
    val descripcion: String = "",
    /** Efecto en el nivel máximo (5). */
    val nivelMaximo: String = "",
    val imagen: String? = null,
    val imagenUrl: String? = null,
)

@Serializable
data class Mecanica(
    val id: String,
    val nombre: String,
    val categoria: String = "",
    val descripcion: String = "",
    val imagen: String? = null,
    val imagenUrl: String? = null,
)

@Serializable
data class Control(
    val id: String,
    val accion: String,
    val teclado: String = "",
    val mando: String = "",
    val categoria: String = "",
    val descripcion: String = "",
)
