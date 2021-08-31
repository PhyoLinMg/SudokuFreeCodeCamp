package dev.linmg.sudokufreecodecamp.persistence

import dev.linmg.sudokufreecodecamp.GameSettings
import dev.linmg.sudokufreecodecamp.domain.GamesStorageResult
import dev.linmg.sudokufreecodecamp.domain.IGameDataStorage
import dev.linmg.sudokufreecodecamp.domain.SudokuPuzzle
import dev.linmg.sudokufreecodecamp.domain.getHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.Exception

private const val FILE_NAME = "game_state.txt"

class LocalGameStorageImpl(
    fileStorageDirectory: String,
    private val pathToStorageFile: File = File(fileStorageDirectory, FILE_NAME)
) : IGameDataStorage {
    override suspend fun updateGame(game: SudokuPuzzle): GamesStorageResult =
        withContext(Dispatchers.IO) {
            try {
                updateGameData(game)
                GamesStorageResult.OnSuccess(game)
            } catch (e: Exception) {
                GamesStorageResult.OnError(e)
            }
        }

    override suspend fun updateNode(x: Int, y: Int, color:Int,elapsedTime: Long): GamesStorageResult =
        withContext(Dispatchers.IO) {
            try {
                val game=getGame()
                game.graph[getHash(x,y)]!!.first.color=color
                game.elapsedTime=elapsedTime
                updateGameData(game)
                GamesStorageResult.OnSuccess(game)
            }catch (e:Exception){
                GamesStorageResult.OnError(e)
            }
        }

    override suspend fun getCurrentGame(): GamesStorageResult = withContext(Dispatchers.IO){
        try{
            GamesStorageResult.OnSuccess(getGame())
        }catch(e:Exception){
            GamesStorageResult.OnError(e)
        }
    }
    private fun getGame():SudokuPuzzle{
        try{
            var game:SudokuPuzzle
            val fileInputStream=FileInputStream(pathToStorageFile)
            val objectInputStream=ObjectInputStream(fileInputStream)
            game=objectInputStream.readObject() as SudokuPuzzle
            objectInputStream.close()
            return game
        }catch (e:Exception){
            throw e
        }
    }

    private fun updateGameData(game: SudokuPuzzle) {
        try {
            val fileOutputStream = FileOutputStream(pathToStorageFile)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(game)
            objectOutputStream.close()
        } catch (e: Exception) {
            throw e
        }
    }
}