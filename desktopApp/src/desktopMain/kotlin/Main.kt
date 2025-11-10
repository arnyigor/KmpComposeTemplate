import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import components.detail.DefaultUserDetailComponent
import components.list.DefaultUserListComponent
import components.root.DefaultRootComponent
import di.commonModules
import di.desktopModules
import domain.repository.UserRepository
import org.koin.core.context.startKoin
import ui.RootContent

fun main() {
    println("Starting Desktop App...")  // ✅ Debug вывод

    try {
        // Инициализация Koin
        startKoin {
            modules(
                commonModules + desktopModules
            )
        }
        println("Koin initialized")  // ✅ Debug

        val lifecycle = LifecycleRegistry()

        val root = DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle),
            userListComponentFactory = { context, onUserSelected ->
                val repo = org.koin.core.context.GlobalContext.get().get<UserRepository>()
                DefaultUserListComponent(context, repo, onUserSelected)
            },
            userDetailComponentFactory = { context, userId, onBack ->
                val repo = org.koin.core.context.GlobalContext.get().get<UserRepository>()
                DefaultUserDetailComponent(context, userId, repo, onBack)
            }
        )
        println("RootComponent created")  // ✅ Debug

        application {
            val windowState = rememberWindowState(
                width = 1200.dp,
                height = 800.dp
            )

            Window(
                onCloseRequest = ::exitApplication,
                title = "KMP Desktop App",
                state = windowState
            ) {
                println("Window created")  // ✅ Debug
                MaterialTheme {
                    Surface {
                        RootContent(root)
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        System.err.println("Error: ${e.message}")
    }
}
