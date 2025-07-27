package de.moyapro.filecopy.model

import java.io.File
import java.util.*

data class FileSystemNode(
    val file: File,
    var isSelected: Boolean = false,
    var isVisible: Boolean = true,
    var isChildrenVisible: Boolean = true,
    val id: UUID = UUID.randomUUID(),
) {
    fun isDirectory() = file.isDirectory
    fun isFile() = file.isFile
    fun text(): String = file.absolutePath
}
