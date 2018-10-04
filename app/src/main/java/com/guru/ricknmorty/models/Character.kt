package com.guru.ricknmorty.models

import com.google.gson.annotations.SerializedName

data class Character(

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("status")
        val status: String,

        @field:SerializedName("species")
        val species: String,

        @field:SerializedName("type")
        val type: String,

        @field:SerializedName("gender")
        val gender: String,

        @field:SerializedName("image")
        val image: String,

        @field:SerializedName("url")
        val url: String,

        @field:SerializedName("created")
        val created: String
)

