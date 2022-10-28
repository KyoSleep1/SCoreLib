package dev.sleep.scorelib.server;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerTimer {

    private static final Table<UUID, String, Long> ACTIVE_COOLDOWNS_TABLE = HashBasedTable.create();
    private static final Table<UUID, String, Integer> ACTIVE_TICKERS_TABLE = HashBasedTable.create();

    public static boolean canUseWithCooldown(UUID playerUUID, String cooldownKey, int delayInSeconds) {
        Long timeInMillis = ACTIVE_COOLDOWNS_TABLE.get(playerUUID, cooldownKey);

        if (timeInMillis != null) {
            return calculateCooldownRemainder(timeInMillis) <= 0L;
        }

        updateCooldown(playerUUID, cooldownKey, delayInSeconds);
        return true;
    }

    private static long calculateCooldownRemainder(long timeInMillis) {
        return timeInMillis - System.currentTimeMillis();
    }

    public static void updateCooldown(UUID playerUUID, String cooldownKey, int delayInSeconds) {
        ACTIVE_COOLDOWNS_TABLE.put(playerUUID, cooldownKey, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(delayInSeconds));
    }

    public static boolean canUseWithTicker(UUID playerUUID, String tickerKey, boolean isFirstTickValid, int maxTicks) {
        Integer timeInTicks = ACTIVE_TICKERS_TABLE.get(playerUUID, tickerKey);

        if (timeInTicks != null) {
            if (calculateTickRemainder(maxTicks, timeInTicks) <= 0) {
                updateTicker(playerUUID, tickerKey, 0);
                return true;
            }

            updateTicker(playerUUID, tickerKey, timeInTicks + 1);
            return false;
        }

        updateTicker(playerUUID, tickerKey, 0);
        return isFirstTickValid;
    }

    private static int calculateTickRemainder(int maxTicks, int timeInTicks) {
        return maxTicks - timeInTicks;
    }

    public static void updateTicker(UUID playerUUID, String tickerKey, int newValue) {
        ACTIVE_TICKERS_TABLE.put(playerUUID, tickerKey, newValue);
    }

    public static void clearPlayerTimers(UUID uuid) {
        ACTIVE_COOLDOWNS_TABLE.row(uuid).clear();
        ACTIVE_TICKERS_TABLE.row(uuid).clear();
    }
}