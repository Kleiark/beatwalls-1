package old_structures

import com.google.gson.annotations.SerializedName
import structure.*
import kotlin.math.*
import kotlin.random.Random


sealed class OldWallStructure  {
    abstract val name: String

    abstract val mirror: Boolean

    abstract val spookyWallList: ArrayList<SpookyWall>

    abstract fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall>
}

/** gets helix with fixed duration */
object Helix: OldWallStructure(){
    override val name: String = "helix"
    override val mirror: Boolean = false
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        spookyWallList.clear()
        val count = oldParameters.customParameters.getIntOrElse(0,1)
        val radius = oldParameters.customParameters.getDoubleOrElse(1,2.0)
        val fineTuning = oldParameters.customParameters.getIntOrElse(2,10)
        val startRotation = oldParameters.customParameters.getDoubleOrElse(3,0.0)
        val rotationCount = oldParameters.customParameters.getDoubleOrElse(4,1.0)
        val reverse = oldParameters.customParameters.getBooleanOrElse(5,false)
        val heightOffset = oldParameters.customParameters.getDoubleOrElse(6,2.0)
        val speedChange = oldParameters.customParameters.getOrNull(7)?.toDouble()
        val wallDuration = oldParameters.customParameters.getOrNull(8)?.toDouble()
        spookyWallList.addAll(
            circle(
                count = count,
                radius = radius,
                fineTuning = fineTuning,
                startRotation = startRotation,
                rotationCount = rotationCount,
                heightOffset = heightOffset,
                speedChange = speedChange,
                wallDuration = wallDuration,
                helix = true,
                reverse = reverse
            )
        )
        return spookyWallList
    }
}


/** gets CeilingHelix with fixed duration */
object CeilingHelix: OldWallStructure(){
    override val name: String = "ceilinghelix"
    override val mirror: Boolean = false
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        spookyWallList.clear()
        val count = 1
        val startRotation = 0.0
        val rotationCount = 0.5
        val heightOffset = 0.0
        val fineTuning = oldParameters.customParameters.getIntOrElse(0,10)
        val reverse = oldParameters.customParameters.getBooleanOrElse(1,false)
        val radius = oldParameters.customParameters.getDoubleOrElse(2,5.0)
        val speedChange = oldParameters.customParameters.getOrNull(3)?.toDouble()
        val wallDuration = oldParameters.customParameters.getOrNull(4)?.toDouble()
        spookyWallList.addAll(
            circle(
                count = count,
                radius = radius,
                fineTuning = fineTuning,
                startRotation = startRotation,
                rotationCount = rotationCount,
                heightOffset = heightOffset,
                speedChange = speedChange,
                wallDuration = wallDuration,
                helix = true,
                reverse = reverse
            )
        )
        return spookyWallList
    }
}

/** creates normal stairways */
object StairWay: OldWallStructure() {
    override val name = "StairWay"
    override val mirror  = true
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        val list = arrayListOf<SpookyWall>()
        val amount = oldParameters.customParameters.getIntOrElse(0,4)
        val min  = oldParameters.customParameters.getDoubleOrElse(1,0.0)
        val max = oldParameters.customParameters.getDoubleOrElse(2,4.0)
        for(i in 0 until amount){

            val height = abs(max-min)/amount
            val startHeight = if(min<=max)
                min + i* height
            else
                min - (i+1)*height

            list.add(SpookyWall(4.0, 1.0 / amount, 0.5, height, startHeight, i.toDouble() / amount))
        }
        return list
    }
}

/** draws a line given a centerPoint, an angle and a radius */
object CyanLine: OldWallStructure() {
    override val name = "CyanLine"
    override val mirror  = false
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        spookyWallList.clear()
        val degree = oldParameters.customParameters.getDoubleOrElse(0,0.0)
        val length = oldParameters.customParameters.getDoubleOrElse(1,1.0)
        val cx = oldParameters.customParameters.getDoubleOrElse(2,0.0)
        val cy = oldParameters.customParameters.getDoubleOrElse(3,2.0)

        val dgr = degree / 360 * (2* PI)
        val defaultAmount = ((cos(dgr)*sin(dgr)).pow(2)*200 +1).toInt()

        val amount = oldParameters.customParameters.getIntOrElse(4, defaultAmount)

