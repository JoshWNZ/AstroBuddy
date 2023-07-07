package com.pilot.astrobuddy.domain.use_case.get_forecast

import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.model.weatherapi.Astro
import com.pilot.astrobuddy.domain.model.weatherapi.ForecastResult
import com.pilot.astrobuddy.domain.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAstroUseCase @Inject constructor(
    private val repository: ForecastRepository
) {
    operator fun invoke(lat: String, long: String): Flow<Resource<List<Astro>>> = flow{
        try{
            emit(Resource.Loading())
            val forecast = repository.getWAForecast(lat+long).forecast.toAstroList()
            emit(Resource.Success(forecast))
        }catch(e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }catch(e: IOException){
            emit(Resource.Error("Could not reach the server, check your internet connection"))
        }
    }
}