package com.nacho.gamesapplication.di

import com.nacho.gamesapplication.data.repository.GamesRepositoryImpl
import com.nacho.gamesapplication.domain.repository.IGamesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGameRepository(gamesRepositoryImpl: GamesRepositoryImpl): IGamesRepository

}