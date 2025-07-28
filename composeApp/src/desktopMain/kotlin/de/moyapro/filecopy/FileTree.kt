package de.moyapro.filecopy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.moyapro.filecopy.model.FileSystemNode
import de.moyapro.filecopy.theme.md_theme_dark_onBackground
import de.moyapro.filecopy.theme.md_theme_dark_onSurface
import de.moyapro.filecopy.theme.md_theme_dark_outline
import java.util.*

const val SELECT = true
const val OPEN = true


@Composable
fun FileTree(rootDir: java.io.File) {
    val nodes = loadDirectoryContents(rootDir).toMutableStateList()

    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(0.5f),
                state = rememberTextFieldState(initialText = rootDir.absolutePath),
                label = { Text("Source directory", fontSize = 18.sp, color = md_theme_dark_onSurface) },
                colors = TextFieldDefaults.textFieldColors(textColor = md_theme_dark_onSurface),
                textStyle = TextStyle(fontSize = 22.sp, color = md_theme_dark_onBackground),
            )
            TextField(
                modifier = Modifier.fillMaxWidth(0.7f),
                state = rememberTextFieldState(initialText = ""),
                label = { Text("Target directory", fontSize = 18.sp, color = md_theme_dark_onSurface) },
                colors = TextFieldDefaults.textFieldColors(textColor = md_theme_dark_onSurface),
                textStyle = TextStyle(fontSize = 22.sp, color = md_theme_dark_onBackground),
            )
            Button(onClick = {}) {
                Text("Copy")
            }
        }
        LazyColumn(modifier = Modifier) {
            items(items = nodes, key = { it.id }) { node ->
                AnimatedVisibility(node.isVisible) {
                    FileRow(node, nodes)
                }
            }
        }
    }
}

@Composable
fun FileRow(node: FileSystemNode, nodes: MutableList<FileSystemNode> = mutableListOf()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .border(1.dp, md_theme_dark_outline),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Selector(node) { nodes.select(node.id) }
        TypeIndicator(node) { nodes.openCloseDirectory(node.id) }
        Text(text = node.text(), fontSize = 22.sp, color = md_theme_dark_onBackground)
    }
}


@Composable
fun Selector(node: FileSystemNode, onClick: () -> Unit) = Box() {
    Text(
        text = (if (node.isSelected) "[✔]" else "[]"),
        textAlign = TextAlign.Center, fontSize = 22.sp,
        color = md_theme_dark_onBackground,
        modifier = Modifier
            .width(50.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
fun TypeIndicator(node: FileSystemNode, onClick: () -> Unit) = Box() {
    val displaySymbol = when {
        node.isFile() -> "♬"
        node.isChildrenVisible -> "[ ]"
        else -> "[+]"
    }
    Text(
        text = displaySymbol,
        textAlign = TextAlign.Center,
        fontSize = 22.sp,
        color = md_theme_dark_onBackground,
        modifier = Modifier
            .width(50.dp)
            .clickable(onClick = onClick)
    )
}

fun MutableList<FileSystemNode>.openCloseDirectory(id: UUID) {
    var startIndex = -1
    lateinit var clickedNodeName: String
    var openOrClose = OPEN
    val replacedNodes = mutableListOf<FileSystemNode>()

    for (i in indices) {
        if (startIndex == -1 && this[i].id == id) {
            startIndex = i
            openOrClose = !this[i].isChildrenVisible
            clickedNodeName = this[i].text()
            replacedNodes.add(this[i].copy(isChildrenVisible = openOrClose))
        } else if (startIndex >= 0 && this[i].text().startsWith(clickedNodeName)) {
            replacedNodes.add(this[i].copy(isVisible = openOrClose, isChildrenVisible = openOrClose))
        }
    }

    require(startIndex >= 0) { "Element to replace is not in the list." }
    replacedNodes.forEachIndexed { index, node ->
        set(startIndex + index, node)
    }
}

fun MutableList<FileSystemNode>.select(id: UUID) {
    var startIndex = -1
    lateinit var clickedNodeName: String
    var selectOrDeselect = SELECT
    val replacedNodes = mutableListOf<FileSystemNode>()

    for (i in indices) {
        if (startIndex == -1 && this[i].id == id) {
            startIndex = i
            selectOrDeselect = !this[i].isSelected
            clickedNodeName = this[i].text()
            replacedNodes.add(this[i].copy(isSelected = selectOrDeselect))
        } else if (startIndex >= 0 && this[i].text().startsWith(clickedNodeName)) {
            replacedNodes.add(this[i].copy(isSelected = selectOrDeselect))
        }
    }

    require(startIndex >= 0) { "Element to replace is not in the list." }
    replacedNodes.forEachIndexed { index, node ->
        set(startIndex + index, node)
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
