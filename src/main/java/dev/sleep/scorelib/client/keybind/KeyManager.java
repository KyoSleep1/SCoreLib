package dev.sleep.scorelib.client.keybind;

import dev.sleep.scorelib.common.AbstractManager;
import dev.sleep.scorelib.common.config.AbstractConfigFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.Arrays;

public class KeyManager extends AbstractManager<String, AbstractKeybind> {

    public final static KeyManager INSTANCE = new KeyManager();

    public void executeKeyIfPossible(InputMappings.Input inputMappings, int action, boolean shouldRunOnTick) {
        AbstractClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        if (clientPlayer == null) {
            return;
        }

        for (AbstractKeybind keybind : this.getRegisteredObjectList().values()) {
            if (keybind.getKeyBinding() == null || shouldRunOnTick != keybind.executeOnClientTick) {
                continue;
            }

            if (!keybind.canUse(clientPlayer) || !keybind.pressed(inputMappings, action)) {
                continue;
            }

            keybind.tick(clientPlayer);
            break;
        }
    }

    @Override
    public void registerObject(String keyIdentifier, AbstractKeybind keybind) {
        KeyBinding[] vanillaKeybinds = Minecraft.getInstance().options.keyMappings;
        KeyBinding mcKeybind = keybind.getKeyBinding();

        if (mcKeybind != null && !Arrays.asList(vanillaKeybinds).contains(mcKeybind)) {
            ClientRegistry.registerKeyBinding(keybind.getKeyBinding());
        }

        super.registerObject(keyIdentifier, keybind);
    }


    @Override
    public AbstractManager<String, AbstractKeybind> getInstance() {
        return INSTANCE;
    }
}
