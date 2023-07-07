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
    operator fun invoke(query: String): Flow<Resource<List<OMLocation>>> = flow{
        try{
            emit(Resource.Loading())
            val locations = repository.getOMLocations(query).results
            val outLocations = if(locations != null)
            {locations.map{ r->r.toLocation()}}
            else{ emptyList()}
            emit(Resource.Success(outLocations))
        }catch(e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }catch(e: IOException){
            emit(Resource.Error("Could not reach the server, check your internet connection"))
        }
    }
}