package de.moyapro.filecopy

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import de.moyapro.filecopy.model.FileSystemNode

@Composable
fun DirectoryComponent(file: FileSystemNode) {
    Text("Directory: ${file.text()}")
}
