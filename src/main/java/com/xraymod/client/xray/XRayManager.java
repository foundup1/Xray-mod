package com.xraymod.client.xray;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import java.util.*;

public class XRayManager {

    private boolean enabled = false;

    private final Set<Block> visibleBlocks = new HashSet<>(Arrays.asList(
            Blocks.DIAMOND_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.EMERALD_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.GOLD_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.IRON_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.COAL_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.COPPER_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.LAPIS_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.REDSTONE_ORE,
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.NETHER_GOLD_ORE,
            Blocks.NETHER_QUARTZ_ORE,
            Blocks.ANCIENT_DEBRIS,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.ENDER_CHEST,
            Blocks.SPAWNER,
            Blocks.TRIAL_SPAWNER
    ));

    private final Map<Block, Boolean> blockToggleMap = new LinkedHashMap<>();

    public XRayManager() {
        for (Block block : visibleBlocks) {
            blockToggleMap.put(block, true);
        }
    }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void toggle() { this.enabled = !this.enabled; }

    public boolean shouldHideBlock(Block block) {
        if (!enabled) return false;
        return !blockToggleMap.getOrDefault(block, false);
    }

    public Map<Block, Boolean> getBlockToggleMap() { return blockToggleMap; }

    public void toggleBlock(Block block) {
        blockToggleMap.put(block, !blockToggleMap.getOrDefault(block, true));
    }

    public void setBlockEnabled(Block block, boolean enabled) {
        blockToggleMap.put(block, enabled);
    }
}
