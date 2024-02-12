package com.pilot.astrobuddy.presentation.equipment_screen

import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment

data class EquipmentScreenState (
    val isLoading: Boolean = false,
    val objects: List<AstroEquipment> = emptyList(),
    val error: String = "",
    val editing: Boolean = false
)