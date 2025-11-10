package com.arny.kmpprogress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.arkivanov.decompose.defaultComponentContext
import components.detail.DefaultUserDetailComponent
import components.list.DefaultUserListComponent
import components.root.DefaultRootComponent
import org.koin.android.ext.android.get
import ui.RootContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val root = DefaultRootComponent(
            componentContext = defaultComponentContext(),
            userListComponentFactory = { context, onUserSelected ->
                DefaultUserListComponent(context, get(), onUserSelected)
            },
            userDetailComponentFactory = { context, userId, onBack ->
                DefaultUserDetailComponent(context, userId, get(), onBack)
            }
        )

        setContent {
            MaterialTheme {
                Surface {
                    RootContent(root)
                }
            }
        }
    }
}
