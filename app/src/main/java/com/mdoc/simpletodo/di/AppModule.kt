package com.mdoc.simpletodo.di

import android.app.Application
import androidx.room.Room
import com.mdoc.simpletodo.data.TodoDatabase
import com.mdoc.simpletodo.data.TodoRepository
import com.mdoc.simpletodo.data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesTodoDatabase(app: Application): TodoDatabase{
        return Room.databaseBuilder(app, TodoDatabase::class.java, "todo_Db").build()
    }

    @Provides
    @Singleton
    fun providesRepository(database: TodoDatabase): TodoRepository{
        return TodoRepositoryImpl(database.dao)
    }
}