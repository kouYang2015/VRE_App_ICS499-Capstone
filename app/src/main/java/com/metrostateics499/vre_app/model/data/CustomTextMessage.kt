package com.metrostateics499.vre_app.model.data

data class CustomTextMessage(var title: String, var textMessage: String) {
    override fun toString(): String {
        return textMessage
    }
}