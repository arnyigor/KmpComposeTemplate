package data.repository

import database.dao.UserDao
import domain.model.User
import domain.model.toDomain
import domain.model.toEntity
import domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUsers(forceRefresh: Boolean): Result<List<User>> {
        return try {
            val users = userDao.getAll().map { it.toDomain() }

            // Если данных нет или требуется обновление, загружаем mock-данные
            if (users.isEmpty() || forceRefresh) {
                val mockUsers = generateMockUsers()
                userDao.insertAll(mockUsers.map { it.toEntity() })
                return Result.success(mockUsers)
            }

            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(id: String): Result<User?> {
        return try {
            val user = userDao.getById(id)?.toDomain()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observeUsers(): Flow<List<User>> {
        return userDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeUserById(id: String): Flow<User?> {
        return userDao.observeById(id).map { it?.toDomain() }
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            userDao.insert(user.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            userDao.deleteById(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateMockUsers(): List<User> = listOf(
        User("1", "Alice Johnson", "alice@example.com"),
        User("2", "Bob Smith", "bob@example.com"),
        User("3", "Charlie Brown", "charlie@example.com"),
        User("4", "Diana Prince", "diana@example.com"),
        User("5", "Eve Adams", "eve@example.com")
    )
}
