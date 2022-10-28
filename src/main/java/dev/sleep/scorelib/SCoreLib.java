package dev.sleep.scorelib;

import lombok.Getter;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class SCoreLib {

    @Getter
    private static final Logger logger = LogManager.getLogger();

    private static void registerListeners(IEventBus bus, Consumer<? extends Event>[] listenersList) {
        for (Consumer<? extends Event> consumer : listenersList) {
            bus.addListener(consumer);
        }

        logger.info(SCoreLibReference.LIBPREFIX + "Event listeners registered.");
    }
}
