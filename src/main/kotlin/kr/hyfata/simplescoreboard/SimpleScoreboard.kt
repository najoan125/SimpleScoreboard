package kr.hyfata.simplescoreboard

import fr.mrmicky.fastboard.FastBoard
import kr.hyfata.simplescoreboard.config.ConfigManager
import kr.hyfata.simplescoreboard.util.TextFormatUtil
import kr.hyfata.simplescoreboard.listener.ScoreboardCommand
import kr.hyfata.simplescoreboard.listener.ScoreboardListener
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class SimpleScoreboard : JavaPlugin() {
    private val boards: MutableMap<UUID?, FastBoard> = HashMap<UUID?, FastBoard>()

    override fun onEnable() {
        placeholderApiEnabled = Bukkit.getServer().pluginManager.isPluginEnabled("PlaceholderAPI")

        ConfigManager.init(this)
        setListeners()
        createScoreboardAllPlayers()

        // update board per 20 ticks
        Bukkit.getServer().scheduler.runTaskTimer(this, Runnable {
            for (board in this.boards.values) {
                updateBoard(board)
            }
        }, 0, 20)
        this.logger.info("Simple Scoreboard has been enabled.")
    }

    override fun onDisable() {
        ConfigManager.save()
        removeScoreboardAllPlayers()
        this.logger.info("Simple Scoreboard has been disabled.")
    }

    private fun setListeners() {
        val command = ScoreboardCommand(this)
        this.getCommand("simplescoreboard")?.setExecutor(command)
        this.getCommand("simplescoreboard")?.tabCompleter = command
        Bukkit.getServer().pluginManager.registerEvents(ScoreboardListener(this), this)
    }

    fun createScoreboardAllPlayers() {
        for (player in Bukkit.getServer().onlinePlayers) {
            createScoreboard(player)
        }
    }

    fun createScoreboard(player: Player) {
        val board = FastBoard(player)
        board.updateTitle(
            TextFormatUtil.getFormattedText(
                player,
                ConfigManager.scoreboardConfig.getString("scoreboard.title", "&cERROR")!!
            )
        )

        this.boards[player.uniqueId] = board
    }

    fun removeScoreboardAllPlayers() {
        for (player in Bukkit.getServer().onlinePlayers) {
            removeScoreboard(player)
        }
    }

    fun removeScoreboard(player: Player) {
        val board = this.boards.remove(player.uniqueId)
        board?.delete()
    }

    private fun updateBoard(board: FastBoard) {
        board.updateLines(
            *TextFormatUtil.getFormattedText(
                board.player,
                ConfigManager.scoreboardConfig.getString("scoreboard.message", "&cERROR")!!
            )
                .replace("\\\n", "")
                .split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        )
    }

    companion object {
        var placeholderApiEnabled: Boolean = false
    }
}
