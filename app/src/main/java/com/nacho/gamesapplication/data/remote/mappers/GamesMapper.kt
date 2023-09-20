package com.nacho.gamesapplication.data.remote.mappers

import com.nacho.gamesapplication.data.remote.dto.GameDto
import com.nacho.gamesapplication.data.remote.dto.GamesResponseDto
import com.nacho.gamesapplication.data.remote.dto.MinimumSystemRequirementsDto
import com.nacho.gamesapplication.data.remote.dto.ScreenshotDto
import com.nacho.gamesapplication.domain.model.Game
import com.nacho.gamesapplication.domain.model.Games
import com.nacho.gamesapplication.domain.model.MinimumSystemRequirements
import com.nacho.gamesapplication.domain.model.ScreenShot

fun GamesResponseDto.toGames(): Games {
    return Games(
        developer,
        freeToGameProfileUrl,
        gameUrl,
        genre,
        id,
        platform,
        publisher,
        releaseDate,
        shortDescription,
        thumbnail,
        title,
    )
}

fun ScreenshotDto.toScreenShots(): ScreenShot {
    return ScreenShot(id, image)
}

fun MinimumSystemRequirementsDto.toMinimumRequirements(): MinimumSystemRequirements {
    return MinimumSystemRequirements(graphics, memory, os, processor, storage)
}

fun GameDto.toGame(): Game {
    return Game(
        description = description,
        developer = developer,
        freetogameProfileUrl = freetogameProfileUrl,
        gameUrl = gameUrl,
        genre = genre,
        id = id,
        minimumSystemRequirements = minimumSystemRequirements?.toMinimumRequirements(),
        platform = platform,
        publisher = publisher,
        releaseDate = releaseDate,
        screenshots = screenshots.map { it.toScreenShots() },
        shortDescription = shortDescription,
        status = status,
        thumbnail = thumbnail,
        title = title
    )
}