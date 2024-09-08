package com.pilot.astrobuddy.domain.use_case.get_equipment

import com.pilot.astrobuddy.common.Resource
import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment
import com.pilot.astrobuddy.domain.repository.AstroEquipmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

/**
 * Use-case to interact with the Astro Equipment database.
 */
class GetAstroEquipmentUseCase @Inject constructor(
    private val repository: AstroEquipmentRepository
){
    /**
     * Fetch all astro equipment
     *
     * @return Flow of Resource containing List of all AstroEquipment records.
     */
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

    /**
     * Fetch a particular AstroEquipment record by ID
     *
     * @param id id of the record
     * @return Flow of resource of one-element list of AstroEquipment
     */
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

    /**
     * Save an AstroEquipment object to the database
     *
     * @param equip AstroEquipment to save
     */
    suspend fun saveAstroEquipment(equip: AstroEquipment){
        repository.saveAstroEquipment(equip)
    }

    /**
     * Remove an AstroEquipment object from the database
     *
     * @param id id of the equipment record to remove
     */
    suspend fun unsaveAstroEquipment(id: Int){
        repository.unsaveAstroEquipment(id)
    }
}