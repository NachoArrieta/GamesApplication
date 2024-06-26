package com.nacho.gamesapplication

import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.junit4.createAndroidComposeRule

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testSplashScreenDisplayedFirst() {
        composeTestRule.onNodeWithText("Games").assertIsDisplayed()
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