package com.metrostateics499.vre_app.model.data

class CallMessage(var title: String, var callMessage: String) {
    override fun toString(): String {
        return callMessage
    }
}