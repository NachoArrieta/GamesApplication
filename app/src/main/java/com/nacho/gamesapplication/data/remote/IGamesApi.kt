package com.nacho.gamesapplication.data.remote

import com.nacho.gamesapplication.data.remote.dto.GameDto
import com.nacho.gamesapplication.data.remote.dto.GamesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface IGamesApi {

    @GET("games")
    suspend fun getAllGames(): List<GamesResponseDto>

    @GET("game")
    suspend fun getGameById(@Query("id") id: Int): GameDto

    @GET("games")
    suspend fun getGameByCategory(@Query("category") category: String): List<GamesResponseDto>

}