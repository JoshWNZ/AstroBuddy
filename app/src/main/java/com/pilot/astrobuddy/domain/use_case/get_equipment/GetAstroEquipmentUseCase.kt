package com.pilot.astrobuddy.domain.use_case.get_equipment

import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment
import com.pilot.astrobuddy.domain.repository.AstroEquipmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetAstroEquipmentUseCase @Inject constructor(
    private val repository: AstroEquipmentRepository
){
    suspend fun getAllAstroEquipment(): Flow<Resource<List<AstroEquipment>>> = flow{
        try{
            emit(Resource.Loading())

            val equips = repository.getAllAstroEquipment()
            if(equips.isEmpty()){
                emit(Resource.Success(emptyList()))
            }else{
                emit(Resource.Success(equips))
            }
        }catch(e: IOException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    suspend fun getAstroEquipment(id: Int): Flow<Resource<List<AstroEquipment>>> = flow{
        try{
            emit(Resource.Loading())

            val equips = listOf(repository.getAstroEquipment(id))
            if(equips.isEmpty()){
                emit(Resource.Success(emptyList()))
            }else{
                emit(Resource.Success(equips))
            }
        }catch(e: IOException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    suspend fun saveAstroEquipment(equip: AstroEquipment){
        repository.saveAstroEquipment(equip)
    }

    suspend fun unsaveAstroEquipment(id: Int){
        repository.unsaveAstroEquipment(id)
    }
}