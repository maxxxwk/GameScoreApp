package com.maxxxwk.gamescoreapp.utils

import com.maxxxwk.gamescoreapp.models.Team

class GameResultChecker {

    var isDraw: Boolean = false
    var winner: Team? = null
    var loser: Team? = null

    constructor(firstTeam: Team, secondTeam: Team) {
        when {
            firstTeam.score == secondTeam.score -> {
                isDraw = true
            }
            firstTeam.score > secondTeam.score -> {
                winner = firstTeam
                loser = secondTeam
            }
            else -> {
                winner = secondTeam
                loser = firstTeam
            }
        }
    }
}