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

    // Indica que se están utilizando corrutinas en este archivo
@ExperimentalCoroutinesApi
    // Utiliza el runner de Mockito para ejecutar las pruebas
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    // Permite la ejecución instantánea de tareas de LiveData
    @get:Rule
    var instantTaskExecutorRule =
        InstantTaskExecutorRule()

    // Declaramos un mock de la interfaz IGamesRepository
    @Mock
    private lateinit var repository: IGamesRepository

    // Declaramos la variable viewModel que será inicializada en el setUp
    private lateinit var viewModel: HomeViewModel

    // Crea un dispatcher para ejecutar las coroutines de forma controlada en las pruebas
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        // Configura el dispatcher principal para que use el testDispatcher
        Dispatchers.setMain(testDispatcher)
        // Inicializa el ViewModel con el mock del repositorio
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        // Restablece el dispatcher principal después de cada prueba
        Dispatchers.resetMain()
        // Limpia las coroutines del testDispatcher
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `getAllGames with success result updates ui state correctly`() =
            // Permite ejecutar bloques de código suspendido en una prueba de manera sincrónica
        testDispatcher.runBlockingTest {
            // Creamos una lista de objetos Games con datos de prueba
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
            // Creamos un flow que emite un Resources.Success con la lista de juegos
            val successResult = flow { emit(Resources.Success(games)) }

            // Configuramos el mock para que devuelva successResult cuando se llame a getAllGames
            `when`(repository.getAllGames()).thenReturn(successResult)

            // Llamamos al método getAllGames del ViewModel
            viewModel.getAllGames()

            // Avanzamos el dispatcher de pruebas hasta que todas las tareas pendientes se completen
            advanceUntilIdle()

            // Obtenemos el valor actual del estado de la UI del ViewModel
            val uiState = viewModel.homeScreenState.value
            // Verificamos que el estado de carga sea falso
            assertFalse(uiState.isLoading)
            // Verificamos que la lista de juegos en el estado de la UI contenga solo los juegos cuyo género es "shooter"
            assertEquals(games.filter { it.genre.lowercase() == "shooter" }, uiState.shooterGames)
            // Verificamos que la lista de juegos en el estado de la UI contenga solo los juegos cuyo género es "racing"
            assertEquals(games.filter { it.genre.lowercase() == "racing" }, uiState.racingGames)
            // Verificamos que la lista de juegos en el estado de la UI contenga solo los juegos cuyo género es "sports"
            assertEquals(games.filter { it.genre.lowercase() == "sports" }, uiState.sportsGames)
            // Verificamos que la lista de juegos en el estado de la UI contenga solo los juegos cuyo género es "fighting"
            assertEquals(games.filter { it.genre.lowercase() == "fighting" }, uiState.fightingGames)
            // Verificamos que el mensaje de error en el estado de la UI sea nulo
            assertNull(uiState.errorMessage)
        }

    @Test
    fun `getAllGames with error result updates ui state correctly`() =
            // Permite ejecutar bloques de código suspendido en una prueba de manera sincrónica
        testDispatcher.runBlockingTest {
            // Definimos un mensaje de error para la prueba
            val errorMessage = "An error occurred"
            // Creamos un flow que emite un Resources.Error con el mensaje de error
            val errorResult = flow { emit(Resources.Error<List<Games>>(errorMessage)) }

            // Configuramos el mock para que devuelva errorResult cuando se llame a getAllGames
            `when`(repository.getAllGames()).thenReturn(errorResult)

            // Llamamos al método getAllGames del ViewModel
            viewModel.getAllGames()

            // Avanza el dispatcher de pruebas hasta que todas las tareas pendientes se completen
            advanceUntilIdle()

            // Obtenemos el valor actual del estado de la UI del ViewModel
            val uiState = viewModel.homeScreenState.value
            // Verificamos que el estado de carga sea falso
            assertFalse(uiState.isLoading)
            // Verificamos que la lista de juegos de género "shooter" esté vacía
            assertTrue(uiState.shooterGames.isEmpty())
            // Verificamos que la lista de juegos de género "racing" esté vacía
            assertTrue(uiState.racingGames.isEmpty())
            // Verificamos que la lista de juegos de género "sports" esté vacía
            assertTrue(uiState.sportsGames.isEmpty())
            // Verificamos que la lista de juegos de género "fighting" esté vacía
            assertTrue(uiState.fightingGames.isEmpty())
            // Verificamos que el mensaje de error en el estado de la UI sea igual al mensaje de error de prueba
            assertEquals(errorMessage, uiState.errorMessage)
        }
        
}
