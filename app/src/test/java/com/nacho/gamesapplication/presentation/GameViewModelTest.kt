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

// Indica que se están utilizando corrutinas en este archivo
@ExperimentalCoroutinesApi
    // Utiliza el runner de Mockito para ejecutar las pruebas
@RunWith(MockitoJUnitRunner::class)
class GameViewModelTest {

    // Permite la ejecución instantánea de tareas en pruebas de componentes LiveData
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Declara un mock de la interfaz IGamesRepository
    @Mock
    private lateinit var repository: IGamesRepository

    // Declara la variable viewModel que será inicializada en el setUp
    private lateinit var viewModel: GameViewModel

    // Crea un dispatcher para ejecutar las coroutines de forma secuencial en las pruebas
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // Configura el dispatcher principal para que use el testDispatcher
        Dispatchers.setMain(testDispatcher)
        // Inicializa el ViewModel con el mock del repositorio
        viewModel = GameViewModel(repository)
    }

    @After
    fun tearDown() {
        // Restablece el dispatcher principal después de cada prueba
        Dispatchers.resetMain()
    }

    @Test
    fun `getGame with success result updates ui state correctly`() = runTest(testDispatcher) {
        //  Creamos un objeto Game con datos de prueba
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

        // Creamos un flow que emite un Resources.Success con el objeto game
        val successResult = flow { emit(Resources.Success(game)) }

        // Configuramos el mock para que devuelva successResult cuando se llame a getGameById con 1
        `when`(repository.getGameById(1)).thenReturn(successResult)

        // Llamamos al método getGame del ViewModel con el ID 1
        viewModel.getGame(1)

        // Obtenemos el valor actual del estado de la UI del ViewModel
        val uiState = viewModel.gameScreenState.value
        // Verificamos que el estado de carga sea falso
        Assert.assertFalse(uiState.isLoading)
        // Verificamos que el dato en el estado de la UI sea igual al objeto game de prueba
        Assert.assertEquals(game, uiState.data)
        // Verificamos que el mensaje de error en el estado de la UI sea nulo
        Assert.assertNull(uiState.errorMessage)
    }

    @Test
    fun `getGame with error result updates ui state correctly`() = runTest(testDispatcher) {
        // Definimos un mensaje de error para la prueba
        val errorMessage = "An error occurred"
        // Creamos un flow que emite un Resources.Error con el mensaje de error
        val errorResult = flow { emit(Resources.Error<Game>(errorMessage)) }

        // Configuramos el mock para que devuelva errorResult cuando se llame a getGameById con 1
        `when`(repository.getGameById(1)).thenReturn(errorResult)

        // Llamamos al método getGame del ViewModel con el ID 1
        viewModel.getGame(1)

        // Obtenemos el valor actual del estado de la UI del ViewModel
        val uiState = viewModel.gameScreenState.value
        // Verificamos que el estado de carga sea falso
        Assert.assertFalse(uiState.isLoading)
        // Verificamos que el dato en el estado de la UI sea nulo
        Assert.assertNull(uiState.data)
        // Verificamos que el mensaje de error en el estado de la UI sea igual al mensaje de error de prueba
        Assert.assertEquals(errorMessage, uiState.errorMessage)
    }
}

