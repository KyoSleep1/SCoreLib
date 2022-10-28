package dev.sleep.scorelib.client.keybind;

import lombok.Getter;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

public abstract class AbstractKeybind {

    @Getter
    protected final String KeyIdentifier;

    @Getter
    protected final KeyBinding KeyBinding;

    public boolean executeOnClientTick = false;

    public AbstractKeybind(KeyBinding keyBinding, String keyIdentifier) {
        this.KeyBinding = keyBinding;
        this.KeyIdentifier = keyIdentifier;
    }

    public AbstractKeybind(KeyBinding keyBinding, Enum<?> keyIdentifier) {
        this.KeyBinding = keyBinding;
        this.KeyIdentifier = keyIdentifier.toString();
    }

    public abstract void tick(AbstractClientPlayerEntity clientPlayer);

    public boolean pressed(InputMappings.Input inputMappings, int action) {
        if(inputMappings != null){
            return KeyBinding.isActiveAndMatches(inputMappings) && action == GLFW.GLFW_PRESS;
        }

        return KeyBinding.isDown();
    }

    public abstract boolean canUse(PlayerEntity player);
}