        val x1 = (cx + cos(dgr))*length
        val x2 = (cx - cos(dgr))*length
        val y1 = (cy + sin(dgr))*length
        val y2 = (cy - sin(dgr))*length

        spookyWallList.addAll(
            line(x1, x2, y1, y2, 0.0, 0.0, amount)
        )
        return spookyWallList
    }
}

/** Line */
object Line: OldWallStructure() {
    override val name = "Line"
    override val mirror  = false
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        spookyWallList.clear()

        //all parameters
        val x1 = oldParameters.customParameters.getDoubleOrElse(0,-2.0)
        val x2 = oldParameters.customParameters.getDoubleOrElse(1,2.0)
        val y1  = oldParameters.customParameters.getDoubleOrElse(2,0.0)
        val y2 = oldParameters.customParameters.getDoubleOrElse(3,0.0)
        val z1 = oldParameters.customParameters.getDoubleOrElse(4,0.0)
        val z2 = oldParameters.customParameters.getDoubleOrElse(5,1.0)

        val amount = oldParameters.customParameters.getOrNull(6)?.toInt()
        val duration = oldParameters.customParameters.getOrNull(7)?.toDouble()

        spookyWallList.addAll(line(x1, x2, y1, y2, z1, z2, amount, duration))

        return spookyWallList
    }
}

/** mirroredLine */
object MirroredLine: OldWallStructure() {
    override val name = "MirroredLine"
    override val mirror  = true
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        spookyWallList.clear()

        //all parameters
        val x1 = oldParameters.customParameters.getDoubleOrElse(0,-2.0)
        val x2 = oldParameters.customParameters.getDoubleOrElse(1,2.0)
        val y1  = oldParameters.customParameters.getDoubleOrElse(2,0.0)
        val y2 = oldParameters.customParameters.getDoubleOrElse(3,0.0)
        val z1 = oldParameters.customParameters.getDoubleOrElse(4,0.0)
        val z2 = oldParameters.customParameters.getDoubleOrElse(5,0.0)

        val amount = oldParameters.customParameters.getOrNull(6)?.toInt()
        val duration = oldParameters.customParameters.getOrNull(7)?.toDouble()

        spookyWallList.addAll(line(x1, x2, y1, y2, z1, z2, amount, duration))

        return spookyWallList
    }
}
/** Curve Object - when called, creates a example curve */
object Curve: OldWallStructure() {
    override val name = "Curve"
    override val mirror = false
    override val spookyWallList = arrayListOf<SpookyWall>()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        var p0: Point
        var p1: Point
        var p2: Point
        var p3: Point
        val amount =oldParameters.customParameters.getIntOrElse(0,8)
        val syntax = "The Syntax is /bw curve -- \$amount startPoint \$x \$y\$z endPoint \$x \$y\$z p2 \$x \$y\$z p3 \$x \$y\$z -- notice how p0-p2 must be hardcoded"
        if (oldParameters.customParameters.getOrNull(1) != "startPoint") throw Exception(syntax)
        if (oldParameters.customParameters.getOrNull(5) != "endPoint") throw Exception(syntax)
        if (oldParameters.customParameters.getOrNull(9) != "p2") throw Exception(syntax)
        if (oldParameters.customParameters.getOrNull(13) != "p3") throw Exception(syntax)
        with(oldParameters.customParameters){
            try {
                p0= Point(get(2).toDouble(), get(3).toDouble(), get(4).toDouble())
                p1= Point(get(6).toDouble(), get(7).toDouble(), get(8).toDouble())
                p2= Point(get(10).toDouble(), get(11).toDouble(), get(12).toDouble())
                p3= Point(get(14).toDouble(), get(15).toDouble(), get(16).toDouble())
            }catch (e:Exception){
                println("Something is wrong with your curve, skipping")
                return arrayListOf()
            }
        }
        return curve(p0, p1, p2, p3, amount)
    }
}
/** MirroredCurve Object - when called, creates a mirrored Curve */
object MirroredCurve: OldWallStructure() {
    override val name = "MirroredCurve"
    override val mirror = true
    override val spookyWallList = arrayListOf<SpookyWall>()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        var p0: Point
        var p1: Point
        var p2: Point
        var p3: Point
        val amount =oldParameters.customParameters.getIntOrElse(0,8)
        val syntax = "The Syntax is /bw curve -- \$amount startPoint \$x \$y\$z endPoint \$x \$y\$z p2 \$x \$y\$z p3 \$x \$y\$z -- while your options were ${oldParameters.customParameters} notice how p0-p2 must be hardcoded"
        if (oldParameters.customParameters.getOrNull(1) != "startPoint") throw Exception(syntax)
        if (oldParameters.customParameters.getOrNull(5) != "endPoint") throw Exception(syntax)
        if (oldParameters.customParameters.getOrNull(9) != "p2") throw Exception(syntax)
        if (oldParameters.customParameters.getOrNull(13) != "p3") throw Exception(syntax)
        with(oldParameters.customParameters){
            try {
                p0= Point(get(2).toDouble(), get(3).toDouble(), get(4).toDouble())
                p1= Point(get(6).toDouble(), get(7).toDouble(), get(8).toDouble())
                p2= Point(get(10).toDouble(), get(11).toDouble(), get(12).toDouble())
                p3= Point(get(14).toDouble(), get(15).toDouble(), get(16).toDouble())
            }catch (e:Exception){
                println("Something is wrong with your curve, skipping")
                return arrayListOf()
            }
        }
        return curve(p0, p1, p2, p3, amount)
    }
}

