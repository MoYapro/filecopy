package de.moyapro.filecopy

import de.moyapro.filecopy.model.FileSystemNode
import java.io.File

fun loadDirectoryContents(directory: File): List<FileSystemNode> {
    val currentWithSubdirs = loadSubdir(directory)
    if (currentWithSubdirs.isEmpty()) return emptyList()
    currentWithSubdirs.sortWith(compareBy({ it.absolutePath }, { it.isDirectory }))
    val dirNodes = currentWithSubdirs
        .filter(::isFileVisible)
        .map(::FileSystemNode)
    dirNodes[0].isVisible = true
    dirNodes[0].isChildrenVisible = false
    return dirNodes
}

fun loadSubdir(directory: File): MutableList<File> {
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
