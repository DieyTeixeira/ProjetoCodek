package com.codek.projetocodek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.codek.projetocodek.features.dateSet.DateScreen2
import com.codek.projetocodek.features.screens.CaronaScreen
import com.codek.projetocodek.features.screens.EventosScreen
import com.codek.projetocodek.features.screens.TripScreen
import com.codek.projetocodek.ui.theme.CodekTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodekTheme {
                App()
            }
        }
    }
}

class BottomAppBarItem(
    val iconVector: ImageVector? = null,
    val iconPainter: Painter? = null,
    val label: String
)

sealed class ScreenItem(
    val bottomAppBarItem: BottomAppBarItem
) {
    data object Eventos : ScreenItem(
        bottomAppBarItem = BottomAppBarItem(
            iconVector = Icons.Default.Nightlife,
            label = "Eventos"
        )
    )
    data object Carona : ScreenItem(
        bottomAppBarItem = BottomAppBarItem(
            iconVector = Icons.Default.DirectionsCar,
            label = "Carona"
        )
    )
    data object Trip : ScreenItem(
        bottomAppBarItem = BottomAppBarItem(
            iconVector = Icons.Default.Map,
            label = "Trip"
        )
    )
    data object Data : ScreenItem(
        bottomAppBarItem = BottomAppBarItem(
            iconVector = Icons.Default.CalendarMonth,
            label = "Data"
        )
    )
}

@Composable
fun App() {
    val screens = remember {
        listOf(
            ScreenItem.Eventos,
            ScreenItem.Carona,
            ScreenItem.Trip,
            ScreenItem.Data
        )
    }
    var currentScreen by remember {
        mutableStateOf(screens.first())
    }
    val pagerState = rememberPagerState {
        screens.size
    }
    LaunchedEffect(currentScreen) {
        pagerState.animateScrollToPage(
            screens.indexOf(currentScreen)
        )
    }
    LaunchedEffect(pagerState.targetPage, screens) {
        currentScreen = screens[pagerState.targetPage]
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar {
                screens.forEach { screen ->
                    with(screen.bottomAppBarItem) {
                        NavigationBarItem(
                            selected = screen == currentScreen,
                            onClick = { currentScreen = screen },
                            icon = {
                                if (iconPainter != null) {
                                    Icon(painter = iconPainter, contentDescription = null)
                                } else if (iconVector != null) {
                                    Icon(imageVector = iconVector, contentDescription = null)
                                }
                            },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            pagerState,
            Modifier.padding(innerPadding)
        ) { page ->
            val item = screens[page]
            when(item) {
                ScreenItem.Eventos -> EventosScreen()
                ScreenItem.Carona -> CaronaScreen()
                ScreenItem.Trip -> TripScreen()
                ScreenItem.Data -> DateScreen2()
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    CodekTheme{
        App()
    }
}