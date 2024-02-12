package com.pilot.astrobuddy.presentation.equipment_setup

import com.pilot.astrobuddy.domain.model.astro_equipment.AstroEquipment

data class EquipmentSetupState (
    val isLoading: Boolean = false,
    val astroEquipment: AstroEquipment? = null,
    val error: String = "",
)