package com.pilot.astrobuddy.common

/**
 * Class to handle resource loading within coroutines/flows
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null){
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}