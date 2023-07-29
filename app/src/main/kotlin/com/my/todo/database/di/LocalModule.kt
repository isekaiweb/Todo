package com.my.todo.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.my.todo.R
import com.my.todo.database.TodoDatabase
import com.my.todo.database.dao.TodoDao
import com.my.todo.database.model.TodoEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesTodoDatabase(
        @ApplicationContext context: Context,
        todoDao: TodoDao
    ): TodoDatabase = Room.databaseBuilder(
        context = context,
        klass = TodoDatabase::class.java,
        name = "todo_database"
    ).addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Executors.newSingleThreadExecutor().execute {
                fillWithStartingData(context, todoDao)
            }
        }
    }).build()

    private fun fillWithStartingData(context: Context, dao: TodoDao) {
        val jsonArray = loadJsonArray(context)
        try {
            if (jsonArray != null) {
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    dao.insertAll(
                        TodoEntity(
                            id = item.getInt("id"),
                            title = item.getString("title"),
                            description = item.getString("description"),
                            dueDate = item.getString("dueDate")
                        )
                    )
                }
            }
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
    }

    private fun loadJsonArray(context: Context): JSONArray? {
        val builder = StringBuilder()
        val `in` =
            context.resources.openRawResource(R.raw.todos)
        val reader = BufferedReader(InputStreamReader(`in`))
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            val json = JSONObject(builder.toString())
            return json.getJSONArray("todos")
        } catch (exception: IOException) {
            exception.printStackTrace()
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        return null
    }

    @Provides
    @Singleton
    fun providesTodoDao(
        database: TodoDatabase
    ): TodoDao = database.todoDao()
}