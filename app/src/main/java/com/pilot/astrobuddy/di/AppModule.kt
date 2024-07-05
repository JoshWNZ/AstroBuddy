package com.pilot.astrobuddy.di

//import com.pilot.astrobuddy.data.remote.WeatherApi
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.pilot.astrobuddy.common.Constants
import com.pilot.astrobuddy.data.local.AstroBuddyDatabase
import com.pilot.astrobuddy.data.local.AstroObjectDatabase
import com.pilot.astrobuddy.data.remote.OpenMeteoApi
import com.pilot.astrobuddy.data.remote.OpenMeteoSearchApi
import com.pilot.astrobuddy.data.repository.AstroEquipmentRepositoryImpl
import com.pilot.astrobuddy.data.repository.AstroObjectRepositoryImpl
import com.pilot.astrobuddy.data.repository.ForecastRepositoryImpl
import com.pilot.astrobuddy.data.repository.SavedLocationRepositoryImpl
import com.pilot.astrobuddy.data.repository.SavedObjectRepositoryImpl
import com.pilot.astrobuddy.domain.repository.AstroEquipmentRepository
import com.pilot.astrobuddy.domain.repository.AstroObjectRepository
import com.pilot.astrobuddy.domain.repository.ForecastRepository
import com.pilot.astrobuddy.domain.repository.SavedLocationRepository
import com.pilot.astrobuddy.domain.repository.SavedObjectRepository
import com.pilot.astrobuddy.domain.use_case.calculate_lightpollution.CalculateLightPollUseCase
import com.pilot.astrobuddy.setings_store.SettingsStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/*
Contains providers for dagger hilt to inject dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesOpenMeteoApi() : OpenMeteoApi {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//
//        val client = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.OM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.client(client)
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

    //repositories
    @Provides
    @Singleton
    fun providesForecastRepository(apiOM: OpenMeteoApi, apiSOM: OpenMeteoSearchApi, settingsStore: SettingsStore): ForecastRepository{
        return ForecastRepositoryImpl(apiOM, apiSOM, settingsStore)
    }

    @Provides
    @Singleton
    fun providesSavedLocRepository(db: AstroBuddyDatabase): SavedLocationRepository{
        return SavedLocationRepositoryImpl(db.locDao)
    }

    @Provides
    @Singleton
    fun providesAstroObjectRepository(db: AstroObjectDatabase): AstroObjectRepository{
        return AstroObjectRepositoryImpl(db.objDao)
    }

    @Provides
    @Singleton
    fun providesSavedObjectRepository(db: AstroBuddyDatabase): SavedObjectRepository {
        return SavedObjectRepositoryImpl(db.saveObjDao)
    }

    @Provides
    @Singleton
    fun providesAstroEquipmentRepository(db: AstroBuddyDatabase): AstroEquipmentRepository {
        return AstroEquipmentRepositoryImpl(db.equipDao)
    }

    @Provides
    @Singleton
    fun providesLightPollutionUseCase(@ApplicationContext context: Context): CalculateLightPollUseCase {
        return CalculateLightPollUseCase(context)
    }

    //room database
    @Provides
    @Singleton
    fun provideAstroBuddyDatabase(app: Application): AstroBuddyDatabase{
        Log.i("create", "AstroBuddyDB created")
        return Room.databaseBuilder(
            app, AstroBuddyDatabase::class.java, "astrobuddy_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesAstroObjectDatabase(app: Application): AstroObjectDatabase{
        Log.i("create", "AstroObjectDB created")
        return Room.databaseBuilder(
            app, AstroObjectDatabase::class.java, "astroobject_db"
        ).createFromAsset("database/Astronomical_Database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesApplicationContext(application: Application): Context {
        return application.applicationContext
    }

}