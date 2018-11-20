package com.guru.ricknmorty.viewmodels

import com.google.gson.Gson
import com.guru.ricknmorty.JsonFileReader
import com.guru.ricknmorty.models.Character
import com.guru.ricknmorty.ui.viewmodels.CharacterItemViewModel
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterItemViewModelTest {

    @Test
    fun `test_character`() {
        val character = getCharacter()
        val characterItemViewModel = CharacterItemViewModel(character)
        Assert.assertEquals(character, characterItemViewModel.getCharacter())
    }

    @Test
    fun `test_character_name`() {
        val character = getCharacter()
        val characterItemViewModel = CharacterItemViewModel(character)
        Assert.assertEquals(character.name, characterItemViewModel.getName())
    }

    @Test
    fun `test_character_status`() {
        val character = getCharacter()
        val characterItemViewModel = CharacterItemViewModel(character)
        Assert.assertEquals(character.status, characterItemViewModel.getStatus())
    }

    @Test
    fun `test_character_avatar_url`() {
        val character = getCharacter()
        val characterItemViewModel = CharacterItemViewModel(character)
        Assert.assertEquals(character.image, characterItemViewModel.getAvatarUrl())
    }

    @Test
    fun `test_character_species`() {
        val character = getCharacter()
        val characterItemViewModel = CharacterItemViewModel(character)
        Assert.assertEquals(character.species, characterItemViewModel.getSpecies())
    }

    private fun getCharacter(): Character {
        return JsonFileReader.read(javaClass.classLoader.getResourceAsStream("character.json"),
                Gson(), Character::class.java).blockingFirst()
    }
}