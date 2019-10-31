package assetFile

import com.github.spookyghost.beatwalls.errorExit
import mu.KotlinLogging
import structure.*
import kotlin.reflect.*
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.withNullability

//   ____  ____   __   ____  ____  ____
//  (  _ \(  __) / _\ (    \(  __)(  _ \
//   )   / ) _) /    \ ) D ( ) _)  )   /
//  (__\_)(____)\_/\_/(____/(____)(__\_)
//
// I basically wrote a new script language.
// Below is what needed to go automatically set all the values for all the parameters.

private val logger = KotlinLogging.logger {}

/**
 * reads in a String and returns the corresponding SongAssetFile
 */
fun parseAsset(s: String): ArrayList<WallStructure> {
    // creates the list of key-Value
    val lines = parseAssetString(s)
    val structures = parseStructures(lines)
    logger.info { "Read DifficultyAsset Succesfull with ${structures.size} structures" }
    return structures
}

/**
 * reads the Structures from the list
 */
fun parseStructures(mutableList: MutableList<Pair<String, String>>): ArrayList<WallStructure>{
    val list = arrayListOf<WallStructure>()

    for (i in 0 until mutableList.size){
        val definedStructures = list.filterIsInstance<Define>()
        if (mutableList[i].key().toDoubleOrNull() != null){
            val structName = mutableList[i].value().toLowerCase()
            val beat = mutableList[i].key().toDouble()
            val struct: WallStructure = findStructure(structName, definedStructures)

            struct.beat = beat

            //hacky
            if (struct is Define && structName != "define"){
                struct.isTopLevel = true
            }

            logger.info { "adding $structName" }
            list.add(struct)
        }else{
            readWallStructOptions(list.last(), mutableList[i], definedStructures)
        }
    }
    return list
}

fun adjustStruct(struct:WallStructure,beat:Double){
}

fun findStructure(name: String, definedStructure: List<Define>): WallStructure {
    val structName = name.toLowerCase()
    val specialStructs = WallStructure::class.sealedSubclasses
    val specialStructsNames = specialStructs.map { it.simpleName?.toLowerCase()?:""}

    val definedStructureNames = definedStructure.map { it.name.toLowerCase() }
    val struct: WallStructure

    // sets the struct
    struct = when (structName) {
        in definedStructureNames -> definedStructure.find { it.name.toLowerCase() == structName }!!.deepCopy()
        in specialStructsNames -> specialStructs.find { it.simpleName!!.toLowerCase() == structName }!!.createInstance()
        else -> {
            logger.info { "structure $structName not found" }
            EmptyWallStructure
        }
    }
    return struct
}

/**
 * fills the given structure with the option, if it exist
 */
fun readWallStructOptions(wallStructure: WallStructure, option: Pair<String, String>, definedStructure: List<Define>){
    if (wallStructure is EmptyWallStructure){
        return
    }

    val property = findProperty(wallStructure,option.key())

    if (property != null) {
        fillProperty(
            wallStructure = wallStructure,
            property = property,
            value = option.value(),
            definedStructure = definedStructure
        )
    }else{
        if( wallStructure is Define) {
            if(wallStructure.structures.firstOrNull() != null){
                readWallStructOptions(wallStructure.structures.first(), option, definedStructure)
            }
        }else{
            errorExit { "The WallStructure $wallStructure does not have the property ${option.key()}" }
        }
    }
}
fun findProperty(wallStructure: WallStructure, key:String): KProperty1<out WallStructure, Any?>? {
    val properties = wallStructure::class.memberProperties.toMutableList()
    return  properties.find { it.name.toLowerCase() ==key.toLowerCase() }

}

fun fillProperty(wallStructure: WallStructure, property: KProperty1<out WallStructure, Any?>, value: String , definedStructure: List<Define>){
    val valueType:Any?
    val type = property.returnType.withNullability(false)

    with (value){
        valueType = when (type) {
            Boolean::class.createType() -> toBoolean()
            Int::class.createType() -> toInt()
            Double::class.createType() -> toDouble()
            Double::class.createType() -> toDouble()
            String::class.createType() -> toString()
            Point::class.createType() -> toPoint()
            WallStructure::class.createType() -> toWallStructure(definedStructure)
            getWallListType() -> toWallStructureList(definedStructure)
            else -> null
        }
    }

    if (property is KMutableProperty<*>){
        property.setter.call(wallStructure,valueType)
    }
}

fun getWallListType(): KType {
    val l = WallStructure::class.createType()
    val k = KTypeProjection(type = l, variance = KVariance.INVARIANT)
    return List::class.createType(listOf(k))
}

/**
 * creates the list of key-value Pairs
 */
fun parseAssetString(s:String): MutableList<Pair<String, String>> =
    s
        .lines()
        .asSequence()
        .map { it.trim() }
        .filterNot { it.startsWith("#") || it.isEmpty() }
        .map { it.replace("\\t".toRegex(), "") }
        .map { it.split(":") }
        .map { it.map { l -> l.trim() } }
        .map { it[0].toLowerCase() to it.minus(it[0]).joinToString(":") }
        .filterNot{ it.first.isEmpty() || it.second.isEmpty()}
        .toMutableList()

/**
 * different name for componen1/component2
 */
fun Pair<String,String>.value(): String {
    return this.component2()
}
fun Pair<String,String>.key(): String {
    return this.component1()
}

/**
 * String to Point
 */
private fun String.toPoint(): Point {
    val values = this.split(",")
        .map { it.trim() }
        .map { it.toDoubleOrNull() }
    return Point(
        x = values.getOrNull(0)?:0.0,
        y = values.getOrNull(1)?:0.0,
        z = values.getOrNull(2)?:0.0
    )
}

/**
 * String to WallStructure
 */
private fun String.toWallStructure(definedStructure: List<Define>): WallStructure {
    return findStructure(this, definedStructure)
}

/**
 * String to List<WallStructure>
 */
private fun String.toWallStructureList(definedStructure: List<Define>): List<WallStructure>{
    return this
        .split(",")
        .map { it.trim() }
        .map { findStructure(it, definedStructure) }
}



