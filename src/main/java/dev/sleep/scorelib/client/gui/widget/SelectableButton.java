package dev.sleep.scorelib.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.sleep.scorelib.client.gui.GuiUtil;
import net.minecraft.util.ResourceLocation;

public class SelectableButton extends AbstractButton {

    public boolean selected = false;

    public SelectableButton(ResourceLocation buttonTexture, int positionX, int positionY, int width, int height, int defaultU, int defaultV, int hoveredU, int hoveredV, IPressable pressable) {
        super(buttonTexture, positionX, positionY, width, height, defaultU, defaultV, hoveredU, hoveredV, pressable);
    }

    @Override
    public void drawButtonAndComponents(MatrixStack matrixStack, boolean hovered) {
        renderToolTip(matrixStack, x, y);

        if (selected || hovered) {
            if (HOVERED_U != -3892389 || HOVERED_V != -3892389) {
                GuiUtil.drawTexture(DEFAULT_BUTTON_TEXTURE, matrixStack, x, y - 1, HOVERED_U, HOVERED_V, width + 2, height + 2, 0);
                return;
            }

            GuiUtil.drawTexture(DEFAULT_BUTTON_TEXTURE, matrixStack, x, y, DEFAULT_U, DEFAULT_V, width, height, 0);
            return;
        }

        GuiUtil.drawTexture(DEFAULT_BUTTON_TEXTURE, matrixStack, x, y, DEFAULT_U, DEFAULT_V, width, height, 0);
    }

}
