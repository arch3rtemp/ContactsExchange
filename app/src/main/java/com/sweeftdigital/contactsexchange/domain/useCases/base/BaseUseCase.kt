package com.sweeftdigital.contactsexchange.domain.useCases.base

interface BaseUseCase<ARG_TYPE, RETURN_TYPE> {
    suspend fun start(arg: ARG_TYPE? = null): RETURN_TYPE
}