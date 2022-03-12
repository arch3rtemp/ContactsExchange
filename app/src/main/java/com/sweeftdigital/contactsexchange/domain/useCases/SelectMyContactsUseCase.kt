package com.sweeftdigital.contactsexchange.domain.useCases

import androidx.lifecycle.LiveData
import com.sweeftdigital.contactsexchange.domain.models.Contact
import com.sweeftdigital.contactsexchange.domain.repository.Repository
import com.sweeftdigital.contactsexchange.domain.useCases.base.BaseUseCase
import kotlinx.coroutines.flow.Flow

class SelectMyContactsUseCase(private val repo: Repository) : BaseUseCase<Unit, Flow<List<Contact>>> {
    override suspend fun start(arg: Unit?): Flow<List<Contact>> {
        return repo.selectAllMyContacts()
    }
}