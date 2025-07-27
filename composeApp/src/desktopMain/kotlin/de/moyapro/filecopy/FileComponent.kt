package de.moyapro.filecopy

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun FileComponent(filename: String) {
    Text("File: $filename")
}
