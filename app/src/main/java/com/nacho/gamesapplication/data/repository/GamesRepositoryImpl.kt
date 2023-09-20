package com.nacho.gamesapplication.data.repository

import com.nacho.gamesapplication.data.remote.IGamesApi
import com.nacho.gamesapplication.data.remote.mappers.toGame
import com.nacho.gamesapplication.data.remote.mappers.toGames
import com.nacho.gamesapplication.domain.model.Game
import com.nacho.gamesapplication.domain.model.Games
import com.nacho.gamesapplication.domain.repository.IGamesRepository
import com.nacho.gamesapplication.utils.Resources
import com.nacho.gamesapplication.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(private val api: IGamesApi) : IGamesRepository {

    override suspend fun getGameById(id: Int): Flow<Resources<Game>> {
        return flowOf(safeApiCall(Dispatchers.IO) {
            api.getGameById(id).toGame()
        })
    }

    override suspend fun getAllGames(): Flow<Resources<List<Games>>> {
        return flowOf(
            safeApiCall(Dispatchers.IO) {
                api.getAllGames().map {
                    it.toGames()
                }
            }
        )
    }

    override suspend fun getGameByCategory(category: String): Flow<Resources<List<Games>>> {
        return flowOf(
            safeApiCall(Dispatchers.IO) {
                api.getGameByCategory(category).map { it.toGames() }
            }
        )
    }


}