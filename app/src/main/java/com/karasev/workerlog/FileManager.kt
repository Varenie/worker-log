package com.karasev.workerlog

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.util.*


class FileManager {
    fun writeFile(log: String, ctx: Context) {
        val file = File(ctx.filesDir, "log.txt")
        if (!file.exists()) {
            file.createNewFile()
        }

        file.appendText(Calendar.getInstance().time.toString() +log+"\n")
    }

    fun readFile(ctx: Context):String =
        FileInputStream(
            File(ctx.filesDir, "log.txt")
        ).bufferedReader().use { it.readText() }



}