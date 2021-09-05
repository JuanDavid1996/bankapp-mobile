package com.example.bankapp.repository.common.models

open class AppResponse<T>(
    open var success: Boolean?,
    open var data: T?,
    open var errors: List<Any>
)