package kr.hyfata.simplescoreboard.util

import kr.hyfata.simplescoreboard.SimpleScoreboard
import kr.hyfata.simplescoreboard.config.ConfigManager
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player

object TextFormatUtil {
    fun getFormattedText(player: Player, text: String): String {
        val placeholders = mapOf(
            "%world%" to { getFormattedWorld(player.world.name) ?: "" },
        )

        var result = getFormattedText(text) // 기존 getFormattedText(text: String) 함수를 호출하여 기본 서식 적용
        for ((placeholder, valueProvider) in placeholders) {
            result = result.replace(placeholder, valueProvider())
        }
        return if (SimpleScoreboard.placeholderApiEnabled)
            PlaceholderAPI.setPlaceholders(player, result)
        else
            result
    }

    fun getFormattedText(text: String): String {
        return text.replace("&", "§")
            .replace("\\\n", "")
    }

    fun getFormattedWorld(world: String): String? {
        return ConfigManager.scoreboardConfig.getString("worlds.$world", world)
    }
}