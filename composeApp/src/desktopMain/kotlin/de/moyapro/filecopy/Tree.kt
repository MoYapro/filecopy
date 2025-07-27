package de.moyapro.filecopy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.moyapro.filecopy.model.FileSystemNode
import java.util.*

const val SELECT = true
const val OPEN = true
const val FONT_SIZE = 20


@Composable
fun Tree(rootDir: java.io.File) {
    val nodes = loadDirectoryContents(rootDir).toMutableStateList()

    Box(
        modifier = Modifier
            .background(color = androidx.compose.ui.graphics.Color.LightGray)
            .fillMaxWidth()
    ) {
        LazyColumn(modifier = Modifier) {
            items(items = nodes, key = { it.id }) { node ->
                AnimatedVisibility(node.isVisible) {
                    Row(modifier = Modifier.fillMaxWidth().height(40.dp)) {
                        Selector(node) { nodes.select(node.id) }
                        TypeIndicator(node) { nodes.openCloseDirectory(node.id) }
                        Text(text = node.text(), fontSize = FONT_SIZE.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun Selector(node: FileSystemNode, onClick: () -> Unit) = Box() {
    Text(
        text = (if (node.isSelected) "[✔]" else "[]"),
        textAlign = TextAlign.Center,
        fontSize = FONT_SIZE.sp,
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
        fontSize = FONT_SIZE.sp,
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
