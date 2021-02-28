package de.hglabor.plugins.uhc.config

import de.hglabor.plugins.uhc.game.GameManager
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.kSpigotGUI

object ConfigInventory {
    init {
        val gui = kSpigotGUI(GUIType.FIVE_BY_NINE) {
            title = "UHC Settings"
            GameManager.INSTANCE.scenarios.forEach {
                page()
            }

        }
    }
}
