package dev.sleep.scorelib.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sleep.scorelib.client.gui.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public abstract class AbstractButton extends Button {

    protected final ResourceLocation DEFAULT_BUTTON_TEXTURE;
    protected final int DEFAULT_U, DEFAULT_V, HOVERED_U, HOVERED_V;

    public AbstractButton(ResourceLocation buttonTexture, int positionX, int positionY, int width, int height, int defaultU, int defaultV, IPressable pressable) {
        super(positionX, positionY, width, height, new StringTextComponent(""), pressable);
        DEFAULT_BUTTON_TEXTURE = buttonTexture;

        DEFAULT_U = defaultU;
        DEFAULT_V = defaultV;

        HOVERED_U = -3892389;
        HOVERED_V = -3892389;
    }

    public AbstractButton(ResourceLocation buttonTexture, int positionX, int positionY, int width, int height, int defaultU, int defaultV, ITextComponent textComponent, IPressable pressable) {
        super(positionX, positionY, width, height, textComponent, pressable);
        DEFAULT_BUTTON_TEXTURE = buttonTexture;

        DEFAULT_U = defaultU;
        DEFAULT_V = defaultV;

        HOVERED_U = -3892389;
        HOVERED_V = -3892389;
    }

    public AbstractButton(ResourceLocation buttonTexture, int positionX, int positionY, int width, int height, int defaultU, int defaultV, int hoveredU, int hoveredV, IPressable pressable) {
        super(positionX, positionY, width, height, new StringTextComponent(""), pressable);
        DEFAULT_BUTTON_TEXTURE = buttonTexture;

        DEFAULT_U = defaultU;
        DEFAULT_V = defaultV;

        HOVERED_U = hoveredU;
        HOVERED_V = hoveredV;
    }

    public AbstractButton(ResourceLocation buttonTexture, int positionX, int positionY, int width, int height, int defaultU, int defaultV, int hoveredU, int hoveredV, ITextComponent textComponent, IPressable pressable) {
        super(positionX, positionY, width, height, textComponent, pressable);
        DEFAULT_BUTTON_TEXTURE = buttonTexture;

        DEFAULT_U = defaultU;
        DEFAULT_V = defaultV;

        HOVERED_U = hoveredU;
        HOVERED_V = hoveredV;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrixStack, int positionX, int positionY, float partialTicks) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();

        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        drawButtonAndComponents(matrixStack, isHovered);
        RenderSystem.disableBlend();
    }

    public void drawButtonAndComponents(MatrixStack matrixStack, boolean hovered) {
        if (hovered) {
            if (HOVERED_U != -3892389 || HOVERED_V != -3892389) {
                GuiUtil.drawTexture(DEFAULT_BUTTON_TEXTURE, matrixStack, x, y - 1, HOVERED_U, HOVERED_V, width + 2, height + 2, 0);
            }

            renderToolTip(matrixStack, x, y);
        }

        GuiUtil.drawTexture(DEFAULT_BUTTON_TEXTURE, matrixStack, x, y, DEFAULT_U, DEFAULT_V, width, height, 0);
        drawText(matrixStack);
    }

    @Override
    public void renderToolTip(@Nonnull MatrixStack p_230443_1_, int p_230443_2_, int p_230443_3_) {
        if (isHovered) {
            super.renderToolTip(p_230443_1_, p_230443_2_, p_230443_3_);
        }
    }

    public void drawText(MatrixStack matrixStack) {
        if (this.getMessage().getString().equals("")) {
            return;
        }

        drawCenteredString(matrixStack, Minecraft.getInstance().font, this.getMessage(), x + this.width / 2, this.y + (this.height - 8) / 2, getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}
