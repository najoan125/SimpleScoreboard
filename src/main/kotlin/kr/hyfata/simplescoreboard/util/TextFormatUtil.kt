package kr.hyfata.simplescoreboard.util

import kr.hyfata.simplescoreboard.config.ConfigManager
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player

object TextFormatUtil {
    fun getFormattedText(player: Player, text: String): String {
        val placeholders = mapOf(
            "%world%" to { getFormattedWorld(player.world.name) ?: "" },
        )

        var result = text
        for ((placeholder, valueProvider) in placeholders) {
            result = result.replace(placeholder, valueProvider())
        }
        result = PlaceholderAPI.setPlaceholders(player, result)
        result = getFormattedText(result)
        return result
    }

    fun getFormattedText(text: String): String {
        return text.replace("&", "§")
            .replace("\\\n", "")
    }

    fun getFormattedWorld(world: String): String? {
        return ConfigManager.scoreboardConfig.getString("worlds.$world", world)
    }
}