/** RandomBox Object - when called, creates a random box with the given amount per tick and the given ticks per beat */
object RandomBox: OldWallStructure() {
    override val name = "RandomBox"
    override val mirror = false
    override val spookyWallList = arrayListOf<SpookyWall>()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        val list = arrayListOf<SpookyWall>()
        val amountPerTick = oldParameters.customParameters.getIntOrElse(0,4)
        val amountOfTicks = oldParameters.customParameters.getIntOrElse(1,4)
        val wallAmountPerWall = oldParameters.customParameters.getIntOrElse(2,8)

        val allSpookyWalls: ArrayList<SpookyWall> = getBoxList(wallAmountPerWall)

        for(start in 0 until amountOfTicks){
            val tempList = ArrayList(allSpookyWalls.map { it.copy() })
            repeat(amountPerTick){
                val w = tempList.random()
                w.startTime = start.toDouble()/amountOfTicks
                list.add(w)
                tempList.remove(w)
            }
        }
        return list
    }
}/** RandomBoxLine Object - when called, creates a random box with the given amount per tick and the given ticks per beat */
object RandomBoxLine: OldWallStructure() {
    override val name = "RandomBoxLine"
    override val mirror = false
    override val spookyWallList = arrayListOf<SpookyWall>()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        val list = arrayListOf<SpookyWall>()
        val amountPerTick = oldParameters.customParameters.getIntOrElse(0,8)
        val amountOfTicks = oldParameters.customParameters.getIntOrElse(1,4)
        val wallAmountPerWall = oldParameters.customParameters.getIntOrElse(2,32)

        val allSpookyWalls: ArrayList<SpookyWall> = getBoxList(wallAmountPerWall)

        for(start in 0 until amountOfTicks){
            val tempList = ArrayList(allSpookyWalls.map { it.copy() })
            repeat(amountPerTick){
                val w = tempList.random()
                w.startTime = start.toDouble()/amountOfTicks
                w.height = 0.0
                w.width = 0.0
                list.add(w)
                tempList.remove(w)
            }
        }
        return list
    }
}

/** gets very small noise in the area -4 .. 4 */
object RandomNoise: OldWallStructure() {
    override val mirror = false
    override val name = "RandomNoise"
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        spookyWallList.clear()
        val intensity = try { oldParameters.customParameters[0].toInt() } catch (e:Exception){ 5 }
        repeat(intensity){
            val tempO = SpookyWall(
                startRow = Random.nextDouble(-4.0, 4.0),
                duration = 0.01,
                width = 0.01,
                height = 0.01,
                startHeight = Random.nextDouble(4.0),
                startTime = Random.nextDouble()
            )
            spookyWallList.add(tempO)
        }
        return spookyWallList
    }
}

/** gets very small noise in the area -30 .. 30 */
object BroadRandomNoise: OldWallStructure() {
    override val mirror = false
    override val name = "BroadRandomNoise"
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        spookyWallList.clear()
        val intensity = try { oldParameters.customParameters[0].toInt() } catch (e:Exception){ 5 }
        repeat(intensity){
            val tempO = SpookyWall(
                startRow = Random.nextDouble(-50.0, 50.0),
                duration = 0.01,
                width = 0.01,
                height = 0.01,
                startHeight = Random.nextDouble(4.0),
                startTime = Random.nextDouble()
            )
            spookyWallList.add(tempO)
        }
        return spookyWallList
    }
}

