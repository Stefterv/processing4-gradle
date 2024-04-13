package org.processing.test

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import javax.swing.Box
import javax.swing.SwingUtilities


fun main() = application{
    Window(onCloseRequest = ::exitApplication, title = "KotlinProject") {
        Text("Hello, World!")
    }
}

@Composable
fun App() {
    Text("Hello, World from Jetpack Compose!" + System.getProperty("compose.application.resources.dir"))
}

fun Start(box: Box){
    SwingUtilities.invokeLater {
        val composePanel = ComposePanel().apply {
            setContent {
                App()
            }
        }
        box.apply {
            add(composePanel)
//            setSize(400, 200)
//            isVisible = true
        }
    }
}