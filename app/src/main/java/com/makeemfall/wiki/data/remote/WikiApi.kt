package com.makeemfall.wiki.data.remote

import com.makeemfall.wiki.data.model.WikiResponse
import retrofit2.http.GET

/** Endpoint de la API. Este es el método GET del CRUD. */
interface WikiApi {
    @GET("wiki.json")
    suspend fun obtenerWiki(): WikiResponse
}
