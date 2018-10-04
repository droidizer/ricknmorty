package com.guru.ricknmorty.models

import com.google.gson.annotations.SerializedName

data class CharactersResponseModel(

        @field:SerializedName("info")
        val info: Info,

        @field:SerializedName("results")
        val items: List<Character> = ArrayList()
)
