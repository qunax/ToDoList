package com.example.todolist.di

import android.app.Application
import androidx.room.Room
import com.example.todolist.data.data_source.TodoDatabase
import com.example.todolist.data.repository.TodoRepositoryImpl
import com.example.todolist.data.repository.UiConfigRepositoryImpl
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.domain.repository.UiConfigRepository
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
    fun provideTodoDatabase(app: Application) : TodoDatabase{
        return Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            "todo_db"
        ).build()
    }

//    @Provides
//    @Singleton
//    fun provideUiConfigRepository() : UiConfigRepository{
//        return UiConfigRepositoryImpl()
//    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase) : TodoRepository{
        return TodoRepositoryImpl(db.dao)
    }
}