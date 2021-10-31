package com.tejas.stackoverflow.model

class ApiResponse constructor() {
    var successBody: Any? = null
    var errorBody: ErrorBody? = null

    constructor(successBody: Any) : this() {
        this.successBody = successBody
        this.errorBody = null
    }

    constructor(errorBody: ErrorBody) : this() {
        this.errorBody = errorBody
        this.successBody = null
    }
}