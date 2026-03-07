package com.hufeng943.timetable.di

import android.content.Context
import com.hufeng943.timetable.data.PreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {
    @Provides
    @Singleton
    fun providePreferenceStorage(
        @ApplicationContext context: Context
    ): PreferenceStorage {
        return PreferenceStorage(context)
    }
}