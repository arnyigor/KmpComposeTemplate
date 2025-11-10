package ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import components.root.RootComponent
import ui.detail.UserDetailContent
import ui.list.UserListContent

@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.stack,
        animation = stackAnimation(slide())
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.UserList -> UserListContent(child.component)
            is RootComponent.Child.UserDetail -> UserDetailContent(child.component)
        }
    }
}
