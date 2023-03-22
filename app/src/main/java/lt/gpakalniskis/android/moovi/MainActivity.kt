package lt.gpakalniskis.android.moovi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import lt.gpakalniskis.android.moovi.destinations.HomeScreenDestination
import lt.gpakalniskis.android.moovi.destinations.InfoScreenDestination
import lt.gpakalniskis.android.moovi.destinations.SettingsScreenDestination
import lt.gpakalniskis.android.moovi.destinations.TypedDestination
import lt.gpakalniskis.android.moovi.ui.theme.MooviTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MooviTheme {
                val navController = rememberNavController()
                val routes = BottomBarDestination.values().map { it.direction.route }
                val shouldShowBottomBar = navController.currentBackStackEntryAsState().value
                    ?.destination?.route in routes

                Scaffold(bottomBar = {
                    if (shouldShowBottomBar) {
                        BottomBar(navController = navController)
                    }
                }) { paddingValues ->
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
fun Header(navigator: DestinationsNavigator) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Moovi", fontSize = 24.sp, fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Icon(imageVector = Icons.Filled.Settings, contentDescription = null, modifier = Modifier
            .clickable { navigator.navigate(SettingsScreenDestination()) })
    }
}

@Composable
fun NumbersList(numbers: MutableList<Int>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding
                (horizontal = 16.dp)
    ) {
        items(numbers) { item ->
            Text(
                text = item.toString(), fontSize = 20.sp, modifier =
                Modifier.padding(vertical = 16.dp)
            )
            Divider()
        }
    }
}

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {
    val numbers = remember {
        mutableStateListOf<Int>()
    }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White)
    ) {
        Column {
            Header(navigator)
            NumbersList(numbers = numbers)
        }
        FloatingActionButton(
            onClick = {
                numbers.add((0..59).random())
            },
            backgroundColor = Color.Black,
            contentColor = Color.White,
            modifier = Modifier
                .padding(all = 16.dp)
                .align(alignment = Alignment.BottomEnd),
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Destination
@Composable
fun InfoScreen(navigator: DestinationsNavigator) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(text = "Info screen")
    }
}

@Destination
@Composable
fun SettingsScreen(navigator: DestinationsNavigator) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {
        Text(text = "Settings screen")
    }
}

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int
) {
    Home(HomeScreenDestination, Icons.Default.Home, R.string.bottom_bar_home),
    Info(InfoScreenDestination, Icons.Default.Info, R.string.bottom_bar_info),
}

@Composable
fun BottomBar(
    navController: NavController
) {
    val currentDestination: TypedDestination<*> = navController.appCurrentDestinationAsState()
        .value
        ?: NavGraphs.root.startAppDestination

    BottomNavigation {
        BottomBarDestination.values().forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.navigate(destination.direction) {
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = stringResource(destination.label)
                    )
                },
                label = { Text(stringResource(destination.label)) },
                modifier = Modifier.background(Color.Black)
            )
        }
    }
}