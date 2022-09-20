package com.startInnovations.workerlog

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.util.*


class FileManager {
    fun writeFile(log: String) {
        try {
            val file = getFile()
            if (!file.exists()) {
                file.createNewFile()
            }
            file.appendText(Calendar.getInstance().time.toString() + log + "\n")
        } catch (e: Exception) {

        }
    }

    fun readFile(): String =
        FileInputStream(
            getFile()
        ).bufferedReader().use { it.readText() }

    private fun getFile(): File {
        return File(Environment.getExternalStorageDirectory(), "log.txt")
    }
}