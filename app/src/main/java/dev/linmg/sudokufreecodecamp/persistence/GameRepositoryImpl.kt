package dev.linmg.sudokufreecodecamp.persistence

import dev.linmg.sudokufreecodecamp.GameSettings
import dev.linmg.sudokufreecodecamp.domain.*
import java.lang.Exception

class GameRepositoryImpl(
    private val gameStorage: IGameDataStorage,
    private val settingsStorage: ISettingsStorage
) : IGameRepository {
    override suspend fun saveGame(
        elapsedTime: Long,
        onSuccess: (Unit) -> Unit,
        onError: (Exception) -> Unit
    ) {
        when (val getCurrentGameResult = gameStorage.getCurrentGame()) {
            is GamesStorageResult.OnSuccess -> {
                gameStorage.updateGame(
                    getCurrentGameResult.currentGame.copy(
                        elapsedTime = elapsedTime
                    )
                )
                onSuccess(Unit)
            }
            is GamesStorageResult.OnError -> {
                onError(getCurrentGameResult.exception)
            }
        }

    }

    override suspend fun updateGame(
        game: SudokuPuzzle,
        onSuccess: (Unit) -> Unit,
        onError: (Exception) -> Unit
    ) {
        when (val updateGameResult: GamesStorageResult = gameStorage.updateGame(game)) {
            is GamesStorageResult.OnSuccess -> onSuccess(Unit)
            is GamesStorageResult.OnError -> onError(updateGameResult.exception)
        }
    }

    override suspend fun updateNode(
        x: Int,
        y: Int,
        color: Int,
        elapsedTime: Long,
        onSuccess: (isComplete: Boolean) -> Unit,
        onError: (Exception) -> Unit
    ) {
        when (val result = gameStorage.updateNode(x, y, color, elapsedTime)) {
            is GamesStorageResult.OnSuccess -> onSuccess(
                puzzleIsComplete(result.currentGame)
            )
            is GamesStorageResult.OnError -> onError(result.exception)
        }
    }

    override suspend fun getCurrentGame(
        onSuccess: (currentGame: SudokuPuzzle, isComplete: Boolean) -> Unit,
        onError: (Exception) -> Unit
    ) {
        when (val getCurrentGameResult = gameStorage.getCurrentGame()) {
            is GamesStorageResult.OnSuccess -> onSuccess(
                getCurrentGameResult.currentGame,
                puzzleIsComplete(getCurrentGameResult.currentGame)
            )
            is GamesStorageResult.OnError ->{
                when(val getSettingsResult=settingsStorage.getSettings()){

                    is SettingsStorageResult.OnSuccess -> {
                        when(val updateGameResult=createAndWriteNewGame(getSettingsResult.settings)){
                            is GamesStorageResult.OnSuccess->onSuccess(
                                updateGameResult.currentGame,
                                puzzleIsComplete(
                                    updateGameResult.currentGame,
                                )
                            )
                            is GamesStorageResult.OnError ->onError(updateGameResult.exception)
                        }
                    }
                    is SettingsStorageResult.OnError ->onError(getSettingsResult.exception)

                }
            }
        }
    }



    override suspend fun getSettings(onSuccess: (Settings) -> Unit, onError: (Exception) -> Unit) {
        when(val getSettingsResult=settingsStorage.getSettings()){
            is SettingsStorageResult.OnSuccess-> onSuccess(getSettingsResult.settings)
            is SettingsStorageResult.OnError->onError(getSettingsResult.exception)
        }
    }

    override suspend fun updateSettings(
        settings: Settings,
        onSuccess: (Unit) -> Unit,
        onError: (Exception) -> Unit
    ) {
        settingsStorage.updateSettings(settings)
        onSuccess(Unit)
    }

    override suspend fun createNewGame(
        settings: Settings,
        onSuccess: (Unit) -> Unit,
        onError: (Exception) -> Unit
    ) {
        when(val updateSettingsResult=settingsStorage.updateSettings(settings)){
            SettingsStorageResult.OnComplete->{
                when(val updateGameResult=createAndWriteNewGame(settings)){
                    is GamesStorageResult.OnSuccess ->onSuccess(Unit)
                    is GamesStorageResult.OnError->onError(updateGameResult.exception)
                }
            }
            is SettingsStorageResult.OnError ->{
                onError(updateSettingsResult.exception)
            }

        }
    }

    private suspend fun createAndWriteNewGame(settings:Settings):GamesStorageResult{
        return gameStorage.updateGame(
            SudokuPuzzle(
                settings.boundary,
                settings.difficulty
            )
        )
    }

}