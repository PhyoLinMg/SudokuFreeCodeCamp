package dev.linmg.sudokufreecodecamp.ui.activegame

import dev.linmg.sudokufreecodecamp.common.DispatcherProvider
import dev.linmg.sudokufreecodecamp.domain.IGameRepository
import dev.linmg.sudokufreecodecamp.domain.IStatisticsRepository

class ActiveGameLogic(
    private val container:ActiveGameContainer?,
    private val viewModel:ActiveGameViewModel,
    private val gameRepo: IGameRepository,
    private val statsRepo:IStatisticsRepository,
    private val dispatcher: DispatcherProvider
) {
}