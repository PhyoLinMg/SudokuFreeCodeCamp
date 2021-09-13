package dev.linmg.sudokufreecodecamp.ui.activegame

import dev.linmg.sudokufreecodecamp.common.BaseLogic
import dev.linmg.sudokufreecodecamp.common.DispatcherProvider
import dev.linmg.sudokufreecodecamp.domain.IGameRepository
import dev.linmg.sudokufreecodecamp.domain.IStatisticsRepository
import dev.linmg.sudokufreecodecamp.domain.SudokuPuzzle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ActiveGameLogic(
    private val container: ActiveGameContainer?,
    private val viewModel: ActiveGameViewModel,
    private val gameRepo: IGameRepository,
    private val statsRepo: IStatisticsRepository,
    private val dispatcher: DispatcherProvider
) : BaseLogic<ActiveGameEvent>(), CoroutineScope {
    init {
        jobTracker = Job()
    }

    inline fun startCoroutineTimer(
        crossinline action: () -> Unit
    ) = launch {
        while (true) {
            action()
            delay(1000)
        }
    }

    private var timerTracker: Job? = null


    private val Long.timeOffSet: Long
        get() {
            return if (this <= 0) 0 else this - 1
        }

    override val coroutineContext: CoroutineContext
        get() = dispatcher.provideUIContext() + jobTracker

    override fun onEvent(event: ActiveGameEvent) {
        when (event) {
            is ActiveGameEvent.OnInput -> onInput(event.input, viewModel.subTimerState)
            ActiveGameEvent.OnNewGameClicked -> onNewGameClicked()
            ActiveGameEvent.OnStart -> TODO()
            ActiveGameEvent.OnStop -> TODO()
            is ActiveGameEvent.OnTileFocused -> TODO()
        }
    }

    private fun onNewGameClicked() = launch {
        viewModel.showLoadingState()

        if (!viewModel.isCompleteState) {
            gameRepo.getCurrentGame(
                //onSuccess
                { puzzle, _ -> updateWithTime(puzzle) },
                //onError
                { container?.showError() }
            )
        } else {
            navigateToNewGame()
        }

    }

    private fun updateWithTime(puzzle: SudokuPuzzle) =launch{
        gameRepo.updateGame(
            puzzle.copy(elapsedTime = viewModel.timerState.timeOffSet),
            //onSuccess,
            {navigateToNewGame()},
            //onError
            {
                container?.showError()
                navigateToNewGame()
            },
        )

    }

    private fun navigateToNewGame() {
        cancelStuff()
        container?.onNewGameClick()
    }

    private fun cancelStuff(){
        if(timerTracker?.isCancelled==false) timerTracker?.cancel()
        jobTracker.cancel()
    }

    private fun onInput(input: Int, elapsedTime: Long) = launch {
        var focusedTile: SudokuTile? = null
        viewModel.boardState.values.forEach {
            if (it.hasFocus) focusedTile = it
        }
        if (focusedTile != null) {
            gameRepo.updateNode(focusedTile!!.x,
                focusedTile!!.y,
                input,
                elapsedTime,
                //success,
                { isComplete ->
                    focusedTile?.let {
                        viewModel.updateBoardState(
                            it.x,
                            it.y,
                            input,
                            false
                        )
                    }
                    if (isComplete) {
                        timerTracker?.cancel()
                        checkIfNewRecord()
                    }
                },
                //error,
                { container?.showError() }
            )
        }
    }

    private fun checkIfNewRecord() = launch {
        statsRepo.updateStatistics(
            viewModel.timerState,
            viewModel.difficulty,
            viewModel.boundary,
            //success
            { isRecord ->
                viewModel.isNewRecordState = isRecord
                viewModel.updateCompleteState()
            },
            //error
            {
                container?.showError()
                viewModel.updateCompleteState()
            },
        )
    }
}