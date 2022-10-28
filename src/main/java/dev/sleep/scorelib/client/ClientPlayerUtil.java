package dev.sleep.scorelib.client;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.function.Predicate;

public class ClientPlayerUtil {

    public static LivingEntity pickLivingEntityWithRaytrace(ClientPlayerEntity clientPlayer, int pickRange, float partialTicks) {
        Vector3d playerEyePosition = clientPlayer.getEyePosition(partialTicks);
        Vector3d playerViewVector = clientPlayer.getViewVector(partialTicks).scale(pickRange);

        Vector3d rayDirectionVector = playerEyePosition.add(playerViewVector);
        AxisAlignedBB aabb = clientPlayer.getBoundingBox().expandTowards(playerViewVector).inflate(1.0D);

        Predicate<Entity> predicate = (lockedEntity) -> !lockedEntity.isSpectator() && lockedEntity.isPickable();
        EntityRayTraceResult entityRayTraceResult = ProjectileHelper.getEntityHitResult(clientPlayer, playerEyePosition, rayDirectionVector, aabb, predicate, pickRange * pickRange);

        if (entityRayTraceResult == null || !(entityRayTraceResult.getEntity() instanceof LivingEntity)) {
            return null;
        }

        return (LivingEntity) entityRayTraceResult.getEntity();
    }

    public static void smoothLookAt(ClientPlayerEntity clientPlayer, Vector3d lookVector, float partialTicks) {
        Vector3d playerEyePosition = clientPlayer.getEyePosition(partialTicks);

        double distanceX = lookVector.x - playerEyePosition.x;
        double distanceY = (lookVector.y - playerEyePosition.y) - 0.4;
        double distanceZ = lookVector.z - playerEyePosition.z;
        double sqrtDistance = MathHelper.sqrt(distanceX * distanceX + distanceZ * distanceZ);

        clientPlayer.xRot = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(distanceY, sqrtDistance) * (double) (180F / (float) Math.PI))));
        clientPlayer.yRot = MathHelper.wrapDegrees((float) (MathHelper.atan2(distanceZ, distanceX) * (double) (180F / (float) Math.PI)) - 90.0F);
        clientPlayer.setYHeadRot(clientPlayer.yRot);
        clientPlayer.xRotO = clientPlayer.xRot;

        clientPlayer.yRotO = clientPlayer.yRot;
        clientPlayer.yHeadRotO = clientPlayer.yHeadRot;
        clientPlayer.yBodyRot = clientPlayer.yHeadRot;
        clientPlayer.yBodyRotO = clientPlayer.yBodyRot;
    }
}
