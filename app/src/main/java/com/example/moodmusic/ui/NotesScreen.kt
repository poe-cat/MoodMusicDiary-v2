package com.example.moodmusic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.moodmusic.viewmodel.NotesViewModel

@Composable
fun NotesScreen() {

    val dateFormat = remember {
        java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm",
            java.util.Locale.getDefault()
        )
    }

    val context = LocalContext.current
    val vm = remember { NotesViewModel(context) }

    val notes by vm.notes.collectAsState()

    var editingNoteId by remember { mutableStateOf<Long?>(null) }
    var editText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Your notes",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(12.dp))

        if (notes.isEmpty()) {
            Text(
                text = "No notes yet",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn {
                items(notes) { note ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(
                                text = note.track,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = note.artist,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(Modifier.height(8.dp))

                            if (editingNoteId == note.id) {

                                OutlinedTextField(
                                    value = editText,
                                    onValueChange = { editText = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(Modifier.height(8.dp))

                                Row {
                                    Button(
                                        onClick = {
                                            if (editText.isNotBlank()) {
                                                vm.update(note, editText)
                                                editingNoteId = null
                                            }
                                        }
                                    ) {
                                        Text("Save")
                                    }

                                    Spacer(Modifier.width(8.dp))

                                    TextButton(
                                        onClick = {
                                            editingNoteId = null
                                        }
                                    ) {
                                        Text("Cancel")
                                    }
                                }

                            } else {

                                Text(note.note)

                                Spacer(Modifier.height(6.dp))

                                Text(
                                    text = dateFormat.format(java.util.Date(note.timestamp)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(Modifier.height(8.dp))

                                Row {
                                    TextButton(
                                        onClick = {
                                            editingNoteId = note.id
                                            editText = note.note
                                        }
                                    ) {
                                        Text("Edit")
                                    }

                                    Spacer(Modifier.width(8.dp))

                                    TextButton(
                                        onClick = {
                                            vm.delete(note)
                                        }
                                    ) {
                                        Text("Delete")
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}
