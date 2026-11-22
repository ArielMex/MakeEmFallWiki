package com.makeemfall.wiki.data.model

/**
 * Forma común para mostrar cualquier elemento de la wiki con las mismas pantallas.
 */
data class WikiEntry(
    val id: String,
    val seccion: Seccion,
    val titulo: String,
    val subtitulo: String,
    val descripcion: String,
    /** Categoría que se usa para los filtros (tipo de boon, tipo de enemigo…). */
    val etiqueta: String,
    /** Nombre de la imagen local (res/drawable-nodpi). */
    val imagen: String? = null,
    /** Imagen grande para el detalle (por ejemplo, el póster del personaje). */
    val imagenGrande: String? = null,
    val imagenUrl: String? = null,
    /** Datos que se muestran en el detalle: "Estilo" to "Puños", etc. */
    val detalles: List<Pair<String, String>> = emptyList(),
    val lista: List<String> = emptyList(),
    /** Imágenes en pixel art: se dibujan sin suavizado para que se vean nítidas. */
    val pixelado: Boolean = false,
) {
    /** Llave única para guardar favoritos: "boons:extintor". */
    val key: String get() = "${seccion.ruta}:$id"
}

private fun String.siHay(nombre: String) = takeIf { it.isNotBlank() }?.let { nombre to it }

fun WikiResponse.entradas(seccion: Seccion): List<WikiEntry> = when (seccion) {
    Seccion.HISTORIA -> historia.map {
        WikiEntry(
            id = it.id, seccion = seccion, titulo = it.titulo, subtitulo = "",
            descripcion = it.texto, etiqueta = "", imagen = it.imagen, imagenUrl = it.imagenUrl,
        )
    }
    Seccion.PERSONAJES -> personajes.map {
        WikiEntry(
            id = it.id, seccion = seccion, titulo = it.nombre,
            subtitulo = listOf(it.rol, it.estiloPelea).filter(String::isNotBlank).joinToString(" · "),
            descripcion = it.descripcion,
            etiqueta = it.rol.substringBefore(" ·"),
            imagen = it.imagen, imagenGrande = it.imagenPanel, imagenUrl = it.imagenUrl,
            detalles = listOfNotNull(
                it.rol.siHay("Rol"),
                it.estiloPelea.siHay("Pelea con"),
                it.edad?.let { e -> "Edad" to "$e años" },
                it.peso?.let { p -> "Peso" to "$p" },
                it.velocidad?.let { v -> "Velocidad" to "$v" },
                it.vida?.let { v -> "Vida" to "$v puntos" },
                it.apariencia.siHay("Apariencia"),
            ),
            lista = it.habilidades,
            pixelado = it.id == "craub",
        )
    }
    Seccion.ENEMIGOS -> enemigos.map {
        WikiEntry(
            id = it.id, seccion = seccion, titulo = it.nombre,
            subtitulo = listOf(it.tipo, it.piso).filter(String::isNotBlank).joinToString(" · "),
            descripcion = it.descripcion, etiqueta = it.tipo,
            imagen = it.imagen, imagenUrl = it.imagenUrl,
            detalles = listOfNotNull(
                it.tipo.siHay("Tipo"),
                it.piso.siHay("Aparece desde"),
                it.vida?.let { v -> "Vida" to "$v puntos" },
            ),
            lista = it.habilidades,
        )
    }
    Seccion.BOONS -> boons.map {
        WikiEntry(
            id = it.id, seccion = seccion, titulo = it.nombre,
            subtitulo = listOf(it.rareza, it.elemento).filter(String::isNotBlank).joinToString(" · "),
            descripcion = it.descripcion, etiqueta = it.rareza,
            imagen = it.imagen, imagenUrl = it.imagenUrl,
            detalles = listOfNotNull(
                it.rareza.siHay("Rareza"),
                it.tipo.siHay("Tipo"),
                it.elemento.siHay("Elemento"),
                it.nivelMaximo.siHay("Nivel 5 (máx.)"),
            ),
            pixelado = true,
        )
    }
    Seccion.MECANICAS -> mecanicas.map {
        WikiEntry(
            id = it.id, seccion = seccion, titulo = it.nombre,
            subtitulo = it.categoria, descripcion = it.descripcion,
            etiqueta = it.categoria, imagen = it.imagen, imagenUrl = it.imagenUrl,
        )
    }
    Seccion.CONTROLES -> controles.map {
        WikiEntry(
            id = it.id, seccion = seccion, titulo = it.accion,
            subtitulo = "Teclado: ${it.teclado}   ·   Control: ${it.mando}",
            descripcion = it.descripcion, etiqueta = it.categoria,
            detalles = listOfNotNull(
                it.teclado.siHay("Teclado"),
                it.mando.siHay("Control"),
                it.categoria.siHay("Categoría"),
            ),
        )
    }
}

fun WikiResponse.todasLasEntradas(): List<WikiEntry> =
    Seccion.entries.flatMap { entradas(it) }
