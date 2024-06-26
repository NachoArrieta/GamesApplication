package com.nacho.gamesapplication.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nacho.gamesapplication.domain.model.Games
import com.nacho.gamesapplication.domain.repository.IGamesRepository
import com.nacho.gamesapplication.presentation.categoryScreen.CategoryViewModel
import com.nacho.gamesapplication.utils.Resources
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CategoryViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: IGamesRepository

    private lateinit var viewModel: CategoryViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CategoryViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getGames with success result updates ui state correctly`() = runTest {
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
            ),
            Games(
                developer = "Developer 2",
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
            ), Games(
                developer = "Developer 3",
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

        `when`(repository.getGameByCategory("action")).thenReturn(successResult)

        viewModel.getGames("action")

        advanceUntilIdle()

        val uiState = viewModel.categoryScreenUiState.value
        Assert.assertFalse(uiState.isLoading)
        Assert.assertEquals(games, uiState.games)
        Assert.assertNull(uiState.errorMessage)
    }

    @Test
    fun `getGames with error result updates ui state correctly`() = runTest {
        val errorMessage = "An error occurred"
        val errorResult = flow { emit(Resources.Error<List<Games>>(errorMessage)) }

        `when`(repository.getGameByCategory("action")).thenReturn(errorResult)

        viewModel.getGames("action")

        advanceUntilIdle()

        val uiState = viewModel.categoryScreenUiState.value
        Assert.assertFalse(uiState.isLoading)
        Assert.assertTrue(uiState.games.isEmpty())
        Assert.assertEquals(errorMessage, uiState.errorMessage)
    }
}
