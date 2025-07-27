package de.moyapro.filecopy.model

import java.io.File

class FileSystemNode(val file: File) {
    var isSelected: Boolean = false
    var isVisible: Boolean = false
    fun isDirectory() = file.isDirectory
    fun text(): String = file.absolutePath
}
