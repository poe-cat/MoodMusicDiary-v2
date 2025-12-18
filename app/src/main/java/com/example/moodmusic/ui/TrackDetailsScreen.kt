package com.example.moodmusic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.moodmusic.viewmodel.TrackDetailsViewModel

@Composable
fun TrackDetailsScreen(
    artist: String,
    track: String,
    apiKey: String,
    vm: TrackDetailsViewModel = viewModel()
) {

    val context = LocalContext.current

    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    val info by vm.info.collectAsState()

    var noteText by remember { mutableStateOf("") }
    var showNoteInput by remember { mutableStateOf(false) }

    // init database
    LaunchedEffect(Unit) {
        vm.init(context)
    }

    // load track info when artist/track changes
    LaunchedEffect(artist, track) {
        vm.load(
            artist = artist,
            track = track,
            apiKey = apiKey
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // -------- TITLE --------
        Text(
            text = track,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = artist,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(12.dp))


        // -------- LOADING / ERROR --------
        if (isLoading) {
            CircularProgressIndicator()
            Spacer(Modifier.height(12.dp))
        }

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
        }


        // -------- TRACK INFO --------
        info?.let { t ->

            val imageUrl = t.album?.images
                ?.find { it.size == "extralarge" }?.url
                ?: t.album?.images?.find { it.size == "large" }?.url
                ?: t.album?.images?.find { it.size == "medium" }?.url

            if (!imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(180.dp)
                )
                Spacer(Modifier.height(12.dp))
            }

            Text("Listeners: ${t.listeners ?: "-"}")
            Text("Playcount: ${t.playcount ?: "-"}")

            Spacer(Modifier.height(12.dp))

            val tags = t.topTags?.tags?.map { it.name } ?: emptyList()
            if (tags.isNotEmpty()) {
                Text(
                    text = "Top tags",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = tags.take(12).joinToString(" · "),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(24.dp))


        // -------- NOTES --------

        Button(
            onClick = { showNoteInput = true },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add note")
        }

        if (showNoteInput) {
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Your note") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (noteText.isNotBlank()) {
                        vm.saveNote(
                            artist = artist,
                            track = track,
                            note = noteText
                        )
                        noteText = ""
                        showNoteInput = false
                    }
                }
            ) {
                Text("Save")
            }
        }
    }
}
