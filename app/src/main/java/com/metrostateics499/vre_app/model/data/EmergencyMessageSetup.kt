package com.metrostateics499.vre_app.model.data

import com.metrostateics499.vre_app.model.data.CustomTextMessage

data class EmergencyMessageSetup(
    var title: String,
    var keyPhrase: KeyPhrase,
    var customTextMessage: CustomTextMessage
)
