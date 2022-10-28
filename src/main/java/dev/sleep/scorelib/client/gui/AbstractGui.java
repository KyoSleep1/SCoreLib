package dev.sleep.scorelib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class AbstractGui extends Screen {

    protected final int CLOSE_KEY_VALUE;

    public AbstractGui(String guiName, int closeKeyValue) {
        super(new StringTextComponent(guiName));
        CLOSE_KEY_VALUE = closeKeyValue;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().options.hideGui = false;
        super.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int p_231046_2_, int p_231046_3_) {
        if (CLOSE_KEY_VALUE == keyCode) {
            onClose();
            return true;
        }

        return super.keyPressed(keyCode, p_231046_2_, p_231046_3_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
