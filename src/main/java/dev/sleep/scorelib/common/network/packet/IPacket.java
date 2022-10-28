package dev.sleep.scorelib.common.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IPacket {

    void toBytes(final PacketBuffer buf);

    void fromBytes(final PacketBuffer buf);

    default LogicalSide getExecutionSide()
    {
        return null;
    }

    void onExecute(final NetworkEvent.Context ctxIn, final boolean isLogicalServer);
}
