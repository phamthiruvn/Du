package main

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import java.io.File
import kotlin.system.exitProcess


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
            exitProcess(1)
        }

        if (files.any { !File(it).exists() }) exitProcess(1)

        val base = if (si) 1000 else 1024

        if (c) {
            if (h) println("Total size: ${unit(getSizes(files)!!, base)}")
            else println("Total size: ${getSizes(files)!! / base}")
        } else {
            for (it in files) {
                if (h) println("File ${File(it).name}: ${unit(getSize(it)!!, base)}")
                else println("File ${File(it).name}: ${getSize(it)!! / base}")
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
        file.exists() && file.isDirectory -> file.walk().filter { !it.isDirectory }.map { it.length() }.sum()
        file.exists() && !file.isDirectory -> file.length()
        else -> null
    }
}

fun getSizes(list: List<String>): Long? {
    val files = list.map {getSize(it) }
    var sum = 0L
    for (it in files.filterNotNull()) {
        sum += it
    }
    return sum
}


