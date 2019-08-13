package com.github.spookyghost.beatwalls

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.file
import mu.KotlinLogging
import reader.*
import song.*
import structures.WallStructureManager
import java.io.File
import kotlin.math.roundToInt
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}
class Beatwalls : CliktCommand() {
    //TODO ADD ICON


    private val file: File by argument(help = "difficulty File (e.G expertPlus.dat)").file().validate {
        require((it.isDifficulty()) || it.isSong()) { "Use a SongFolder or DifficultyFile" }
    }

    private val keepFiles by option("--keepFiles", "-k", help = "keeps original files as backups").flag(default = false)

    private val dryRun by option(
        "--dryRun",
        "-d",
        help = "Do not modify filesystem, only log output"
    ).flag(default = false)

    private val keepWalls by option(
        "--keepWalls",
        "-w",
        help = "keeps the original walls instead of deleting them"
    ).flag(default = false)

    private val yes by option("--yes", "-y", help = "skips confirmation").flag(default = false)

    private val bpm by option("--bpm", "-b", help = "Beats per minute").double()


    private var WallCounter = 0
    private var beatsPerMinute = 0.0
    private val difficultyList = mutableMapOf<Difficulty, File>()

    init {
        context {
            helpFormatter = CliktHelpFormatter(showDefaultValues = true)
        }

    }

    override fun run() {

        try {
            WallStructureManager.loadManager(readAssets())
            //adds all the song
            when {
                file.isSong() -> {
                    logger.info { "Detected song. Running the program through all Difficulties which have commands" }
                    val map = Song(file)
                    beatsPerMinute = bpm ?: map.info._beatsPerMinute
                    map.difficultyList.forEach {
                            difficultyList += it.toPair()
                    }
                }
                file.isDifficulty() -> {
                    logger.info { "Detected Difficulty" }
                    if (bpm == null)
                        logger.error { "No BPM detected, pls use the -b option" }
                    else
                        beatsPerMinute = bpm as Double
                    difficultyList += Pair(readDifficulty(file), file)
                }
            }

        } catch (e: Exception) {
            logger.error { "Failed to read Song. Is it really in the right format?" }
            exitProcess(-1)
        }



        for (difficulty in difficultyList) {

            val diff = difficulty.component1()
            println("found difficulty ${diff._bookmarks}")
            diff._bookmarks.forEach {
                val a = it.getCommandList("SPLIT").first().scale.roundToInt()
                println(  "Found BOOKMARK ${it._name} at ${it._time} with $a" )

            }






        }

        logger.info { "\nfinished run, written $WallCounter Wall Structures" }
    }
}

