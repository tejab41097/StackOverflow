package com.tejas.stackoverflow.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Question(
    var accepted_answer_id: Int = 0,
    var answer_count: Int = 0,
    var closed_date: Int = 0,
    var closed_reason: String = "",
    var content_license: String = "",
    var creation_date: String = "",
    @SerializedName("is_answered")
    var answered: Boolean = false,
    var last_activity_date: Int = 0,
    var last_edit_date: Int = 0,
    var link: String = "",
    @PrimaryKey
    var question_id: Int = 0,
    var score: Int = 0,
    var tags: MutableList<String?> = mutableListOf(),
    var title: String = "",
    var view_count: Int = 0,
    var owner_id: Int? = 0,
    @Ignore
    var owner: Owner? = null
) {
    constructor() : this(
        0,
        0,
        0,
        "",
        "",
        "",
        false,
        0,
        0,
        "",
        0,
        0,
        mutableListOf(),
        "",
        0,
        0,
        null
    )
}