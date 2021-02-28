package de.hglabor.plugins.uhc.config

import de.hglabor.plugins.uhc.game.GameManager
import de.hglabor.plugins.uhc.game.Scenario
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.items.flag
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag

object ConfigInventory {

    private var menuCompound: GUIRectSpaceCompound<ForInventoryFiveByNine, GUICompoundElement<ForInventoryFiveByNine>>? =
        null

    val gui = kSpigotGUI(GUIType.FIVE_BY_NINE, SharedGUICreator()) {
        title = "UHC Settings"
        page(1) {
            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
            menuCompound = createSimpleRectCompound(Slots.RowTwoSlotTwo, Slots.RowFiveSlotEight)
        }
    }

    class ScenarioCompoundElement(scenario: Scenario) : GUICompoundElement<ForInventoryFiveByNine>(
        itemStack(scenario.displayItem.type) {
            meta {
                this.name = "${KColors.DEEPSKYBLUE}${scenario.name}"
                flag(ItemFlag.HIDE_ENCHANTS)
                flag(ItemFlag.HIDE_ATTRIBUTES)
            }
        },
        onClick = {
            val clickedItem = it.bukkitEvent.currentItem
            clickedItem?.meta {
                if (scenario.isEnabled) {
                    removeEnchant(Enchantment.LUCK)
                    scenario.isEnabled = false
                } else {
                    addEnchant(Enchantment.LUCK, 1, true)
                    scenario.isEnabled = true
                }
            }
        })

    fun addScenariosToCompound() {
        GameManager.INSTANCE.scenarios.forEach {
            menuCompound?.addContent(ScenarioCompoundElement(it))
        }
    }
}
