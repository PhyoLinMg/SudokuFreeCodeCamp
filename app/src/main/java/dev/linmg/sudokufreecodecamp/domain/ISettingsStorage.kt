package dev.linmg.sudokufreecodecamp.domain

import java.lang.Exception

interface ISettingsStorage {
    suspend fun getSettings():Settings
    suspend fun updateSettings(settings: Settings):Settings

}
sealed class SettingsStorageResult{
    data class OnSuccess(val settings: Settings): GamesStorageResult()
    data class OnError(val exception: Exception): GamesStorageResult()

}