package com.tejas.stackoverflow.model

data class QuestionListResponse(
    val has_more: Boolean,
    var items: MutableList<Question?>,
    val quota_max: Int,
    val quota_remaining: Int
)