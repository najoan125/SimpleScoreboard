package kr.hyfata.simplescoreboard.listener

import kr.hyfata.simplescoreboard.SimpleScoreboard
import kr.hyfata.simplescoreboard.config.ConfigManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ScoreboardListener(private val simpleScoreboard: SimpleScoreboard) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        ConfigManager.scoreboardConfig.getBoolean("scoreboard.enabled", true).let {
            if (!it) {
                return
            }
        }
        val player = event.getPlayer()
        simpleScoreboard.createScoreboard(player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.getPlayer()
        simpleScoreboard.removeScoreboard(player)
    }
}