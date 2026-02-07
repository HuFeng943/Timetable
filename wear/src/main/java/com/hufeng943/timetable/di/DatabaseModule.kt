package com.hufeng943.timetable.di

import android.content.Context
import androidx.room.Room
import com.hufeng943.timetable.shared.data.dao.TimetableDao
import com.hufeng943.timetable.shared.data.database.AppDatabase
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.data.repository.TimetableRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "timetable_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTimetableDao(db: AppDatabase): TimetableDao {
        return db.timetableDao()
    }

    @Provides
    @Singleton
    fun provideRepository(dao: TimetableDao): TimetableRepository {
        return TimetableRepositoryImpl(dao)
    }
}