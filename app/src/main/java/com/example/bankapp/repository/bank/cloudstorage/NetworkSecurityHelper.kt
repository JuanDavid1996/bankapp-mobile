package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.session.models.Session


class NetworkSecurityHelper {
    companion object {
        fun buildAuthHeaders(session: Session): Map<String, String> {
            val headerMap = mutableMapOf<String, String>()
            headerMap["Authorization"] = session.token
            return headerMap;
        }
    }
}