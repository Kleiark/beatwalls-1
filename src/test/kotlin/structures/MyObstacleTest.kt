package structures

import org.junit.Test

import org.junit.Assert.*

class MyObstacleTest {

    @Test
    fun to_obstacle() {
    }

    @Test
    fun mirror() {
        val  actual  = MyObstacle(2.0,1.0,1.0,1.5,0.5,0.2).mirror()
        val expected = MyObstacle(2.0,1.0,1.0,-2.0,0.5,0.2)
        assertEquals(actual.toString(),expected.toString())
    }

    @Test
    fun adjustEmptyParameters() {
        val p = Parameters("emptyParaMeter")
        val  expected  = MyObstacle(2.0,1.0,1.0,1.5,0.5,0.2)
        val actual = expected.adjustParameters(p)
        assertEquals(expected.toString(),actual.toString())
    }

    @Test
    fun adjustNormalParameter() {
        val p = Parameters("test 2")
        val  expected  = MyObstacle(4.0,1.0,1.0,1.5,0.5,0.4)
        val  obs  = MyObstacle(2.0,1.0,1.0,1.5,0.5,0.2)
        val actual = obs.adjustParameters(p)
        assertEquals(expected.toString(),actual.toString())
    }

    @Test
    fun adjustNotNormalParameters() {
        val p = Parameters("test 1 5 0.2 1 2 3 4 5 6")
        val expected  = MyObstacle(3.0,3.0,4.0,5.5,5.5,6.2)
        val obs  = MyObstacle(2.0,1.0,1.0,1.5,0.5,0.2)
        val actual = obs.adjustParameters(p)
        assertEquals(expected.toString(),actual.toString())
    }
}