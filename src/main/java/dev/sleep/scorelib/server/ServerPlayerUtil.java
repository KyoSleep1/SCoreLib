package dev.sleep.scorelib.server;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class ServerPlayerUtil {

    public static void cancelPlayerMovement(ServerPlayerEntity serverPlayer) {
        serverPlayer.teleportTo(serverPlayer.xOld, serverPlayer.yOld, serverPlayer.zOld);
        serverPlayer.setDeltaMovement(0, 0, 0);

        serverPlayer.fallDistance = 0;
        serverPlayer.hurtMarked = true;
    }

    public static boolean isNight(ServerPlayerEntity serverPlayer) {
        return serverPlayer.getCommandSenderWorld().isNight();
    }

    public static boolean isRaining(ServerPlayerEntity serverPlayer) {
        return serverPlayer.getCommandSenderWorld().isRaining();
    }

    public static boolean isLookingAtMoon(ServerPlayerEntity serverPlayer) {
        ServerWorld serverWorld = (ServerWorld) serverPlayer.getCommandSenderWorld();
        float moonAngle = (float) (serverWorld.getSunAngle(serverWorld.getDayTime()) + -Math.PI / 2);

        Vector3d lookingAt = serverPlayer.getViewVector(1.0F);
        Vector3d moonAnglePosition = new Vector3d(Math.cos(moonAngle), Math.sin(moonAngle), 0f);

        return lookingAt.dot(moonAnglePosition) >= 0.99;
    }

    public static boolean isFullMoon(ServerPlayerEntity serverPlayer) {
        return getMoonPhase(serverPlayer.getCommandSenderWorld().dayTime()) == 0;
    }

    private static int getMoonPhase(long dayTime) {
        return (int) (dayTime / 24000L % 8L + 8L) % 8;
    }

    public static boolean rayTraceWithEyes(ServerPlayerEntity serverPlayer, RayTraceResult.Type raytraceType) {
        return serverPlayer.pick(10.0D, 1.0F, false).getType() == raytraceType;
    }

    public static LivingEntity pickLivingEntityWithRaytrace(ServerPlayerEntity serverPlayer) {
        Vector3d playerEyePosition = serverPlayer.position().add(0, serverPlayer.getEyeHeight(), 0);

        Vector3d playerLookPosition = serverPlayer.getLookAngle().scale(5);
        Vector3d end = playerEyePosition.add(new Vector3d(1, 1, 1));

        AxisAlignedBB aabb = new AxisAlignedBB(playerEyePosition.x, playerEyePosition.y, playerEyePosition.z, end.x, end.y, end.z).expandTowards(playerLookPosition.x, playerLookPosition.y, playerLookPosition.z);
        Vector3d checkVec = playerEyePosition.add(playerLookPosition);

        for (LivingEntity livingEntity : serverPlayer.getCommandSenderWorld().getLoadedEntitiesOfClass(LivingEntity.class, aabb)) {
            AxisAlignedBB entityBB = livingEntity.getBoundingBox();

            if (!entityBB.intersects(Math.min(playerEyePosition.x, checkVec.x), Math.min(playerEyePosition.y, checkVec.y), Math.min(playerEyePosition.z, checkVec.z),
                    Math.max(playerEyePosition.x, checkVec.x), Math.max(playerEyePosition.y, checkVec.y), Math.max(playerEyePosition.z, checkVec.z))) {
                continue;
            }

            return livingEntity;
        }

        return null;
    }
}
