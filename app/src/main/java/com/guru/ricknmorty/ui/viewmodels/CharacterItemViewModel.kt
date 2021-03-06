package com.guru.ricknmorty.ui.viewmodels

import com.guru.ricknmorty.models.Character


class CharacterItemViewModel (character: Character) {

    private val character = character

    fun getAvatarUrl(): String? = character.image

    fun getName(): String? = character.name

    fun getStatus(): String? = character.status

    fun getCharacter() = character

    fun getImage() = character.image

    fun getType() = character.type

    fun getSpecies() = character.species
}