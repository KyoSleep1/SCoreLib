package dev.sleep.scorelib.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class GuiUtil {

    public static void drawCenteredFontWithBlackOutline(MatrixStack matrixStackIn, FontRenderer font, String text, int positionX, int positionY, int colorHex) {
        AbstractGui.drawCenteredString(matrixStackIn, font, text, positionX + 1, positionY, 0);
        AbstractGui.drawCenteredString(matrixStackIn, font, text, positionX - 1, positionY, 0);

        AbstractGui.drawCenteredString(matrixStackIn, font, text, positionX, positionY + 1, 0);
        AbstractGui.drawCenteredString(matrixStackIn, font, text, positionX, positionY - 1, 0);

        AbstractGui.drawCenteredString(matrixStackIn, font, text, positionX, positionY, colorHex);
    }

    public static void drawCenteredFont(MatrixStack matrixStackIn, FontRenderer font, String text, int positionX, int positionY, int colorHex) {
        AbstractGui.drawCenteredString(matrixStackIn, font, text, positionX, positionY, colorHex);
    }

    public static void drawFont(MatrixStack matrixStackIn, FontRenderer font, String text, int positionX, int positionY, int colorHex) {
        font.draw(matrixStackIn, text, positionX, positionY, colorHex);
    }

    public static void drawTexture(ResourceLocation resourceLocation, MatrixStack matrixStack, int positionX, int positionY, int u, int v, int width, int height, int zLevel) {
        bindTexture(resourceLocation);
        GuiUtils.drawTexturedModalRect(matrixStack, positionX, positionY, u, v, width, height, zLevel);
    }

    public static void bindTexture(ResourceLocation resourceLocation) {
        Minecraft.getInstance().getTextureManager().bind(resourceLocation);
    }

    public static int getCenterX(int width) {
        return (getWidthRelativeToScreen()) - width / 2;
    }

    public static int getCenterY(int height) {
        return (getHeightRelativeToScreen()) - height / 2;
    }

    public static int getWidthRelativeToScreen() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
    }

    public static int getHeightRelativeToScreen() {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2;
    }

    public static int getLeftPos(int screenWidth, int imageWidth) {
        return (screenWidth - imageWidth) / 2;
    }

    public static int getRightPos(int screenHeight, int imageHeight) {
        return (screenHeight - imageHeight) / 2;
    }

}
