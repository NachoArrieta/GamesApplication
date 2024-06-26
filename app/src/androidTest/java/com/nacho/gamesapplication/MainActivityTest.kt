package com.nacho.gamesapplication

import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.junit4.createAndroidComposeRule

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    // Creamos una regla de ComposeTestRule, que permite interactuar con la UI de Compose
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testSplashScreenDisplayedFirst() {
        // Buscamos un nodo con el texto "Games" y verifica que está visible en la pantalla
        composeTestRule.onNodeWithText("Games").assertIsDisplayed()

        // Buscamos un nodo con el texto "Application" y verifica que está visible en la pantalla
        composeTestRule.onNodeWithText("Application").assertIsDisplayed()
    }
    @Test
    fun testNavigationToHomeScreen() {
        // Avanza el tiempo para simular la espera de 6 segundos
        composeTestRule.mainClock.advanceTimeBy(6000L)

        // Verifica que el texto de la pantalla de inicio (por ejemplo, "Games Application") se muestra
        composeTestRule.onNodeWithText("Games Application").assertIsDisplayed()
    }
}