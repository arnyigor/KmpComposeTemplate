package components.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import domain.model.User
import domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface UserDetailComponent {
    val state: StateFlow<UserDetailState>

    fun onBackClick()
    fun onDeleteClick()
}

data class UserDetailState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class DefaultUserDetailComponent(
    componentContext: ComponentContext,
    private val userId: String,
    private val userRepository: UserRepository,
    private val onNavigateBack: () -> Unit
) : UserDetailComponent, ComponentContext by componentContext {

    private val scope = coroutineScope(SupervisorJob())

    private val _state = MutableStateFlow(UserDetailState())
    override val state: StateFlow<UserDetailState> = _state.asStateFlow()

    init {
        loadUser()
        observeUser()
    }

    override fun onBackClick() {
        onNavigateBack()
    }

    override fun onDeleteClick() {
        scope.launch {
            userRepository.deleteUser(userId)
                .onSuccess {
                    withContext(Dispatchers.Main.immediate) {
                        onNavigateBack()
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(error = exception.message ?: "Failed to delete user")
                    }
                }
        }
    }

    private fun loadUser() {
        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            userRepository.getUserById(userId)
                .onSuccess { user ->
                    _state.update {
                        it.copy(user = user, isLoading = false)
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "User not found"
                        )
                    }
                }
        }
    }

    private fun observeUser() {
        scope.launch {
            userRepository.observeUserById(userId).collect { user ->
                _state.update { it.copy(user = user) }
            }
        }
    }
}
