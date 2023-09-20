package com.nacho.gamesapplication.domain.repository

import com.nacho.gamesapplication.domain.model.Game
import com.nacho.gamesapplication.domain.model.Games
import com.nacho.gamesapplication.utils.Resources
import kotlinx.coroutines.flow.Flow

interface IGamesRepository {
    suspend fun getGameById(id: Int): Flow<Resources<Game>>
    suspend fun getAllGames(): Flow<Resources<List<Games>>>
    suspend fun getGameByCategory(category: String): Flow<Resources<List<Games>>>
}