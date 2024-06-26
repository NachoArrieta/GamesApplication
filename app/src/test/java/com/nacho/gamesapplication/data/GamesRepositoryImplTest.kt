package com.nacho.gamesapplication.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nacho.gamesapplication.data.remote.IGamesApi
import com.nacho.gamesapplication.data.remote.dto.GameDto
import com.nacho.gamesapplication.data.remote.dto.GamesResponseDto
import com.nacho.gamesapplication.data.repository.GamesRepositoryImpl
import com.nacho.gamesapplication.utils.Resources
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GamesRepositoryImplTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: IGamesApi

    private lateinit var repository: GamesRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = GamesRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getGameById should return game when API call is successful`() = runTest {
        val gameId = 1
        val gameDto = GameDto("description", "developer", "url", "url", "genre", gameId, null, "platform", "publisher", "date", emptyList(), "short", "status", "thumb", "title")
        Mockito.`when`(api.getGameById(gameId)).thenReturn(gameDto)

        val result = repository.getGameById(gameId).first()

        assert(result is Resources.Success)
        assertEquals(gameId, (result as Resources.Success).data?.id)
    }

    @Test
    fun `getAllGames should return list of games when API call is successful`() = runTest {
        // Arrange
        val gameDto = GamesResponseDto("developer", "url", "url", "genre", 1, "platform", "publisher", "date", "short", "thumb", "title")
        val gamesList = listOf(gameDto)
        Mockito.`when`(api.getAllGames()).thenReturn(gamesList)

        val result = repository.getAllGames().first()

        assert(result is Resources.Success)
        assertEquals(1, (result as Resources.Success).data?.size)
    }

    @Test
    fun `getGameByCategory should return list of games when API call is successful`() = runTest {
        val category = "action"
        val gameDto = GamesResponseDto("developer", "url", "url", "genre", 1, "platform", "publisher", "date", "short", "thumb", "title")
        val gamesList = listOf(gameDto)
        Mockito.`when`(api.getGameByCategory(category)).thenReturn(gamesList)

        val result = repository.getGameByCategory(category).first()

        assert(result is Resources.Success)
        assertEquals(1, (result as Resources.Success).data?.size)
    }
}