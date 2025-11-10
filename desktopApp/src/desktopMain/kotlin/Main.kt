import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import components.detail.DefaultUserDetailComponent
import components.list.DefaultUserListComponent
import components.root.DefaultRootComponent
import di.commonModules
import di.desktopModules
import domain.repository.UserRepository
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import ui.RootContent

fun maintest() = application {
    startKoin {
        modules(commonModules + desktopModules)
    }

    application {
        val windowState = rememberWindowState(
            placement = WindowPlacement.Maximized,
            position = WindowPosition(Alignment.Center),
        )
        val lifecycle = remember { LifecycleRegistry() }


        val root = DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle),
            userListComponentFactory = { context, onUserSelected ->
                val repo = GlobalContext.get().get<UserRepository>()
                DefaultUserListComponent(context, repo, onUserSelected)
            },
            userDetailComponentFactory = { context, userId, onBack ->
                val repo = GlobalContext.get().get<UserRepository>()
                DefaultUserDetailComponent(context, userId, repo, onBack)
            }
        )

        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            title = "KMP Desktop App",
            state = windowState
        ) {
            MaterialTheme {
                Surface {
                    RootContent(root)
                }
            }
        }
    }
}



fun main() = application {
    var isOpen by remember { mutableStateOf(true) }

    if (isOpen) {
        Window(
            onCloseRequest = { isOpen = false },
            title = "Test Desktop App"
        ) {
            MaterialTheme {
                Button(onClick = { println("Button clicked!") }) {
                    Text("Hello Desktop!")
                }
            }
        }
    }
}