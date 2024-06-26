package com.nacho.gamesapplication.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nacho.gamesapplication.domain.model.Game
import com.nacho.gamesapplication.domain.repository.IGamesRepository
import com.nacho.gamesapplication.presentation.gameScreen.GameViewModel
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
class GameViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: IGamesRepository

    private lateinit var viewModel: GameViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GameViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getGame with success result updates ui state correctly`() = runTest(testDispatcher) {
        val game = Game(
            description = "description1",
            developer = "developer1",
            freetogameProfileUrl = "url1",
            gameUrl = "url1",
            genre = "genre1",
            id = 1,
            minimumSystemRequirements = null,
            platform = "platform1",
            publisher = "publisher1",
            releaseDate = "2021-01-01",
            screenshots = emptyList(),
            shortDescription = "short description",
            status = "status1",
            thumbnail = "thumbnail1",
            title = "title1"
        )
        val successResult = flow { emit(Resources.Success(game)) }

        `when`(repository.getGameById(1)).thenReturn(successResult)

        viewModel.getGame(1)

        val uiState = viewModel.gameScreenState.value
        Assert.assertFalse(uiState.isLoading)
        Assert.assertEquals(game, uiState.data)
        Assert.assertNull(uiState.errorMessage)
    }

    @Test
    fun `getGame with error result updates ui state correctly`() = runTest(testDispatcher) {
        val errorMessage = "An error occurred"
        val errorResult = flow { emit(Resources.Error<Game>(errorMessage)) }

        `when`(repository.getGameById(1)).thenReturn(errorResult)

        viewModel.getGame(1)

        val uiState = viewModel.gameScreenState.value
        Assert.assertFalse(uiState.isLoading)
        Assert.assertNull(uiState.data)
        Assert.assertEquals(errorMessage, uiState.errorMessage)
    }
}
