package de.moyapro.filecopy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    val rootDir = java.io.File("/home/tom/projects/FileCopy/composeApp/src")
    MaterialTheme {

        Box(modifier = Modifier.fillMaxSize()) {
            Tree(rootDir)
        }
    }
}
