package dev.linmg.sudokufreecodecamp.domain

import java.lang.Exception

interface ISettingsStorage {
    suspend fun getSettings():SettingsStorageResult
    suspend fun updateSettings(settings: Settings):SettingsStorageResult

}
sealed class SettingsStorageResult{
    data class OnSuccess(val settings: Settings): SettingsStorageResult()
    object OnComplete : SettingsStorageResult()
    data class OnError(val exception: Exception): SettingsStorageResult()

}