package com.sweeftdigital.contactsexchange.presentation.qr

import com.sweeftdigital.contactsexchange.domain.models.Contact
import com.sweeftdigital.contactsexchange.presentation.base.markers.EffectMarker
import com.sweeftdigital.contactsexchange.presentation.base.markers.EventMarker
import com.sweeftdigital.contactsexchange.presentation.base.markers.StateMarker

sealed class ViewState {
    object Idle: ViewState()
    object Loading: ViewState()
    object Error : ViewState()
    object Success : ViewState()
}

sealed class QrEvent : EventMarker {
    data class OnQrScan(val contact: Contact) : QrEvent()
}

sealed class QrEffect : EffectMarker {
    data class Error(val message: String) : QrEffect()
}

data class QrState(
    val viewState: ViewState,
    val contact: Contact = Contact()
) : StateMarker