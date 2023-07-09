package com.pilot.astrobuddy.domain.use_case.get_forecast

import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.openmeteo.OMForecast
import com.pilot.astrobuddy.domain.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: ForecastRepository
) {
    /*
    Overridden invocation function to return a flow of resources containing weather forecasts
     */
    operator fun invoke(lat: String, long: String, days: Int): Flow<Resource<OMForecast>> = flow{
        try{
            //initially report loading
            emit(Resource.Loading())
            //try get forecast from api
            val forecast = repository.getOMForecast(lat,long,days).toForecast()
            //if successful, emit a success containing the data
            emit(Resource.Success(forecast))
        }catch(e: HttpException){
            //emit errors if applicable
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }catch(e: IOException){
            emit(Resource.Error("Could not reach the server, check your internet connection"))
        }
    }
}