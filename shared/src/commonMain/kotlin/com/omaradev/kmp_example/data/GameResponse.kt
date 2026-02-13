package com.omaradev.kmp_example.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val id: Long,
    val title: String,
    val thumbnail: String,
    @SerialName("short_description")
    val shortDescription: String,
    val publisher: String,
    val developer: String,
)