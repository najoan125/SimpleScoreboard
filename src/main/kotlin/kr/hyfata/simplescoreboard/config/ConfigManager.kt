package kr.hyfata.simplescoreboard.config

import org.bukkit.plugin.java.JavaPlugin

object ConfigManager {
    var scoreboardConfig: AbstractConfig = ScoreboardConfig()

    fun init(plugin: JavaPlugin) {
        scoreboardConfig.init(plugin)
    }

    fun save() {
        scoreboardConfig.saveConfig()
    }
}