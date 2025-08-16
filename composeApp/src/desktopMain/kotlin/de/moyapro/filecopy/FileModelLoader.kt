package de.moyapro.filecopy

import androidx.compose.runtime.snapshots.SnapshotStateList
import de.moyapro.filecopy.model.FileSystemNode
import de.moyapro.filecopy.model.SELECTION
import java.io.File

fun loadDirectoryContents(directory: File, targetNodes: SnapshotStateList<File>): List<FileSystemNode> {
    val currentWithSubdirs = loadSubdir(directory).toMutableList()
    if (currentWithSubdirs.isEmpty()) return emptyList()
    currentWithSubdirs.sortWith(compareBy({ it.absolutePath }, { it.isDirectory }))
    val dirNodes = currentWithSubdirs
        .filter(::isFileVisible)
        .map(::FileSystemNode)
        .map { currentNode ->
            if (currentNode.isFile()
                && targetNodes.any {
                    it.absolutePath.substringAfterLast("/") == currentNode.text().substringAfterLast("/")
                }
            ) {
                currentNode.isSelected = SELECTION.ALREADY_COPIED
            }
            return@map currentNode
        }
    dirNodes[0].isVisible = true
    dirNodes[0].isChildrenVisible = false
    dirNodes.forEach { node ->
        if (node.isDirectory()) node.isSelected = determineFolderSelectionStatus(node, dirNodes)
    }
    return dirNodes
}

fun loadSubdir(directoryName: String): List<File> {
    val directory = File(directoryName)
    if (!directory.isDirectory || !directory.canRead() || !directory.canWrite()) return emptyList()
    return loadSubdir(directory)
}

fun loadSubdir(directory: File): List<File> {
    val currentWithSubdirs = mutableListOf(directory)
    val currentDirectory = directory.listFiles().toMutableList()
    currentDirectory.forEach { element ->
        when {
            element.isDirectory -> currentWithSubdirs.addAll(loadSubdir(element))
            element.isFile -> currentWithSubdirs.add(element)
        }
    }
    return currentWithSubdirs
}

private fun isFileVisible(file: File) = !file.isHidden
