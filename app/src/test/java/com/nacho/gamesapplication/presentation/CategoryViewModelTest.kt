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
    // Utiliza el runner de Mockito para ejecutar las pruebas
@RunWith(MockitoJUnitRunner::class)
class CategoryViewModelTest {

    // Permite la ejecución instantánea de tareas en pruebas de componentes LiveData
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Declaramos un mock de la interfaz IGamesRepository
    @Mock
    private lateinit var repository: IGamesRepository

    // Declaramos la variable viewModel que será inicializada en el setUp
    private lateinit var viewModel: CategoryViewModel

    // Creamos un dispatcher para ejecutar las coroutines de forma secuencial en las pruebas
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // Configura el dispatcher principal para que use el testDispatcher
        Dispatchers.setMain(testDispatcher)
        // Inicializamos el ViewModel con el mock del repositorio
        viewModel = CategoryViewModel(repository)
    }

    @After
    fun tearDown() {
        // Restablece el dispatcher principal después de cada prueba
        Dispatchers.resetMain()
    }

    // validamos que getGames con resultado exitoso actualiza el estado UI correctamente
    @Test
    fun `getGames with success result updates ui state correctly`() = runTest {
        // Creamos una lista de objetos Games con datos de prueba
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
            ),
            Games(
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

        // Creamos un flow que emite un Resources.Success con la lista de juegos
        val successResult = flow { emit(Resources.Success(games)) }

        // Configuramos el mock para que devuelva successResult cuando se llame a getGameByCategory con "action"
        `when`(repository.getGameByCategory("action")).thenReturn(successResult)

        // Llamamos al método getGames del ViewModel con la categoría "action"
        viewModel.getGames("action")

        // Avanzamos el dispatcher de prueba hasta que todas las coroutines estén inactivas
        advanceUntilIdle()

        // Obtenemos el valor actual del estado de la UI del ViewModel
        val uiState = viewModel.categoryScreenUiState.value
        // Verificamos que el estado de carga sea falso
        Assert.assertFalse(uiState.isLoading)
        // Verificamos que la lista de juegos en el estado de la UI sea igual a la lista de juegos de prueba
        Assert.assertEquals(games, uiState.games)
        // Verificamos que el mensaje de error en el estado de la UI sea nulo
        Assert.assertNull(uiState.errorMessage)
    }

    // validamos qye getGames con resultado de error, actualiza el estado de la UI correctamente
    @Test
    fun `getGames with error result updates ui state correctly`() = runTest {
        // Define un mensaje de error para la prueba
        val errorMessage = "An error occurred"
        // Creamos un flow que emite un Resources.Error con el mensaje de error
        val errorResult = flow { emit(Resources.Error<List<Games>>(errorMessage)) }

        // Configuramos el mock para que devuelva errorResult cuando se llame a getGameByCategory con "action"
        `when`(repository.getGameByCategory("action")).thenReturn(errorResult)

        // Llamamos al método getGames del ViewModel con la categoría "action"
        viewModel.getGames("action")

        // Avanza el dispatcher de prueba hasta que todas las coroutines estén inactivas
        advanceUntilIdle()

        // Obtenemos el valor actual del estado de la UI del ViewModel
        val uiState = viewModel.categoryScreenUiState.value
        // Verificamos que el estado de carga sea falso
        Assert.assertFalse(uiState.isLoading)
        // Verificamos que la lista de juegos en el estado de la UI esté vacía
        Assert.assertTrue(uiState.games.isEmpty())
        // Verificamos que el mensaje de error en el estado de la UI sea igual al mensaje de error de prueba
        Assert.assertEquals(errorMessage, uiState.errorMessage)
    }
}

