package com.pilot.astrobuddy.domain.use_case.get_locations

import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.openmeteo.OMLocation
import com.pilot.astrobuddy.domain.repository.ForecastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(
    private val repository: ForecastRepository
) {
    /*
    Overridden invocation function to return a flow of resources containing lists of location objects
     */
    operator fun invoke(query: String): Flow<Resource<List<OMLocation>>> = flow{
        try{
            //initially emit loading
            emit(Resource.Loading())
            //try retrieve locations from api and map to proper objects
            val locations = repository.getOMLocations(query).results
            val outLocations = locations.map{ r->r.toLocation()}
            //emit data if successful
            emit(Resource.Success(outLocations))
        }catch(e: HttpException){
            //emit applicable errors
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }catch(e: IOException){
            emit(Resource.Error("Could not reach the server, check your internet connection"))
        }
    }
}