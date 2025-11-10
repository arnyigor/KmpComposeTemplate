package components.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import domain.model.User
import domain.repository.UserRepository
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface UserListComponent {
    val state: StateFlow<UserListState>

    fun onUserClick(userId: String)
    fun onRefresh()
    fun onAddUserClick()
}

data class UserListState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class DefaultUserListComponent(
    componentContext: ComponentContext,
    private val userRepository: UserRepository,
    private val onUserSelected: (String) -> Unit
) : UserListComponent, ComponentContext by componentContext {

    private val scope = coroutineScope(SupervisorJob())

    private val _state = MutableStateFlow(UserListState())
    override val state: StateFlow<UserListState> = _state.asStateFlow()

    init {
        loadUsers()
        observeUsers()
    }

    override fun onUserClick(userId: String) {
        onUserSelected(userId)
    }

    override fun onRefresh() {
        loadUsers(forceRefresh = true)
    }

    override fun onAddUserClick() {
        // TODO: Implement add user functionality
    }

    private fun loadUsers(forceRefresh: Boolean = false) {
        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            userRepository.getUsers(forceRefresh)
                .onSuccess { users ->
                    _state.update {
                        it.copy(users = users, isLoading = false)
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error"
                        )
                    }
                }
        }
    }

    private fun observeUsers() {
        scope.launch {
            userRepository.observeUsers().collect { users ->
                _state.update { it.copy(users = users) }
            }
        }
    }
}
