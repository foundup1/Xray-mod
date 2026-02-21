package com.xraymod.client.gui;

import com.xraymod.client.XRayModClient;
import com.xraymod.client.xray.XRayManager;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.registry.Registries;
import java.util.*;

public class XRayScreen extends Screen {

    private static final int PANEL_WIDTH = 280;
    private static final int PANEL_HEIGHT = 380;
    private static final int BACKGROUND_COLOR = 0xCC0A0A1A;
    private static final int HEADER_COLOR = 0xFF1A1A3E;
    private static final int ACCENT_COLOR = 0xFF5555FF;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private ButtonWidget masterToggleButton;
    private final List<BlockToggleButton> blockButtons = new ArrayList<>();

    public XRayScreen() {
        super(Text.literal("§b§lX-Ray §7| §fMenu"));
    }

    @Override
    protected void init() {
        super.init();
        blockButtons.clear();

        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;

        masterToggleButton = ButtonWidget.builder(
                getMasterToggleText(),
                btn -> {
                    XRayModClient.xRayManager.toggle();
                    btn.setMessage(getMasterToggleText());
                    if (this.client != null && this.client.worldRenderer != null) {
                        this.client.worldRenderer.reload();
                    }
                }
        ).dimensions(panelX + 10, panelY + 40, PANEL_WIDTH - 20, 25).build();
        this.addDrawableChild(masterToggleButton);

        XRayManager manager = XRayModClient.xRayManager;
        Map<Block, Boolean> blockMap = manager.getBlockToggleMap();

        int buttonY = panelY + 80;
        int col = 0;
        int buttonW = (PANEL_WIDTH - 30) / 2;

        for (Map.Entry<Block, Boolean> entry : blockMap.entrySet()) {
            Block block = entry.getKey();
            int bx = panelX + 10 + col * (buttonW + 10);
            String blockName = getShortBlockName(block);
            BlockToggleButton btn = new BlockToggleButton(block, blockName, bx, buttonY, buttonW, 20, manager);
            this.addDrawableChild(btn);
            blockButtons.add(btn);
            col++;
            if (col >= 2) {
                col = 0;
                buttonY += 24;
            }
        }

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§cЗакрыть"),
                btn -> this.close()
        ).dimensions(panelX + 10, panelY + PANEL_HEIGHT - 35, PANEL_WIDTH - 20, 25).build());
    }

    private Text getMasterToggleText() {
        boolean enabled = XRayModClient.xRayManager.isEnabled();
        return Text.literal("X-Ray: " + (enabled ? "§a§lВКЛЮЧЁН ✔" : "§c§lВЫКЛЮЧЕН ✘"));
    }

    private String getShortBlockName(Block block) {
        String name = Registries.BLOCK.getId(block).getPath()
                .replace("_ore", "").replace("deepslate_", "DS ")
                .replace("nether_", "N/").replace("ancient_debris", "Анц.мусор")
                .replace("diamond", "Алмаз").replace("emerald", "Изумруд")
                .replace("gold", "Золото").replace("iron", "Железо")
                .replace("coal", "Уголь").replace("copper", "Медь")
                .replace("lapis", "Лазурит").replace("redstone", "Красн.")
                .replace("quartz", "Кварц").replace("chest", "Сундук")
                .replace("trapped_", "Лов.").replace("ender_", "Эндер ")
                .replace("spawner", "Спавнер").replace("trial_", "Исп.");
        return name.isEmpty() ? name : Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;
        this.renderBackground(context, mouseX, mouseY, delta);
        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, BACKGROUND_COLOR);
        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + 32, HEADER_COLOR);
        context.fill(panelX, panelY + 32, panelX + PANEL_WIDTH, panelY + 34, ACCENT_COLOR);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("§b§lX-RAY §7CHEAT CLIENT"), this.width / 2, panelY + 10, TEXT_COLOR);
        context.drawTextWithShadow(this.textRenderer,
                Text.literal("§7Видимые блоки:"), panelX + 10, panelY + 72, 0xFFAAAAAA);
        boolean enabled = XRayModClient.xRayManager.isEnabled();
        context.drawTextWithShadow(this.textRenderer,
                Text.literal("Статус: " + (enabled ? "§aАКТИВЕН" : "§cНЕАКТИВЕН")),
                panelX + PANEL_WIDTH - 100, panelY + 72, TEXT_COLOR);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }

    private static class BlockToggleButton extends ButtonWidget {
        private final Block block;
        private final XRayManager manager;
        private final String name;

        public BlockToggleButton(Block block, String name, int x, int y, int width, int height, XRayManager manager) {
            super(x, y, width, height,
                    Text.literal(getLabel(name, manager.getBlockToggleMap().getOrDefault(block, true))),
                    btn -> {
                        manager.toggleBlock(block);
                        btn.setMessage(Text.literal(getLabel(name, manager.getBlockToggleMap().getOrDefault(block, true))));
                    },
                    DEFAULT_NARRATION_SUPPLIER);
            this.block = block;
            this.manager = manager;
            this.name = name;
        }

        private static String getLabel(String name, boolean enabled) {
            return (enabled ? "§a✔ " : "§c✘ ") + "§f" + name;
        }
    }
}
