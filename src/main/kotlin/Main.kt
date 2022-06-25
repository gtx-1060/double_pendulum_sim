import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import composable.MainScreen

fun main() = application {
    Window(onCloseRequest =
    {
        Manager.instance.stop()
        exitApplication()
    }
    ) {
        Manager.instance.start()
        MainScreen()
    }
}
