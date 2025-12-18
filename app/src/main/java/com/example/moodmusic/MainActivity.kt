package com.example.moodmusic

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.moodmusic.ui.MoodScreen
import com.example.moodmusic.ui.NotesScreen
import com.example.moodmusic.ui.TrackDetailsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hardcoded key for now !!!!!!
        val apiKey = "4c0e716e5f6a272f9b32b5f038b5f907"


        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "mood") {

                composable("mood") {
                    MoodScreen(
                        onTrackClick = { artist, track ->
                            val a = Uri.encode(artist)
                            val t = Uri.encode(track)
                            navController.navigate("track/$a/$t")
                        },
                        onNotesClick = {
                            navController.navigate("notes")
                        }
                    )
                }

                composable(
                    route = "track/{artist}/{track}",
                    arguments = listOf(
                        navArgument("artist") { type = NavType.StringType },
                        navArgument("track") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val artist = backStackEntry.arguments?.getString("artist") ?: ""
                    val track = backStackEntry.arguments?.getString("track") ?: ""

                    TrackDetailsScreen(
                        artist = artist,
                        track = track,
                        apiKey = apiKey
                    )
                }
                composable("notes") {
                    NotesScreen()
                }

            }
        }
    }
}
