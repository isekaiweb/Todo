package com.my.todo.data.di

import com.my.todo.data.repo.DefaultTodoRepo
import com.my.todo.data.repo.TodoRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsTodoRepo(
        repo: DefaultTodoRepo
    ): TodoRepo
}