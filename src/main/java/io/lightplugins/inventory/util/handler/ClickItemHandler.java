package io.lightplugins.inventory.util.handler;

import io.lightplugins.inventory.LightMaster;
import io.lightplugins.inventory.util.NumberFormatter;
import io.lightplugins.inventory.util.SkullUtil;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ClickItemHandler {

    private final ConfigurationSection GUI_ITEM_ARGS;
    private final ConfigurationSection PLACEHOLDERS;
    private final Player player;
    private String item;
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private final Map<String, String> placeholders = new HashMap<>();
    @Getter
    private final ConfigurationSection actions;
    @Getter
    private int modelData;
    @Getter
    private boolean playerHead = false;
    @Getter
    private String headData;
    @Getter
    private HashMap<RequirementHandler, List<ActionHandler>> requirementHandler;
    @Getter
    private List<ActionHandler> successActions;

    public ClickItemHandler(ConfigurationSection section, Player player) {

        this.GUI_ITEM_ARGS = section.getConfigurationSection("args");
        this.actions = section.getConfigurationSection("args.click-actions");
        this.PLACEHOLDERS = section.getConfigurationSection("args.placeholders");
        this.player = player;

        applyPlaceholders();
        guiItemContent();
        translatePlaceholders();
        translateLore();
        loadRequirements();

    }

    private void loadRequirements() {

        if(actions == null) {
            LightMaster.getDebugPrinting().print("No click actions configuration section found");
            return;
        }

        for(String key : actions.getKeys(true)) {
            if(key.contains("requirement")) {

                List<ActionHandler> failActions = new ArrayList<>();

                for(String failAction : actions.getStringList(key + ".fail-actions")) {
                    failActions.add(new ActionHandler(player, failAction));
                }

                String requirementType = actions.getString(key + ".type");
                String requirementData = actions.getString(key + ".data");

                RequirementHandler requirementHandler = new RequirementHandler(
                        requirementType, requirementData, failActions);

                this.requirementHandler.put(requirementHandler, failActions);
            }
        }
    }

    private void applyPlaceholders() {
        for(String placeholder : PLACEHOLDERS.getKeys(true)) {
            placeholders.put(
                    placeholder,
                    PlaceholderAPI.setPlaceholders(
                            player,
                            Objects.requireNonNull(PLACEHOLDERS.getString(placeholder))
                    ));
        }
    }

    private void translatePlaceholders() {
        for(String key : placeholders.keySet()) {
            this.displayName = displayName.replace("#" + key + "#", placeholders.get(key));
            this.headData = headData.replace("#" + key + "#", placeholders.get(key));
        }
    }

    private void translateLore() {
        List<String> translatedLore = new ArrayList<>();
        for(String line : lore) {
            for(String key : placeholders.keySet()) {
                line = line.replace("#" + key + "#", placeholders.get(key));
            }
            translatedLore.add(LightMaster.instance.colorTranslation.loreLineTranslation(line, player));
        }
        this.lore = translatedLore;
    }

    private void guiItemContent() {

        this.item = GUI_ITEM_ARGS.getString("item");
        this.displayName = GUI_ITEM_ARGS.getString("display-name");
        this.lore = GUI_ITEM_ARGS.getStringList("lore");
        this.headData = GUI_ITEM_ARGS.getString("head-data");

    }

    public ItemStack getGuiItem() {

        ItemStack itemStack = new ItemStack(Material.STONE, 1);

        String[] splitItem = item.split(" ");
        Material material = Material.getMaterial(splitItem[0].toUpperCase());

        if(NumberFormatter.isNumber(splitItem[1])) {
            itemStack.setAmount(Integer.parseInt(splitItem[1]));
        }

        if(material != null) {
            itemStack.setType(material);
        } else {
            return new ItemStack(Material.STONE, 1);
        }

        if(material.equals(Material.PLAYER_HEAD)) {
            itemStack = SkullUtil.getPlayerSkull(player);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        if(itemMeta == null) {
            return null;
        }

        itemMeta.setDisplayName(LightMaster.instance.colorTranslation.loreLineTranslation(displayName, player));

        for(String split : splitItem) {

            if(split.equalsIgnoreCase("model-data")) {
                String[] splitModelData = split.split(":");
                if(NumberFormatter.isNumber(splitModelData[1])) {
                    itemMeta.setCustomModelData(Integer.parseInt(splitModelData[1]));
                    this.modelData = Integer.parseInt(splitModelData[1]);
                }
            }

            if(split.equalsIgnoreCase("hide_enchants")) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            if(split.equalsIgnoreCase("hide_attributes")) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }

            if(split.equalsIgnoreCase("hide_unbreakable")) {
                itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            }

            if(split.equalsIgnoreCase("hide_destroys")) {
                itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            }

            if(split.equalsIgnoreCase("hide_dye")) {
                itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
            }

            if(split.equalsIgnoreCase("hide_placed_on")) {
                itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            }
        }

        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private void readActions() {
        for(String key : actions.getKeys(true)) {

        }
    }
}
