package com.nacho.gamesapplication.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nacho.gamesapplication.domain.model.Games
import com.nacho.gamesapplication.domain.repository.IGamesRepository
import com.nacho.gamesapplication.presentation.searchScreen.SearchScreenEvents
import com.nacho.gamesapplication.presentation.searchScreen.SearchViewModel
import com.nacho.gamesapplication.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
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
class SearchViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: IGamesRepository

    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllGames with success result updates searchScreenState correctly`() = runTest {
        val games = listOf(
            Games(
                developer = "Developer 1",
                freetoGameProfileUrl = "url1",
                gameUrl = "url1",
                genre = "genre1",
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

        advanceUntilIdle()

        val uiState = viewModel.searchScreenState.value
        assertFalse(uiState.isLoading)
        assertEquals(games, uiState.games)
        assertNull(uiState.errorMessage)
    }

    @Test
    fun `getAllGames with error result updates searchScreenState correctly`() = runTest {
        val errorMessage = "An error occurred"
        val errorResult = flow { emit(Resources.Error<List<Games>>(errorMessage)) }

        `when`(repository.getAllGames()).thenReturn(errorResult)

        viewModel.getAllGames()

        advanceUntilIdle()

        val uiState = viewModel.searchScreenState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.games.isEmpty())
        assertEquals(errorMessage, uiState.errorMessage)
    }

    @Test
    fun `onEvent with OnValueChange updates searchScreenState correctly`() = runTest {
        val games = listOf(
            Games(
                developer = "Developer 1",
                freetoGameProfileUrl = "url1",
                gameUrl = "url1",
                genre = "genre1",
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

        advanceUntilIdle()

        viewModel.onEvent(SearchScreenEvents.OnValueChange("title"))

        advanceTimeBy(500)

        val uiState = viewModel.searchScreenState.value
        assertFalse(uiState.isLoading)
        assertEquals(games.filter { it.title.lowercase().contains("title") }, uiState.games)
        assertNull(uiState.errorMessage)
    }
}
