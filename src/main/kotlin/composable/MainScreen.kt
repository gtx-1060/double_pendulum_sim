package composable

import Manager
import Pendulum
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    var screenSize by remember { mutableStateOf(IntSize.Zero) }
    val centerX = { screenSize.width.toFloat() / 2 }
    val metresToPix = { v: Float -> v * 150}
    var x1 by remember { mutableStateOf(Manager.instance.rPend.x) }
    var y1 by remember { mutableStateOf(Manager.instance.rPend.y) }
    var x2 by remember { mutableStateOf(Manager.instance.cPend.x) }
    var y2 by remember { mutableStateOf(Manager.instance.cPend.y) }

    var rPendEditable by remember { mutableStateOf(Manager.instance.rPend) }
    var cPendEditable by remember { mutableStateOf(Manager.instance.cPend) }

    remember {
        scope.launch {
            Manager.instance.updateFlow().collect() {
                x1 = it[0].x
                y1 = it[0].y
                x2 = it[1].x
                y2 = it[1].y
            }
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth().weight(0.2f).padding(10.dp, 10.dp, 10.dp, 0.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            TextField(
                modifier = Modifier.weight(1f),
                value = rPendEditable.mass.toString(),
                label = { Text("mass of the first pendulum (kg)") },
                onValueChange = {
                    val mass = it.toFloatOrNull() ?: return@TextField
                    rPendEditable = Pendulum(rPendEditable.radius, mass, rPendEditable.angle)
                }
            )
            TextField(
                modifier = Modifier.weight(1f),
                value = cPendEditable.mass.toString(),
                label = { Text("mass of the second pendulum (kg)") },
                onValueChange = {
                    val mass = it.toFloatOrNull() ?: return@TextField
                    cPendEditable = Pendulum(cPendEditable.radius, mass, cPendEditable.angle)
                }
            )
            TextField(
                modifier = Modifier.weight(1f),
                value = rPendEditable.angle.toString(),
                label = { Text("start angle 1st pendulum (rad)") },
                onValueChange = {
                    val angle = it.toFloatOrNull() ?: return@TextField
                    rPendEditable = Pendulum(rPendEditable.radius, rPendEditable.mass, angle)
                }
            )
            TextField(
                modifier = Modifier.weight(1f),
                value = cPendEditable.angle.toString(),
                label = { Text("start angle 2st pendulum (rad)") },
                onValueChange = {
                    val angle = it.toFloatOrNull() ?: return@TextField
                    cPendEditable = Pendulum(cPendEditable.radius, cPendEditable.mass, angle)
                }
            )
        }

        Button(
            modifier = Modifier.weight(0.1f),
            content = { Text("REFRESH") },
            onClick = {
                Manager.instance.updateConfiguration(rPendEditable, cPendEditable)
            }
        )

        Box(Modifier.fillMaxWidth().weight(0.8f).padding(15.dp).onGloballyPositioned { screenSize = it.size }) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val pend1 = Offset(centerX()+metresToPix(x1), metresToPix(y1))
                val pend2 = Offset(pend1.x+metresToPix(x2), pend1.y+metresToPix(y2))

                drawLine(
                    Color.Blue,
                    Offset(centerX(), 0f),
                    pend1
                )
                drawLine(
                    Color.Blue,
                    pend1,
                    pend2
                )

                drawCircle(Color.Magenta, 20f, pend1)
                drawCircle(Color.Green, 20f, pend2)

            }
        }
    }
}