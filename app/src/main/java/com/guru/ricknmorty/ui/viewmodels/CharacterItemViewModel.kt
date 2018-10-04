package com.guru.ricknmorty.ui.viewmodels

import com.guru.ricknmorty.models.Character


class CharacterItemViewModel constructor(character: Character) {

    private val character = character

    fun getAvatarUrl(): String? = character.image

    fun getName(): String? = character.name

    fun getStatus(): String? = character.status

    fun getCharacter() = character

    fun getImage() = character.image
}