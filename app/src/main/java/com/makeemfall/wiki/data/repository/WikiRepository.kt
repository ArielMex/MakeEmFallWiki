package com.makeemfall.wiki.data.repository

import com.makeemfall.wiki.data.model.WikiResponse
import com.makeemfall.wiki.data.remote.RetrofitClient

class WikiRepository {

    /**
     * Hace el GET y regresa el resultado o el error.
     * Retrofit se crea aquí adentro para que, si la URL de la API está mal escrita,
     * la app muestre el error en pantalla en lugar de cerrarse.
     */
    suspend fun obtenerWiki(): Result<WikiResponse> = runCatching { RetrofitClient.api.obtenerWiki() }
}