package com.pilot.astrobuddy.domain.use_case.get_objects

import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject
import com.pilot.astrobuddy.domain.repository.AstroObjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

/**
 * Use-case for fetching astronomical objects
 */
class GetAstroObjectUseCase @Inject constructor(
    private val repository: AstroObjectRepository
) {
    /**
     * Fetch all astro objects
     *
     * @return Flow of resource of List of AstroObjects
     */
    suspend fun getAllAstroObjects(): Flow<Resource<List<AstroObject>>> = flow{
       try{
           emit(Resource.Loading())

           val objects = repository.getAllAstroObjects()
           if(objects.isNullOrEmpty()){
               emit(Resource.Success(emptyList()))
           }else{
               emit(Resource.Success(objects))
           }
       }catch(e: IOException){
           emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
       }
    }

    /**
     * Search AstroObjects with a given string
     *
     * @param query search query
     * @return Flow of resource of List of AstroObject search results
     */
    suspend fun searchAstroObjects(query: String): Flow<Resource<List<AstroObject>>> = flow{
        try{
            emit(Resource.Loading())

            val objects = repository.searchAstroObjects(query)
            if(objects.isNullOrEmpty()){
                emit(Resource.Success(emptyList()))
            }else{
                emit(Resource.Success(objects))
            }
        }catch(e: IOException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    /**
     * Fetch a particular AstroObject
     *
     * @param name name of the AstroObject to fetch
     * @return Flow of resource of single-element List of AstroObject
     */
    suspend fun getAstroObject(name: String): Flow<Resource<List<AstroObject>>> = flow{
        try{
            emit(Resource.Loading())

            val objects = repository.getAstroObject(name)
            if(objects.isNullOrEmpty()){
                emit(Resource.Success(emptyList()))
            }else{
                emit(Resource.Success(listOf(objects[0])))
            }
        }catch(e: IOException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}