/** random blocks to the right and left */
object RandomBlocks: OldWallStructure() {
    override val mirror = false
    override val name = "RandomBlocks"
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        spookyWallList.clear()
        val duration = oldParameters.customParameters.getDoubleOrElse(0,4.0)
        for(i in 0 until duration.toInt()){
            spookyWallList.add(
                SpookyWall(
                    Random.nextDouble(10.0, 20.0),
                    Random.nextDouble(0.5),
                    Random.nextDouble(2.0),
                    Random.nextDouble(2.0),
                    0.0,
                    i.toDouble()
                )
            )
            spookyWallList.add(
                SpookyWall(
                    Random.nextDouble(-20.0, -10.0),
                    Random.nextDouble(0.5),
                    Random.nextDouble(2.0),
                    Random.nextDouble(2.0),
                    0.0,
                    i.toDouble()
                )
            )
        }
        return spookyWallList
    }
}

/** random blocks to the right and left */
object RandomFastBlocks: OldWallStructure() {
    override val mirror = false
    override val name = "RandomFastBlocks"
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        spookyWallList.clear()
        val duration = oldParameters.customParameters.getDoubleOrElse(0,4.0)
        for(i in 0 until duration.toInt()){
            spookyWallList.add(
                SpookyWall(
                    Random.nextDouble(10.0, 20.0),
                    -2.0,
                    Random.nextDouble(2.0),
                    Random.nextDouble(2.0),
                    0.0,
                    i.toDouble()
                )
            )
            spookyWallList.add(
                SpookyWall(
                    Random.nextDouble(-20.0, -10.0),
                    -2.0,
                    Random.nextDouble(2.0),
                    Random.nextDouble(2.0),
                    0.0,
                    i.toDouble()
                )
            )
        }
        return spookyWallList
    }
}

/** gets randomLines, default on the floor */
object RandomLines: OldWallStructure() {
    //todo TEST
    override val mirror: Boolean = false
    override val name: String = "randomLines"
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        //getting the variables or the default values
        val count = try { oldParameters.customParameters[0].toInt() } catch (e:Exception){ 1 }
        val intensity = try { oldParameters.customParameters[1].toInt() } catch (e:Exception){ 4 }
        spookyWallList.clear()

        var x:Double
        for(i in 1..count){
            //adjusting the starting x, splitting it evenly among the count
            x = Random.nextDouble(-4.0 , 4.0)

            //for each wall intensity
            for(j in 1..intensity){
                spookyWallList.add(SpookyWall(x, 1.0 / intensity, 0.05, 0.05, 0.0, j.toDouble() / intensity))

                //randomly changes lines, adjusts x when doing so
                if (Random.nextInt(0, sqrt(count.toDouble()).roundToInt()) == 0){
                    val nX = Random.nextDouble(-4.0,4.0)
                    val stRow = if(nX > x) x else nX
                    val stWidth = nX-x
                    val stTime = j.toDouble()/intensity + 1.0/intensity
                    spookyWallList.add(SpookyWall(stRow, 0.0005, stWidth, 0.05, 0.0, stTime))
                    x = nX
                }
            }
        }
        return spookyWallList
    }
}

/** gets random side walls, default on the floor */
object RandomSideLines: OldWallStructure() {
    //todo TEST
    override val mirror: Boolean = true
    override val name: String = "randomSideLines"
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        //getting the variables or the default values
        val count = try { oldParameters.customParameters[0].toInt() } catch (e:Exception){ 1 }
        val intensity = try { oldParameters.customParameters[1].toInt() } catch (e:Exception){ 4 }
        spookyWallList.clear()

        var x:Double
        for(i in 1..count){
            //adjusting the starting x, splitting it evenly among the count
            x = Random.nextDouble(0.0 , 4.0)

            //for each wall intensity
            for(j in 1..intensity){
                spookyWallList.add(SpookyWall(4.0, 1.0 / intensity, 0.05, 0.05, x, j.toDouble() / intensity))

                //randomly changes lines, adjusts x when doing so
                if (Random.nextInt(0, sqrt(count.toDouble()).roundToInt()) == 0){
                    val nX = Random.nextDouble(0.0,4.0)
                    val stHeight = if(nX > x) x else nX
                    val height = abs(nX-x)
                    val stTime = j.toDouble()/intensity + 1.0/intensity
                    spookyWallList.add(SpookyWall(4.0, 0.0005, 0.05, height, stHeight, stTime))
                    x = nX
                }
            }
        }
        return spookyWallList
    }
}

