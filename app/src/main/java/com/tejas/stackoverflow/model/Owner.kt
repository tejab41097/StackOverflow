package com.tejas.stackoverflow.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Owner(
    val accept_rate: Int? = null,
    val display_name: String? = null,
    val link: String? = null,
    val profile_image: String? = null,
    val reputation: Int? = null,
    @PrimaryKey
    val user_id: Int,
    val user_type: String? = null
)