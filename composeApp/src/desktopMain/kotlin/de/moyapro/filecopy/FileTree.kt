package de.moyapro.filecopy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.moyapro.filecopy.model.FileSystemNode
import de.moyapro.filecopy.theme.*
import java.io.File
import java.util.*

const val SELECT = true
const val OPEN = true
const val SELECTION_FONT_SIZE: Int = 32


@Composable
fun FileTree(initialRootDir: File) {
    var rootDir by remember { mutableStateOf(initialRootDir) }
    var isValidSourceDirectory by remember { mutableStateOf(rootDir.isDirectory && rootDir.canRead() && rootDir.canWrite()) }
    val nodes = loadDirectoryContents(rootDir).toMutableStateList()
    var sourceDirectory by remember { mutableStateOf(rootDir.absolutePath) }
    var targetDirectory by remember { mutableStateOf("/home/tom/outdir") }
    var isValidTargetDirectory by remember {
        val target = File(targetDirectory)
        mutableStateOf(target.isDirectory && target.canRead() && target.canWrite())
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                TextField(
                    modifier = Modifier.fillMaxWidth(.9f),
                    value = sourceDirectory,
                    label = { Text("Source directory", fontSize = SELECTION_FONT_SIZE.sp, color = md_theme_dark_onSurface) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = md_theme_dark_onSurface,
                        backgroundColor = (if (isValidSourceDirectory) md_theme_dark_surface else md_theme_dark_error),
                    ),
                    textStyle = TextStyle(fontSize = SELECTION_FONT_SIZE.sp, color = md_theme_dark_onBackground),
                    onValueChange = { newText ->
                        sourceDirectory = newText
                        val newFile = File(sourceDirectory)
                        isValidSourceDirectory = newFile.isDirectory && newFile.canRead() && newFile.canWrite()
                    },
                )
                Button(onClick = {
                    if (!isValidSourceDirectory) return@Button
                    nodes.clear()
                    nodes.addAll(
                        loadDirectoryContents(File(sourceDirectory))
                    )
                }) {
                    Text("Load source")
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                TextField(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    value = targetDirectory,
                    onValueChange = { newText ->
                        targetDirectory = newText
                        isValidTargetDirectory =
                            File(targetDirectory).isDirectory && File(targetDirectory).canRead() && File(targetDirectory).canWrite()
                    },
                    label = { Text("Target directory", fontSize = SELECTION_FONT_SIZE.sp, color = md_theme_dark_onSurface) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = md_theme_dark_onSurface,
                        backgroundColor = (if (isValidTargetDirectory) md_theme_dark_surface else md_theme_dark_error),
                    ),
                    textStyle = TextStyle(fontSize = SELECTION_FONT_SIZE.sp, color = md_theme_dark_onBackground),
                )
                Button(onClick = { isValidTargetDirectory = createOutputDirectory(targetDirectory) }) {
                    Text("Create")
                }
            }
            Button(onClick = { copy(nodes, targetDirectory) }) {
                Text("Copy ${nodes.filter { it.isSelected && it.isFile() }.size} files")
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
        modifier = Modifier.fillMaxWidth().height(40.dp).border(1.dp, md_theme_dark_outline),
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
        textAlign = TextAlign.Center,
        fontSize = 22.sp,
        color = md_theme_dark_onBackground,
        modifier = Modifier.width(50.dp).clickable(onClick = onClick)
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
        modifier = Modifier.width(50.dp).clickable(onClick = onClick)
    )
}

fun MutableList<FileSystemNode>.openCloseDirectory(id: UUID) {
    var startIndex = -1
    lateinit var clickedNodeName: String
    var pathDepth: Int = -1
    var openOrClose = OPEN
    val replacedNodes = mutableListOf<FileSystemNode>()

    for (i in indices) {
        if (startIndex == -1 && this[i].id == id) {
            startIndex = i
            openOrClose = !this[i].isChildrenVisible
            clickedNodeName = this[i].text()
            pathDepth = countOccurences(clickedNodeName, '/') + 1
            replacedNodes.add(this[i].copy(isChildrenVisible = openOrClose))
        } else if (openOrClose == OPEN) {
            if (startIndex >= 0 && this[i].text()
                    .startsWith(clickedNodeName) && pathDepth == countOccurences(this[i].text(), '/')
            ) {
                replacedNodes.add(this[i].copy(isVisible = openOrClose, isChildrenVisible = false))
            }
        } else {
            if (this[i].text().startsWith(clickedNodeName)) {
                replacedNodes.add(this[i].copy(isVisible = openOrClose, isChildrenVisible = openOrClose))
            }
        }
    }

    require(startIndex >= 0) { "Element to replace is not in the list." }
    replacedNodes.forEach { node ->
        val replaceIndex = indexOfFirst { it.id == node.id }
        set(replaceIndex, node)
    }
}

fun countOccurences(haystack: String, needle: Char): Int {
    return haystack.count { it == needle }
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


fun createOutputDirectory(targetDirectory: String): Boolean {
    val target = File(targetDirectory)
    if (!target.exists()) {
        target.mkdirs()
        return true
    } else if (!target.isDirectory) {
        return false
    } else if (!target.canWrite()) {
        return false
    }
    return false
}

fun copy(nodes: MutableList<FileSystemNode>, targetDirectory: String) {
    val copyFiles = nodes.filter { it.isSelected && it.isFile() }
    copyFiles.forEach { sourceFile ->
        sourceFile.file.copyTo(File("$targetDirectory/${sourceFile.file.name}")) }
}
