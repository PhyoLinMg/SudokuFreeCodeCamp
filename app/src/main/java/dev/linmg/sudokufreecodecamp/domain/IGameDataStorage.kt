package dev.linmg.sudokufreecodecamp.domain

import java.lang.Exception

interface IGameDataStorage {
    suspend fun updateGame(game:SudokuPuzzle):GamesStorageResult
    suspend fun updateNode(x:Int,y:Int,elapsedTime:Long):GamesStorageResult
    suspend fun getCurrentGame():GamesStorageResult
}

sealed class GamesStorageResult{
    data class OnSuccess(val currentGame:SudokuPuzzle): GamesStorageResult()
    data class OnError(val exception: Exception): GamesStorageResult()

}