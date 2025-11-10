package di

import data.repository.UserRepositoryImpl
import database.AppDatabase
import domain.repository.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single { AppDatabase.getInstance() }
    single { get<AppDatabase>().userDao() }

    singleOf(::UserRepositoryImpl) bind UserRepository::class
}

val commonModules = listOf(dataModule)