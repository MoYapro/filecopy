package de.moyapro.filecopy.model

import java.io.File
import java.util.*

data class FileSystemNode(
    val file: File,
    var isSelected: SELECTION = SELECTION.NO,
    var isVisible: Boolean = false,
    var isChildrenVisible: Boolean = true,
    val id: UUID = UUID.randomUUID(),
) {
    fun isDirectory() = file.isDirectory
    fun isFile() = file.isFile
    fun text(): String = file.absolutePath
}

enum class SELECTION(val symbol: String = " ") {
    YES("✔"),
    NO(" . "),
    PARTIAL(" - "),
    ALREADY_COPIED("\uD83D\uDDB4");

    fun invert(): SELECTION = when (this) {
        NO -> YES
        YES -> NO
        PARTIAL -> NO
        ALREADY_COPIED -> ALREADY_COPIED
    }
}
