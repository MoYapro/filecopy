package de.moyapro.filecopy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.moyapro.filecopy.model.FileSystemNode
import de.moyapro.filecopy.model.SELECTION
import de.moyapro.filecopy.model.SELECTION.*
import de.moyapro.filecopy.theme.*
import java.io.File
import java.util.*

const val OPEN = true
const val SELECTION_FONT_SIZE: Int = 32


@Composable
fun FileTree(initialRootDir: File) {
    var rootDir by remember { mutableStateOf(initialRootDir) }
    var isValidSourceDirectory by remember { mutableStateOf(rootDir.isDirectory && rootDir.canRead() && rootDir.canWrite()) }
    var targetDirectory by remember { mutableStateOf("/home/tom/outdir") }
    var sourceDirectory by remember { mutableStateOf(rootDir.absolutePath) }
    val targetNodes = loadSubdir(targetDirectory).toMutableStateList()
    val sourceNodes = loadDirectoryContents(rootDir, targetNodes).toMutableStateList()
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
                    label = {
                        Text(
                            "Source directory",
                            fontSize = SELECTION_FONT_SIZE.sp,
                            color = md_theme_dark_onSurface
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = md_theme_dark_onSurface,
                        unfocusedTextColor = md_theme_dark_onSurface,
                        disabledTextColor = md_theme_dark_onSurface,
                        errorTextColor = md_theme_dark_onSurface,
                        focusedContainerColor = if (isValidSourceDirectory) md_theme_dark_surface else md_theme_dark_error,
                        unfocusedContainerColor = if (isValidSourceDirectory) md_theme_dark_surface else md_theme_dark_error,
                        disabledContainerColor = if (isValidSourceDirectory) md_theme_dark_surface else md_theme_dark_error,
                        errorContainerColor = md_theme_dark_error,
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
                    sourceNodes.clear()
                    sourceNodes.addAll(
                        loadDirectoryContents(File(sourceDirectory), targetNodes)
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
                    label = {
                        Text(
                            "Target directory",
                            fontSize = SELECTION_FONT_SIZE.sp,
                            color = md_theme_dark_onSurface
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = md_theme_dark_onSurface,
                        unfocusedTextColor = md_theme_dark_onSurface,
                        disabledTextColor = md_theme_dark_onSurface,
                        errorTextColor = md_theme_dark_onSurface,
                        focusedContainerColor = if (isValidTargetDirectory) md_theme_dark_surface else md_theme_dark_error,
                        unfocusedContainerColor = if (isValidTargetDirectory) md_theme_dark_surface else md_theme_dark_error,
                        disabledContainerColor = if (isValidTargetDirectory) md_theme_dark_surface else md_theme_dark_error,
                        errorContainerColor = md_theme_dark_error,
                    ),
                    textStyle = TextStyle(fontSize = SELECTION_FONT_SIZE.sp, color = md_theme_dark_onBackground),
                )
                Column {
                    Button(onClick = { isValidTargetDirectory = createOutputDirectory(targetDirectory) }) {
                        Text("Create")
                    }
                    Button(onClick = {
                        if (isValidTargetDirectory) targetNodes.addAll(loadSubdir(File(targetDirectory)))
                        else targetNodes.clear()
                    }) {
                        Text("Load output directory")
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { copy(sourceNodes, targetDirectory) }) {
                    Text("Copy ${sourceNodes.filter { it.isSelected == YES && it.isFile() }.size} files")
                }
                Button(
                    onClick = { delete(sourceNodes, targetDirectory) },
                    colors = ButtonDefaults.buttonColors(md_theme_dark_error)
                ) {
                    Text("Delete ${sourceNodes.filter { it.isSelected == DELETE && it.isFile() }.size} files")
                }
            }
        }
        LazyColumn(modifier = Modifier) {
            items(items = sourceNodes, key = { it.id }) { node ->
                AnimatedVisibility(node.isVisible) {
                    FileRow(node, sourceNodes)
                }
            }
        }
    }
}

@Composable
fun FileRow(node: FileSystemNode, nodes: MutableList<FileSystemNode> = mutableListOf()) {
    val selectNode = { nodes.select(node.id) }
    Row(
        modifier = Modifier.fillMaxWidth().height(40.dp).border(1.dp, md_theme_dark_outline),
        verticalAlignment = Alignment.CenterVertically

    ) {
        TypeIndicator(node) { nodes.openCloseDirectory(node.id) }
        SelectionIndicator(node, selectNode)
        Text(
            text = node.text(),
            fontSize = 22.sp,
            color = md_theme_dark_onBackground,
            modifier = Modifier.clickable(onClick = selectNode)
        )
    }
}


@Composable
fun SelectionIndicator(node: FileSystemNode, onClick: () -> Unit) {

    Box() {
        Text(
            text = "[${node.isSelected.symbol}]",
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            color = md_theme_dark_onBackground,
            modifier = Modifier.width(50.dp).clickable(onClick = onClick)
        )
    }
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
    val clickedNode = this.single { it.id == id }
    val clickedOnSong = clickedNode.isFile()
    val switchToSelection: SELECTION = clickedNode.isSelected.invert()
    val clickedNodeName: String = clickedNode.text()
    val replacedNodes = mutableListOf<FileSystemNode>(clickedNode.copy(isSelected = switchToSelection))

    for (i in indices) {
        val currentNodeText = this[i].text()
        if (currentNodeText.startsWith(clickedNodeName)) {
            replacedNodes.add(this[i].copy(isSelected = switchToSelection))
        }
    }
    replacedNodes.forEach { node ->
        val indexToReplace = indexOfFirst { node.id == it.id }
        set(indexToReplace, node)
    }

    for (i in indices) {
        val currentNode = this[i]
        if (clickedOnSong
            && currentNode.isDirectory()
            && clickedNode.isDirectChildOf(currentNode.text())
        ) {
            val folderSelected = determineFolderSelectionStatus(currentNode, this)
            replacedNodes.add(this[i].copy(isSelected = folderSelected))
        }
    }

    replacedNodes.forEach { node ->
        val indexToReplace = indexOfFirst { node.id == it.id }
        set(indexToReplace, node)
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


fun determineFolderSelectionStatus(node: FileSystemNode, allTreeElements: List<FileSystemNode>): SELECTION {
    val children = allTreeElements.filter { it.isFile() && it.isDirectChildOf(node.text()) }
    if (children.isEmpty()) return NO
    return when {
        children.all { it.isSelected in listOf(YES, ALREADY_COPIED) } -> YES
        children.all { it.isSelected == NO } -> NO
        else -> PARTIAL
    }
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
    val copyFiles = nodes.filter { it.isSelected == YES && it.isFile() }
    copyFiles.forEach { sourceFile ->
        sourceFile.file.copyTo(File("$targetDirectory/${sourceFile.file.name}"))
    }
}

fun delete(nodes: MutableList<FileSystemNode>, targetDirectory: String) {
    val deleteFiles = nodes.filter { it.isSelected == DELETE && it.isFile() }
    deleteFiles.forEach { sourceFile ->
        val fileToDelete = File("$targetDirectory/${sourceFile.file.name}")
        fileToDelete.delete()
    }
}

private fun FileSystemNode.isDirectChildOf(parentCandidate: String) =
    this.text().count { it == '/' } == parentCandidate.count { it == '/' } + 1
            && this.text().removeLastPathSegment() == parentCandidate

private fun String.removeLastPathSegment() = this.substring(0, this.lastIndexOf('/'))
