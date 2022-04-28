package com.karasev.workerlog

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.util.*


class FileManager {
    fun writeFile(log: String) {
        try {
            val file = File(Environment.getExternalStorageDirectory().toString(), "log.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            file.appendText(Calendar.getInstance().time.toString() + log + "\n")
        } catch (e: Exception) {

        }
    }

    fun readFile(): String =
        FileInputStream(
            File(Environment.getExternalStorageDirectory().toString(), "log.txt")
        ).bufferedReader().use { it.readText() }

}