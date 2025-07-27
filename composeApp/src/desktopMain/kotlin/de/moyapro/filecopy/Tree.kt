package de.moyapro.filecopy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier

@Composable
fun Tree(rootDir: java.io.File) {
    val nodes = loadDirectoryContents(rootDir).toMutableStateList()

    Box(
        modifier = Modifier
            .background(color = androidx.compose.ui.graphics.Color.LightGray)
            .fillMaxWidth()
    ) {
        LazyColumn(modifier = Modifier) {
            items(items = nodes, key = { it.hashCode() }) { node ->
                if (node.isVisible) Text(node.text())
            }
        }
    }
}
