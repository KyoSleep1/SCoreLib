package dev.sleep.scorelib.common.config;

import dev.sleep.scorelib.common.AbstractManager;

public class ConfigurationManager extends AbstractManager<String, AbstractConfigFile> {

    public static ConfigurationManager INSTANCE = new ConfigurationManager();

    @Override
    public void registerObject(AbstractConfigFile object) {
        super.registerObject(object.getIdentifier(), object);
    }

    @Override
    public AbstractManager<String, AbstractConfigFile> getInstance() {
        return INSTANCE;
    }
}
