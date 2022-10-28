package dev.sleep.scorelib.client;

import net.minecraftforge.client.event.EntityViewRenderEvent;

public class CameraShaker {

    private static boolean shouldShakeScreen = false;
    private static float shakeProgress = 0;

    public static void shakeCameraIfPossible(EntityViewRenderEvent.CameraSetup e, int shakeAmplitude, int resetShakeAfterTicks) {
        if (!shouldShakeScreen) {
            return;
        }

        shakeCamera(e, shakeAmplitude, resetShakeAfterTicks);
    }

    private static void shakeCamera(EntityViewRenderEvent.CameraSetup e, int shakeAmplitude, int resetShakeAfterTicks) {
        shakeProgress++;

        e.setRoll(e.getRoll() + randomWithBounds(-0.5F, 0.5F) * shakeAmplitude);
        e.setPitch(e.getPitch() + randomWithBounds(-0.25f, 0.25f) * shakeAmplitude);

        if(shakeProgress >= resetShakeAfterTicks){
            shouldShakeScreen = false;
            shakeProgress = 0;
        }
    }

    private static float randomWithBounds(float min, float max) {
        return (float) (Math.random() * (max - min + 1) + min);
    }
}
