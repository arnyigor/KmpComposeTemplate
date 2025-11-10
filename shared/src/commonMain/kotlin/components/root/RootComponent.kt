package components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import components.detail.UserDetailComponent
import components.list.UserListComponent
import kotlinx.serialization.Serializable

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked()

    sealed class Child {
        data class UserList(val component: UserListComponent) : Child()
        data class UserDetail(val component: UserDetailComponent) : Child()
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val userListComponentFactory: (ComponentContext, (String) -> Unit) -> UserListComponent,
    private val userDetailComponentFactory: (ComponentContext, String, () -> Unit) -> UserDetailComponent
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.UserList,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.UserList -> RootComponent.Child.UserList(
                userListComponentFactory(
                    componentContext,
                    { userId -> navigation.push(Config.UserDetail(userId)) }
                )
            )
            is Config.UserDetail -> RootComponent.Child.UserDetail(
                userDetailComponentFactory(
                    componentContext,
                    config.userId,
                    { navigation.pop() }
                )
            )
        }

    override fun onBackClicked() {
        navigation.pop()
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object UserList : Config

        @Serializable
        data class UserDetail(val userId: String) : Config
    }
}
