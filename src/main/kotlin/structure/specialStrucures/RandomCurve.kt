package structure.specialStrucures

import structure.helperClasses.Point
import structure.RandomCurve
import structure.add
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun RandomCurve.run(){
    val r = Random(seed)
    val mult: Double
    if((p2.z-p1.z) < 1){
        mult = 1 / (p2.z-p1.z)
        p2  = p2.copy(z=p1.z+1)
    }else{
        mult = 1.0
    }
    var tp3 = randomTimedPoint(r, -0.33 * mult)
    var tp4 = p1
    for(i in p1.z.toInt() until p2.z.toInt()) {
        val tp1 = tp4.copy()
        val tp2 = tp4.mirrored(tp3)
        tp3 = randomTimedPoint(r, i + 0.66 * mult)
        tp4 = if (i + 1 == p2.z.toInt())
            p2.copy(z = p2.z + 1)
        else
            randomTimedPoint(r, i + 1.0 * mult)
        add(curve(tp1, tp2, tp3, tp4, amount))
    }
}

private fun RandomCurve.randomTimedPoint(r: Random, z: Double): Point {
    val minx = min(p1.x, p2.x)
    val maxX = max(p1.x, p2.x).coerceAtLeast(minx + 0.1)
    val minY = min(p1.y, p2.y)
    val maxY = max(p1.y, p2.y).coerceAtLeast(minY + 0.1)
    val x = r.nextDouble(minx, maxX)
    val y = r.nextDouble(minY, maxY)
    return Point(x, y, z)
}

