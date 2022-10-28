package dev.sleep.scorelib.common.config;

import lombok.Getter;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public abstract class AbstractConfigFile {

    @Getter
    private final ForgeConfigSpec.Builder Builder;

    @Getter
    private final String Identifier;

    @Getter
    private final ModConfig.Type Side;

    @Getter
    private ForgeConfigSpec Spec;

    public AbstractConfigFile(String modID, String fileIdentifier, ModConfig.Type configSide) {
        this.Builder = new ForgeConfigSpec.Builder();

        this.Identifier = formatName(modID, fileIdentifier);
        this.Side = configSide;

        this.build();
        this.register();
    }

    public abstract void build();

    private String formatName(String modID, String fileIdentifier) {
        return modID + "-" + fileIdentifier.toLowerCase() + ".toml";
    }

    private void register() {
        ModLoadingContext.get().registerConfig(Side, Spec, Identifier);
    }
}
