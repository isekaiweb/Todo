package com.my.todo.database.di

import android.content.Context
import androidx.room.Room
import com.my.todo.database.TodoDatabase
import com.my.todo.database.dao.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule{

    @Provides
    @Singleton
    fun providesTodoDatabase(
        @ApplicationContext context: Context,
    ): TodoDatabase = Room.databaseBuilder(
        context = context,
        klass = TodoDatabase::class.java,
        name = "todo_database"
    ).build()


    @Provides
    @Singleton
    fun providesTodoDao(
        database: TodoDatabase
    ): TodoDao = database.todoDao()
}