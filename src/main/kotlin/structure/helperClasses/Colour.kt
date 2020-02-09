package structure.helperClasses

import java.io.Serializable
import kotlin.math.PI
import kotlin.math.sin


data class Color(val red: Double, val green: Double, val blue: Double):Serializable{
    constructor(singleColor: java.awt.Color):this(singleColor.red/255.0, singleColor.green/255.0, singleColor.blue/255.0)
    constructor(red: Int, green: Int, blue: Int):this(red/255.0, green/255.0, blue/255.0)
}

interface ColorMode:Serializable{
    fun colorWalls(walls: Collection<SpookyWall>)
}

data class SingleColor(val c: Color): ColorMode {
    override fun colorWalls(walls: Collection<SpookyWall>) {
        walls.forEach{
            it.color = c
        }
    }
}

data class Gradient(val startColor: Color, val endColor: Color): ColorMode {
    override fun colorWalls(walls: Collection<SpookyWall>) {
        val cr = endColor.red - startColor.red
        val cg = endColor.green - startColor.green
        val cb = endColor.blue - startColor.blue
        val amount= walls.size
        for((index, w) in walls.withIndex()){
            val red = startColor.red + cr*index/amount
            val green = startColor.green + cg*index/amount
            val blue = startColor.blue + cb*index/amount
            w.color=Color(red,green,blue)
        }
    }
}

data class Rainbow(private val repetitions: Double = 1.0): ColorMode{
    override fun colorWalls(walls: Collection<SpookyWall>) {
        for ((index,w) in walls.withIndex()){
            val i = index/walls.size *2* PI * repetitions
            val r = sin(i+0/3* PI)
            val g = sin(i+2/3* PI)
            val b = sin(i+4/3* PI)
            w.color=Color(r,g,b)
        }
    }
}

data class Flash(val color: Color, val flashColor: Color= Color(java.awt.Color.WHITE)): ColorMode{
    override fun colorWalls(walls: Collection<SpookyWall>) {
        for ((index,w) in walls.withIndex()){
            w.color = when {
                index % 2 == 0 -> color
                else -> flashColor
            }
        }
    }
}

object NoColor: ColorMode {
    override fun colorWalls(walls: Collection<SpookyWall>) {
        walls.forEach{
            it.color = null
        }
    }
}

