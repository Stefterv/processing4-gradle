package org.processing.test

import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import javax.swing.Box
import javax.swing.SwingUtilities

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComposeHelloWorld() {
    var open by remember { mutableStateOf(false) }

    Surface(
        onClick = {
           open = !open
        }
    ) {
        Text("Hello, World from Jetpack Compose!")
    }
    if(!open) return
    var isSubmenuShowing by remember { mutableStateOf(false) }
    var action by  remember { mutableStateOf("Last action: None") }
    var isOpen by remember { mutableStateOf(true) }

    Window(onCloseRequest = {
        open = false
    }, title = "Processing") {
        MenuBar {
            Menu("File", mnemonic = 'F') {
                Item("Copy", onClick = { action = "Last action: Copy" }, shortcut = KeyShortcut(Key.C, ctrl = true))
                Item(
                    "Paste",
                    onClick = { action = "Last action: Paste" },
                    shortcut = KeyShortcut(Key.V, ctrl = true)
                )
            }
            Menu("Actions", mnemonic = 'A') {
                CheckboxItem(
                    "Advanced settings",
                    checked = isSubmenuShowing,
                    onCheckedChange = {
                        isSubmenuShowing = !isSubmenuShowing
                    }
                )
                if (isSubmenuShowing) {
                    Menu("Settings") {
                        Item("Setting 1", onClick = { action = "Last action: Setting 1" })
                        Item("Setting 2", onClick = { action = "Last action: Setting 2" })
                    }
                }
                Separator()
                Item("About", icon = AboutIcon, onClick = { action = "Last action: About" })
                Item("Exit", onClick = { isOpen = false }, shortcut = KeyShortcut(Key.Escape), mnemonic = 'E')
            }
        }
        Text("Hello, World!")
    }
}
object AboutIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}

fun Start(box: Box){
    SwingUtilities.invokeLater {
        val composePanel = ComposePanel().apply {
            setContent {
                ComposeHelloWorld()
            }
        }
        box.apply {
            add(composePanel)
        }
    }
}