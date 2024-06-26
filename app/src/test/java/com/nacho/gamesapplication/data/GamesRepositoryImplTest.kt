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

    // Permite la ejecución instantánea de tareas en pruebas de LiveData
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Creamos un mock de la interfaz IGamesApi
    @Mock
    private lateinit var api: IGamesApi

    // Declaramos la variable repository que será inicializada en el setUp
    private lateinit var repository: GamesRepositoryImpl

    // Creamos un dispatcher para ejecutar las corrutinas de forma secuencial en las pruebas
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // Configuramos el dispatcher principal para que use el testDispatcher
        Dispatchers.setMain(testDispatcher)
        // Inicializamos el repositorio con el mock de la API
        repository = GamesRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        // Restablecemos el dispatcher principal después de cada prueba
        Dispatchers.resetMain()
    }

    // Verificamos que si la llamada a la api tiene éxito, getGameById debe devolver el game
    @Test
    fun `getGameById should return game when API call is successful`() = runTest {
        val gameId = 1
        // Creamos un objeto GameDto con datos de prueba
        val gameDto = GameDto(
            "description",
            "developer",
            "url",
            "url",
            "genre",
            gameId,
            null,
            "platform",
            "publisher",
            "date",
            emptyList(),
            "short",
            "status",
            "thumb",
            "title"
        )
        // Configuramos el mock para que devuelva gameDto cuando se llame a getGameById con gameId
        Mockito.`when`(api.getGameById(gameId)).thenReturn(gameDto)

        // Llamamos al método getGameById del repositorio y toma el primer valor emitido
        val result = repository.getGameById(gameId).first()

        // Verificamos que el resultado sea Success
        assert(result is Resources.Success)
        // Verificamos que el ID del juego en el resultado sea igual a gameId
        assertEquals(
            gameId,
            (result as Resources.Success).data?.id
        )
    }

    // Verificamos que si la llamada a la api tiene éxito, getAllGames debe devolver la lista de juegos
    @Test
    fun `getAllGames should return list of games when API call is successful`() = runTest {
        // Creamos un objeto GamesResponseDto con datos de prueba
        val gameDto = GamesResponseDto(
            "developer",
            "url",
            "url",
            "genre",
            1,
            "platform",
            "publisher",
            "date",
            "short",
            "thumb",
            "title"
        )

        // Creamos una lista con un solo elemento gameDto
        val gamesList = listOf(gameDto)

        // Configuramos el mock para que devuelva gamesList cuando se llame a getAllGames
        Mockito.`when`(api.getAllGames()).thenReturn(gamesList)

        // Llamamos al método getAllGames del repositorio y toma el primer valor emitido
        val result = repository.getAllGames().first()

        // Verifica que el resultado sea de tipo Resources.Success
        assert(result is Resources.Success)
        // Verifica que el tamaño de la lista en el resultado sea 1
        assertEquals(1, (result as Resources.Success).data?.size)
    }

    // Verificamos que si la llamada a la api tiene éxito, getGameByCategory debe devolver una lista de juegos
    @Test
    fun `getGameByCategory should return list of games when API call is successful`() = runTest {
        // Defimos una categoría para la prueba
        val category = "action"
        // Creamos un objeto GamesResponseDto con datos de prueba
        val gameDto = GamesResponseDto(
            "developer",
            "url",
            "url",
            "genre",
            1,
            "platform",
            "publisher",
            "date",
            "short",
            "thumb",
            "title"
        )

        // Creamos una lista con un solo elemento gameDto
        val gamesList = listOf(gameDto)
        // Configuramos el mock para que devuelva gamesList cuando se llame a getGameByCategory con una category especifica
        Mockito.`when`(api.getGameByCategory(category)).thenReturn(gamesList)

        // Llamamos al método getGameByCategory del repositorio y toma el primer valor emitido
        val result = repository.getGameByCategory(category).first()

        // Verificamos que el resultado sea de tipo Success
        assert(result is Resources.Success)
        // Verifica que el tamaño de la lista en el resultado sea 1
        assertEquals(1, (result as Resources.Success).data?.size)
    }

}
