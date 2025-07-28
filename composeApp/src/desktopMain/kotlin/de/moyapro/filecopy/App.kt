package de.moyapro.filecopy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.moyapro.filecopy.theme.ThemeAppTheme
import de.moyapro.filecopy.theme.md_theme_dark_surface
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    val rootDir = java.io.File("/home/tom/projects/FileCopy/composeApp/src")
    ThemeAppTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize().background(md_theme_dark_surface)) {
            Column {
                FileTree(rootDir)
            }
        }
    }
}
