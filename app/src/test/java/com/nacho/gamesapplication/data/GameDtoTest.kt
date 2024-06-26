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
        // Arrange
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val jsonAdapter = moshi.adapter(GameDto::class.java)

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

        // Act
        val json = jsonAdapter.toJson(originalDto)
        val recreatedDto = jsonAdapter.fromJson(json)

        // Assert
        assertNotNull(recreatedDto)
        assertEquals(originalDto, recreatedDto)
    }
}