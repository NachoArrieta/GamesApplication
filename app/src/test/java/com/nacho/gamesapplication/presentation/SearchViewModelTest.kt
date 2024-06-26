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

        // Indica que se están utilizando corrutinas en este archivo
@ExperimentalCoroutinesApi
        // Utilizazamos el runner de Mockito para ejecutar las pruebas
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

        // Permite la ejecución instantánea de tareas en pruebas de LiveData
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

        // Declaramos un mock de la interfaz IGamesRepository
    @Mock
    private lateinit var repository: IGamesRepository

        // Declaramos la variable viewModel que será inicializada en el setUp
    private lateinit var viewModel: SearchViewModel

        // Creamos un dispatcher para ejecutar las coroutines de forma secuencial en las pruebas
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // Configura el dispatcher principal para que use el testDispatcher
        Dispatchers.setMain(testDispatcher)
        // Inicializamos el ViewModel con el mock del repositorio
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        // Restablece el dispatcher principal después de cada prueba
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllGames with success result updates searchScreenState correctly`() = runTest {
        // Crea una lista de objetos Games con datos de prueba
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
            // Creamos un flow que emite un Resources.Success con la lista de juegos
        val successResult = flow { emit(Resources.Success(games)) }
            // Configura el mock para que devuelva successResult cuando se llame a getAllGames
        `when`(repository.getAllGames()).thenReturn(successResult)

            // Llama al método getAllGames del ViewModel
        viewModel.getAllGames()

            // Avanza el dispatcher de pruebas hasta que todas las tareas pendientes se completen
        advanceUntilIdle()

            // Obtenemos el valor actual del estado de la UI del ViewModel
        val uiState = viewModel.searchScreenState.value
            // Verificamos que el estado de carga sea falso
        assertFalse(uiState.isLoading)
            // Verificamos que la lista de juegos en el estado de la UI sea igual a la lista de juegos de prueba
        assertEquals(games, uiState.games)
            // Verificamos que el mensaje de error en el estado de la UI sea nulo
        assertNull(uiState.errorMessage)
    }

    @Test
    fun `getAllGames with error result updates searchScreenState correctly`() = runTest {
            // Definimos un mensaje de error para la prueba
        val errorMessage = "An error occurred"
            // Creamos un flow que emite un Resources.Error con el mensaje de error
        val errorResult = flow { emit(Resources.Error<List<Games>>(errorMessage)) }

            // Configuramos el mock para que devuelva errorResult cuando se llame a getAllGames
        `when`(repository.getAllGames()).thenReturn(errorResult)

            // Llamamos al método getAllGames del ViewModel
        viewModel.getAllGames()

            // Avanzamos el dispatcher de pruebas hasta que todas las tareas pendientes se completen
        advanceUntilIdle()

            // Obtiene el valor actual del estado de la UI del ViewModel
        val uiState = viewModel.searchScreenState.value
            // Verificamos que el estado de carga sea falso
        assertFalse(uiState.isLoading)
            // Verificamos que la lista de juegos en el estado de la UI esté vacía
        assertTrue(uiState.games.isEmpty())
            // Verificamos que el mensaje de error en el estado de la UI sea igual al mensaje de error de prueba
        assertEquals(errorMessage, uiState.errorMessage)
    }

    @Test
    fun `onEvent with OnValueChange updates searchScreenState correctly`() = runTest {
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
            )
        )
            // Creamos un flow que emite un Resources.Success con la lista de juegos
        val successResult = flow { emit(Resources.Success(games)) }

            // Configuramos el mock para que devuelva successResult cuando se llame a getAllGames
        `when`(repository.getAllGames()).thenReturn(successResult)

            // Llamamos al método getAllGames del ViewModel
        viewModel.getAllGames()

            // Avanza el dispatcher de pruebas hasta que todas las tareas pendientes se completen
        advanceUntilIdle()

            // Llamamos al método onEvent del ViewModel con el evento OnValueChange y el valor "title"
        viewModel.onEvent(SearchScreenEvents.OnValueChange("title"))

            // Avanzamos el tiempo en 500 milisegundos para permitir que el debounce tenga efecto
        advanceTimeBy(500)

            // Obtenemos el valor actual del estado de la UI del ViewModel
        val uiState = viewModel.searchScreenState.value
            // Verificamos que el estado de carga sea falso
        assertFalse(uiState.isLoading)
            // Verificamos que la lista de juegos en el estado de la UI contenga solo los juegos cuyo título contiene "title"
        assertEquals(games.filter { it.title.lowercase().contains("title") }, uiState.games)
            // Verificamos que el mensaje de error en el estado de la UI sea nulo
        assertNull(uiState.errorMessage)
    }

}