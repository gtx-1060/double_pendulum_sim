import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.*
import kotlin.math.*

class Manager {
    var rPend: Pendulum = Pendulum(1f, 1f, (PI / 3).toFloat())
    private set
    var cPend: Pendulum = Pendulum(1f, 1f, 0f)
    private set
    private var isWorking = false

    private val updatesPerSec = 60
    private val g get() = (9.8f / updatesPerSec / updatesPerSec)

    companion object {
        val instance = Manager()
    }

    fun start() {
        isWorking = true
    }

    fun stop() {
        isWorking = false
    }

    fun updateConfiguration(pend1: Pendulum, pend2: Pendulum) {
        rPend = pend1
        cPend = pend2
    }

    private fun match() {
        val acc1 = (-g * (2 * rPend.mass + cPend.mass) * sin(rPend.angle) -
                cPend.mass * g * sin(rPend.angle-2*cPend.angle) -
                2*sin(rPend.angle-cPend.angle)*cPend.mass *
                (cPend.angleVel.pow(2)*cPend.radius + rPend.angleVel.pow(2)*rPend.radius*cos(rPend.angle-cPend.angle))) /
                (rPend.radius*(2*rPend.mass + cPend.mass - cPend.mass*cos(2*rPend.angle-2*cPend.angle)))
        val acc2 = 2 * sin(rPend.angle-cPend.angle) * (
                rPend.angleVel.pow(2)*rPend.radius*(rPend.mass+cPend.mass) +
                        g*(cPend.mass+rPend.mass)*cos(rPend.angle) +
                        cPend.angleVel.pow(2)*cPend.radius*cPend.mass*cos(rPend.angle-cPend.angle)
                ) /
                (cPend.radius * (2*rPend.mass+cPend.mass-cPend.mass*cos(2*rPend.angle-2*cPend.angle)))

        rPend.updateAngleAccel(acc1)
        cPend.updateAngleAccel(acc2)
    }


    fun updateFlow() = flow {
        while (isWorking) {
            match()
            emit(listOf(rPend, cPend))
            delay((1000 / updatesPerSec).toLong())
        }
    }

}