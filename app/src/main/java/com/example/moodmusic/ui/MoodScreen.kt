package com.example.moodmusic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.moodmusic.model.Mood
import com.example.moodmusic.viewmodel.MoodViewModel

@Composable
fun MoodScreen(
    onTrackClick: (artist: String, track: String) -> Unit,
    onNotesClick: () -> Unit,
    vm: MoodViewModel = viewModel()
) {

    val tracks by vm.tracks.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    val currentMood by vm.currentMood.collectAsState()
    val surpriseMoods by vm.surpriseMoods.collectAsState()


    LaunchedEffect(Unit) {
        vm.restoreLastMood()
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // -------- HEADER --------

        item {
            currentMood?.let { mood ->
                Text(
                    text = "Current mood: ${
                        mood.name.lowercase().replaceFirstChar { it.uppercase() }
                    }",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Includes: ${mood.tags.joinToString(" · ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(12.dp))
            }

            if (currentMood == null && surpriseMoods.isNotEmpty()) {
                Text(
                    text = "Surprise mix",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Based on moods: " +
                            surpriseMoods.joinToString(" · ") {
                                it.name.lowercase().replaceFirstChar { c -> c.uppercase() }
                            },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(12.dp))
            }
        }

        // -------- NOTES BUTTON --------

        item {
            OutlinedButton(
                onClick = onNotesClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Notes")
            }

            Spacer(Modifier.height(8.dp))
            Divider()
            Spacer(Modifier.height(8.dp))
        }

        // -------- SURPRISE MODE --------

        item {
            FilledTonalButton(
                onClick = { vm.loadSurpriseMood() },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text("Surprise me")
            }
        }

        // -------- MOODS --------

        items(Mood.values()) { mood ->
            Button(
                onClick = { vm.loadMood(mood) },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (mood == currentMood)
                            moodColor(mood)
                        else
                            moodColor(mood).copy(alpha = 0.65f),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    mood.name.lowercase()
                        .replaceFirstChar { it.uppercase() }
                )
            }
        }

        // -------- STATES --------

        item {
            if (isLoading) {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            error?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))
        }

        // -------- TRACKS --------

        items(tracks) { track ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                val imageUrl = track.images
                    .find { it.size == "medium" }
                    ?.url

                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = track.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = track.artist.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(
                    onClick = {
                        onTrackClick(track.artist.name, track.name)
                    }
                ) {
                    Text("Details")
                }
            }
        }
    }
}


/* -------- MOOD COLORS -------- */

@Composable
fun moodColor(mood: Mood): Color =
    when (mood) {
        Mood.SAD -> Color(0xFF4A6FA5)
        Mood.HAPPY -> Color(0xFFF29F05)
        Mood.CALM -> Color(0xFF4BA3A1)
        Mood.ENERGETIC -> Color(0xFFE5533D)
        Mood.ANGRY -> Color(0xFF8B2E2E)
        Mood.FOCUS -> Color(0xFF5E4FA2)
    }
