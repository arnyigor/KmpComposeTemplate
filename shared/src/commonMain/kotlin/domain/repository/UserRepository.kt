package domain.repository

import domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(forceRefresh: Boolean = false): Result<List<User>>
    suspend fun getUserById(id: String): Result<User?>
    fun observeUsers(): Flow<List<User>>
    fun observeUserById(id: String): Flow<User?>
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun deleteUser(userId: String): Result<Unit>
}
