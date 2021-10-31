package com.tejas.stackoverflow.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Owner(
    val accept_rate: Int,
    val display_name: String,
    val link: String,
    val profile_image: String,
    val reputation: Int,
    @PrimaryKey
    val user_id: Int,
    val user_type: String
)