package com.pilot.astrobuddy.di

import android.content.Context
import com.pilot.astrobuddy.setings_store.SettingsStore
import com.pilot.astrobuddy.setings_store.SettingsStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Contains provider for dagger-hilt to inject SettingsStore
 */
@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {
    @Provides
    @Singleton
    fun provideSettingsStore(context: Context): SettingsStore {
        return SettingsStoreImpl(context)
    }
}