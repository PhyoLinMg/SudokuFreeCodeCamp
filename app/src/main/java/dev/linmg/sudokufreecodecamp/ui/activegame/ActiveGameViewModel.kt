package dev.linmg.sudokufreecodecamp.ui.activegame

import dev.linmg.sudokufreecodecamp.domain.Difficulty
import dev.linmg.sudokufreecodecamp.domain.SudokuPuzzle
import dev.linmg.sudokufreecodecamp.domain.getHash

class ActiveGameViewModel {

    internal var subBoardState: ((HashMap<Int, SudokuTile>) -> Unit)? = null
    internal var subContentState: ((ActiveGameScreenState) -> Unit)? = null
    internal var subTimerState: ((Long) -> Unit)? = null


    internal fun updateTimerState() {
        timerState++
        subContentState?.invoke(timerState)
    }

    internal var subIsCompleteState: ((Boolean) -> Unit)? = null
    private var timerState: Long = 0L
    internal var difficulty = Difficulty.MEDIUM
    internal var boundary = 9
    internal var boardState: HashMap<Int, SudokuTile> = HashMap()
    internal var isCompleteState: Boolean = false
    internal var isNewRecordState: Boolean = false

    fun initializeBoardState(
        puzzle: SudokuPuzzle,
        isComplete: Boolean
    ) {
        puzzle.graph.forEach {
            val node = it.value[0]
            boardState[it.key] =
                SudokuTile(node.x, node.y, node.color, hasFocus = false, node.readOnly)
        }
        val contentState: ActiveGameScreenState
        if (isComplete) {
            isCompleteState = true

            contentState = ActiveGameContentState.COMPLETE
        } else {
            contentState=ActiveGameContentState.ACTIVE
        }
        boundary=puzzle.boundary
        difficulty=puzzle.difficulty
        timerState=puzzle.elapsedTime

        subIsCompleteState?.invoke(isCompleteState)
        subContentState?.invoke(contentState)
        subBoardState?.invoke(boardState)

    }
    internal fun updateBoardState(
        x:Int,y:Int,value:Int,hasFocus:Boolean
    ){
        boardState[getHash(x,y)]?.let {
            it.value=value
            it.hasFocus=hasFocus
        }
        subBoardState?.invoke(boardState)
    }

    internal fun showLoadingState(){
        subContentState?.invoke(ActiveGameScreenState.LOADING)
    }
    internal fun updateFocusState(x:Int,y:Int){
        boardState.values.forEach {
            it.hasFocus = it.x==x && it.y ==y
        }
        subBoardState?.invoke(boardState)
    }
    fun updateCompleteState(){
        isCompleteState=true
        subContentState?.invoke(ActiveGameScreenState.COMPLETE)
    }

}

class SudokuTile(
    val x: Int,
    val y: Int,
    var value: Int,
    var hasFocus: Boolean,
    val readOnly: Boolean
)