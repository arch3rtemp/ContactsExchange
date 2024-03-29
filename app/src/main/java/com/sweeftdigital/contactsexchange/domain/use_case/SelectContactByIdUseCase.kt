package com.sweeftdigital.contactsexchange.domain.use_case

import com.sweeftdigital.contactsexchange.domain.model.Contact
import com.sweeftdigital.contactsexchange.domain.repository.Repository
import com.sweeftdigital.contactsexchange.domain.use_case.base.BaseUseCase
import kotlinx.coroutines.flow.Flow

class SelectContactByIdUseCase(private val repo: Repository) : BaseUseCase<Int, Flow<Contact>> {
    override suspend fun start(arg: Int?): Flow<Contact> {
        return repo.selectContactById(arg!!)
    }
}