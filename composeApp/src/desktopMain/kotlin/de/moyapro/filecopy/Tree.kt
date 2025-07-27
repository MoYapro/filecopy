package de.moyapro.filecopy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import de.moyapro.filecopy.model.FileSystemNode

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
                AnimatedVisibility(node.isVisible) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = (if (node.isSelected) "[✔]" else "[]"),
                            modifier = Modifier.clickable(onClick = { nodes.replace(node.copy(isSelected = !node.isSelected)) })
                        )
                        Text((if (node.isDirectory()) "[+]" else "♬"))
                        Text(node.text())
                    }
                }
            }
        }
    }
}

fun MutableList<FileSystemNode>.replace(elementToReplace: FileSystemNode) {
    var index = -1
    for (i in indices) {
        if (this[i].id == elementToReplace.id) {
            index = i
            break
        }
    }
    require(index >= 0) { "Element to replace is not in the list." }
    set(index, elementToReplace)
}
