package com.nacho.gamesapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.nacho.gamesapplication.presentation.NavGraphs
import com.nacho.gamesapplication.ui.theme.GamesApplicationTheme
import com.nacho.gamesapplication.ui.theme.montserrat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(this.window, false)

        setContent {
            GamesApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {

                    var splashVisible by remember { mutableStateOf(true) }
                    LaunchedEffect(Unit) {
                        delay(6000)
                        splashVisible = false
                    }

                    if (splashVisible) {
                        SplashScreen()
                    } else {
                        val navHostEngine = rememberAnimatedNavHostEngine(
                            navHostContentAlignment = Alignment.TopCenter,
                            rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING,
                            defaultAnimationsForNestedNavGraph = mapOf(),
                        )
                        DestinationsNavHost(navGraph = NavGraphs.root, engine = navHostEngine)
                    }

                }
            }
        }
    }

    @Composable
    fun SplashScreen() {
        val scaleAnim = rememberInfiniteTransition()

        val scale by scaleAnim.animateFloat(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1800,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Games",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = montserrat,
                    fontSize = 42.sp
                ),
                modifier = Modifier
                    .offset(y = (-40).dp)
                    .graphicsLayer(alpha = if (scale < 1) scale else 1f)
                    .padding(bottom = 220.dp)
            )


            Image(
                painter = painterResource(id = R.drawable.ic_dualshock),
                contentDescription = null,
                modifier = Modifier
                    .scale(scale)
                    .padding(16.dp)
            )

            Text(
                text = "Application",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = montserrat,
                    fontSize = 42.sp
                ),
                modifier = Modifier
                    .offset(y = (40).dp)
                    .padding(top = 220.dp)
                    .graphicsLayer(alpha = if (scale < 1) scale else 1f)
            )
        }
    }

}

