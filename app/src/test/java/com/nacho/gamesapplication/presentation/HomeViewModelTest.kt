package com.nacho.gamesapplication.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nacho.gamesapplication.domain.model.Games
import com.nacho.gamesapplication.domain.repository.IGamesRepository
import com.nacho.gamesapplication.presentation.homeScreen.HomeViewModel
import com.nacho.gamesapplication.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: IGamesRepository

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `getAllGames with success result updates ui state correctly`() = testDispatcher.runBlockingTest{
        val games = listOf(
            Games(
                developer = "Developer 1",
                freetoGameProfileUrl = "url1",
                gameUrl = "url1",
                genre = "shooter",
                id = 1,
                platform = "platform1",
                publisher = "publisher1",
                releaseDate = "2021-01-01",
                shortDescription = "description1",
                thumbnail = "thumbnail1",
                title = "title1"
            )
        )
        val successResult = flow { emit(Resources.Success(games)) }

        `when`(repository.getAllGames()).thenReturn(successResult)

        viewModel.getAllGames()

        // Espera para asegurar que las coroutines se completen
        advanceUntilIdle()

        val uiState = viewModel.homeScreenState.value
        assertFalse(uiState.isLoading)
        assertEquals(games.filter { it.genre.lowercase() == "shooter" }, uiState.shooterGames)
        assertEquals(games.filter { it.genre.lowercase() == "racing" }, uiState.racingGames)
        assertEquals(games.filter { it.genre.lowercase() == "sports" }, uiState.sportsGames)
        assertEquals(games.filter { it.genre.lowercase() == "fighting" }, uiState.fightingGames)
        assertNull(uiState.errorMessage)
    }

    @Test
    fun `getAllGames with error result updates ui state correctly`() = testDispatcher.runBlockingTest {
        val errorMessage = "An error occurred"
        val errorResult = flow { emit(Resources.Error<List<Games>>(errorMessage)) }

        `when`(repository.getAllGames()).thenReturn(errorResult)

        viewModel.getAllGames()

        advanceUntilIdle()

        val uiState = viewModel.homeScreenState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.shooterGames.isEmpty())
        assertTrue(uiState.racingGames.isEmpty())
        assertTrue(uiState.sportsGames.isEmpty())
        assertTrue(uiState.fightingGames.isEmpty())
        assertEquals(errorMessage, uiState.errorMessage)
    }

}
