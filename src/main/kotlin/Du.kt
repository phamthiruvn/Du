package main

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import java.io.File


fun main(arguments: Array<String>) = Du().sizeDu(arguments)

class Du {
    @Option(name = "-h")
    var h = false

    @Option(name = "-c")
    var c = false

    @Option(name = "--si")
    var si = false

    @Argument(required = true)
    var files = mutableListOf<String>()

    fun sizeDu(arguments: Array<String>) {

        val parser = CmdLineParser(this)
        try {
            parser.parseArgument(*arguments)
        } catch (e: CmdLineException) {
            println("Error!")
            return
        }

        val base = if (si) 1000 else 1024
        val list = files.map { Pair(File(it).name, getSize(it)) }

        if (c) {
            var sum = 0L
            for (it in list.filter { it.second != null }) {
                sum += it.second!!
            }

            if (list.any { it.second == -1L })
                println("Files ${list.filter { it.second == -1L }.joinToString(", ") { it.first }} don't exist")
            else {
                if (h) println("Files ${list.joinToString(", ") { it.first }}: ${unit(sum, base)}")
                else println("Files ${list.joinToString(", ") { it.first }}: $sum")
            }
        } else {
            for (it in list) {
                if (it.second == null) println("File ${it.first} doesn't exist ")
                else if (h) println("File ${it.first}: ${unit(it.second!!, base)}")
                else println("File ${it.first}: ${it.second}")

            }
        }
    }
}


fun unit(size: Long, si: Int): String {
    val byte = listOf("B", "KB", "MB", "GB")
    var value = size
    var i = 0
    while (si < value && i < 3) {
        value /= si
        i++
    }
    return "$value ${byte[i]}"
}

fun getSize(name: String): Long? {
    val file = File(name)
    return when {
        file.exists() && file.isDirectory -> file.walk().toList().filter { !it.isDirectory }.map { it.length() }.sum()
        file.exists() && !file.isDirectory -> file.length()
        else -> null
    }
}
