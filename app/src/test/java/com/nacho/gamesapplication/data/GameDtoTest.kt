package com.nacho.gamesapplication.data

import com.nacho.gamesapplication.data.remote.dto.GameDto
import com.nacho.gamesapplication.data.remote.dto.MinimumSystemRequirementsDto
import com.nacho.gamesapplication.data.remote.dto.ScreenshotDto
import org.junit.Assert.*
import org.junit.Test
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class GameDtoTest {

    @Test
    fun `json serialization and deserialization should work correctly`() {
        // Creamos una instancia de Moshi, que es una biblioteca para trabajar con JSON
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())// Agregamos soporte para clases de Kotlin
            .build()

        // Creamos un adaptador JSON específico para la clase GameDto
        val jsonAdapter = moshi.adapter(GameDto::class.java)

        // Creamos una instancia de GameDto con datos de prueba
        val originalDto = GameDto(
            description = "Game Description",
            developer = "Developer",
            freetogameProfileUrl = "http://example.com/profile",
            gameUrl = "http://example.com/game",
            genre = "Action",
            id = 1,
            minimumSystemRequirements = MinimumSystemRequirementsDto(
                graphics = "Graphics Card",
                memory = "8 GB RAM",
                os = "Windows 10",
                processor = "Intel Core i5",
                storage = "1 TB HDD"
            ),
            platform = "PC",
            publisher = "Publisher",
            releaseDate = "2023-01-01",
            screenshots = listOf(ScreenshotDto(id = 1, image = "http://example.com/image.jpg")),
            shortDescription = "Short description",
            status = "Released",
            thumbnail = "http://example.com/thumbnail",
            title = "Game Title"
        )

        // Serializamos el objeto GameDto a un JSON
        val json = jsonAdapter.toJson(originalDto)
        // Deserializamos el JSON de vuelta a un objeto GameDto
        val recreatedDto = jsonAdapter.fromJson(json)

        // Verificamos que el objeto deserializado no sea nulo
        assertNotNull(recreatedDto)
        // Verificamos que el objeto original y el deserializado sean iguales
        assertEquals(originalDto, recreatedDto)
    }

}