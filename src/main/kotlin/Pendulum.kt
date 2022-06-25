import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class Pendulum(
    val radius: Float,
    val mass: Float,
    var angle: Float
) {
    var angleVel: Float = 0f
    private set

    fun updateAngleAccel(accel: Float) {
        angleVel += accel
        angle = (angle+angleVel).mod(6.28f)
    }

    val x: Float
        get() = radius * sin(angle)

    val y: Float
        get() = radius * cos(angle)

    override fun toString(): String {
        return "Pendulum(radius=$radius, mass=$mass, angle=$angle, angleVel=$angleVel)"
    }



}