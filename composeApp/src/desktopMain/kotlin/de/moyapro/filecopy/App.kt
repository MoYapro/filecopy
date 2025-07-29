package de.moyapro.filecopy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.moyapro.filecopy.theme.ThemeAppTheme
import de.moyapro.filecopy.theme.md_theme_dark_surface
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    val rootDir = java.io.File(".")
    ThemeAppTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize().background(md_theme_dark_surface)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 10.dp)
                    .background(md_theme_dark_surface)
            ) {
                Column {
                    FileTree(rootDir)
                }
            }
        }
    }
}
