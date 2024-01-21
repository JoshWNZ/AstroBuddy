package com.pilot.astrobuddy.domain.use_case.get_objects

import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.astro_objects.AstroObject
import com.pilot.astrobuddy.domain.repository.AstroObjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetAstroObjectUseCase @Inject constructor(
    private val repository: AstroObjectRepository
) {
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