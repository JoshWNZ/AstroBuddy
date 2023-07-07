package com.pilot.astrobuddy.di

import android.app.Application
import androidx.room.Room
import com.pilot.astrobuddy.common.Constants
import com.pilot.astrobuddy.data.local.AstroBuddyDatabase
import com.pilot.astrobuddy.data.local.OMLocationDao
import com.pilot.astrobuddy.data.remote.OpenMeteoApi
import com.pilot.astrobuddy.data.remote.OpenMeteoSearchApi
import com.pilot.astrobuddy.data.remote.WeatherApi
import com.pilot.astrobuddy.data.repository.ForecastRepositoryImpl
import com.pilot.astrobuddy.data.repository.SavedLocationRepositoryImpl
import com.pilot.astrobuddy.domain.repository.ForecastRepository
import com.pilot.astrobuddy.domain.repository.SavedLocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesWeatherApi() : WeatherApi {
        return Retrofit.Builder()
            .baseUrl(Constants.WA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun providesOpenMeteoApi() : OpenMeteoApi {
        return Retrofit.Builder()
            .baseUrl(Constants.OM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoApi::class.java)
    }

    @Provides
    @Singleton
    fun providesOpenMeteoSearchApi() : OpenMeteoSearchApi {
        return Retrofit.Builder()
            .baseUrl(Constants.OMS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenMeteoSearchApi::class.java)
    }

    @Provides
    @Singleton
    fun providesForecastRepository(apiWA: WeatherApi, apiOM: OpenMeteoApi, apiSOM: OpenMeteoSearchApi): ForecastRepository{
        return ForecastRepositoryImpl(apiWA,apiOM, apiSOM)
    }

    @Provides
    @Singleton
    fun providesSavedLocRepository(db: AstroBuddyDatabase): SavedLocationRepository{
        return SavedLocationRepositoryImpl(db.locDao)
    }

    @Provides
    @Singleton
    fun provideAstroBuddyDatabase(app: Application): AstroBuddyDatabase{
        return Room.databaseBuilder(
            app, AstroBuddyDatabase::class.java, "astrobuddy_db"
        ).fallbackToDestructiveMigration().build()
    }
}