/** gets text */
object Text: OldWallStructure() {
    override val name: String = "Text"
    override val mirror: Boolean = false
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        val list = arrayListOf<SpookyWall>()
        val text = oldParameters.customParameters.getOrNull(0)?:""
        val gap = oldParameters.customParameters.getDoubleOrElse(1,2.5)
        val midX = oldParameters.customParameters.getDoubleOrElse(2,0.0)
        var x=  midX-(text.length-1) * gap / 2 - gap/2
        for(c in text){
            val tempList =OldWallStructureManager.getWallList(OldParameters(name = c.toString()))
            tempList.forEach { it.startRow += x }
            x+=gap
            list.addAll(tempList)
        }
        return list
    }
}

/** LaneShifter Object - when called, creates an array Lines between the 4 given Points */
object LineShifter: OldWallStructure() {
    override val name = "LineShifter"
    override val mirror = false
    override val spookyWallList = arrayListOf<SpookyWall>()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        val list = arrayListOf<SpookyWall>()
        val amount = oldParameters.customParameters.getIntOrElse(0,4)
        val p1x1 =oldParameters.customParameters.getDoubleOrElse(1,-4.0)
        val p1y1 =oldParameters.customParameters.getDoubleOrElse(2,-4.0)
        val p1x2 =oldParameters.customParameters.getDoubleOrElse(3,-4.0)
        val p1y2 =oldParameters.customParameters.getDoubleOrElse(4,-4.0)
        val p2x1 =oldParameters.customParameters.getDoubleOrElse(5,-4.0)
        val p2y1 =oldParameters.customParameters.getDoubleOrElse(6,-4.0)
        val p2x2 =oldParameters.customParameters.getDoubleOrElse(7,-4.0)
        val p2y2 =oldParameters.customParameters.getDoubleOrElse(8,-4.0)
        var tempx1 = p1x1
        var tempx2 = p1x2
        var tempy1 =p1y1
        var tempy2 = p1y2

        for (i in 0 until amount){
            list.addAll(
                line(
                    tempx1,
                    tempx2,
                    tempy1,
                    tempy2,
                    i.toDouble() / amount,
                    i.toDouble() / amount,
                    null,
                    1.0 / amount
                )
            )
            tempx1 += (p2x1-p1x1)/amount
            tempx2 += (p2x2-p1x2)/amount
            tempy1 += (p2y1-p1y1)/amount
            tempy2 += (p2y2-p1y2)/amount
        }
        return list
    }
}


object SideWave: OldWallStructure(){
    override val name = "SideWave"
    override val mirror = true
    override val spookyWallList: ArrayList<SpookyWall> = arrayListOf()
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        val list = arrayListOf<SpookyWall>()
        val max = oldParameters.customParameters.getIntOrElse(0,8)
        for(i in 0 until (max)){
            val y = i/max*(2* PI)
            val nY = (i+1)/max*(2* PI)

            list.add(
                SpookyWall(
                    duration = 1 / max.toDouble(),
                    height = abs(cos(nY) - cos(y)),
                    startHeight = 1 - cos(y),
                    startRow = 3.0,
                    width = 0.5,
                    startTime = i / max.toDouble()
                )
            )
        }
        return list
    }
}

/** the default savedStructures the asset file uses */
data class CustomOldWallStructure(

    @SerializedName("name")
    override val name: String,

    @SerializedName("mirror")
    override val mirror: Boolean,

    @SerializedName("WallList")
    override val spookyWallList: ArrayList<SpookyWall>

): OldWallStructure() {
    override fun getWallList(oldParameters: OldParameters): ArrayList<SpookyWall> {
        return ArrayList(spookyWallList.map { it.copy() })
    }

    override fun toString(): String {
        var text="\n\tCustomWallStructure(\n"
        text+="\t\t\"$name\",\n"
        text+="\t\t$mirror,\n"
        text+="\t\tarrayListOf("
        for (wall in spookyWallList){
            text+="\n\t\t$wall,"
        }
        text = text.removeSuffix(",")
        text+="\n\t))"
        return text
    }
}
