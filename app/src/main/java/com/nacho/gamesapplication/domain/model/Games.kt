package com.nacho.gamesapplication.domain.model

data class Games(
    val developer: String,
    val freetoGameProfileUrl: String,
    val gameUrl: String,
    val genre: String,
    val id: Int,
    val platform: String,
    val publisher: String,
    val releaseDate: String,
    val shortDescription: String,
    val thumbnail: String,
    val title: String
)