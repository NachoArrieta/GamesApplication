package com.nacho.gamesapplication.data
import com.nacho.gamesapplication.data.remote.dto.GamesResponseDto
import com.nacho.gamesapplication.data.remote.mappers.toGames
import org.junit.Test
import org.junit.Assert.*

class GamesResponseDtoTest {

    @Test
    fun `toGames should convert GamesResponseDto to Games`() {
        // Given: Configuramos el entorno de la prueba inicializando los datos necesarios, asignando
        // el valor a cada uno de los campos
        val dto = GamesResponseDto(
            developer = "Developer",
            freeToGameProfileUrl = "http://example.com",
            gameUrl = "http://example.com/game",
            genre = "Action",
            id = 1,
            platform = "PC",
            publisher = "Publisher",
            releaseDate = "2023-01-01",
            shortDescription = "Short description",
            thumbnail = "http://example.com/thumbnail",
            title = "Game Title"
        )

        // When: Ejecuta la acción que se desea probar
        // Llamamos al método toGames() del objeto dto y almacenamos el resultado en la variable result
        val result = dto.toGames()

        // Then: Verifica los resultados de la acción probada
        assertEquals("Developer", result.developer)
        assertEquals("http://example.com", result.freetoGameProfileUrl)
        assertEquals("http://example.com/game", result.gameUrl)
        assertEquals("Action", result.genre)
        assertEquals(1, result.id)
        assertEquals("PC", result.platform)
        assertEquals("Publisher", result.publisher)
        assertEquals("2023-01-01", result.releaseDate)
        assertEquals("Short description", result.shortDescription)
        assertEquals("http://example.com/thumbnail", result.thumbnail)
        assertEquals("Game Title", result.title)
    }
}