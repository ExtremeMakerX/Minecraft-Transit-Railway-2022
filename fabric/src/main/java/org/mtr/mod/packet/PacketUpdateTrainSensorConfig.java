package org.mtr.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.block.BlockTrainSensorBase;

public final class PacketUpdateTrainSensorConfig extends PacketHandler {

	private final BlockPos blockPos;
	private final LongAVLTreeSet filterRouteIds;
	private final boolean stoppedOnly;
	private final boolean movingOnly;
	private final int number;
	private final String[] strings;

	public PacketUpdateTrainSensorConfig(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		final int routeIdCount = packetBufferReceiver.readInt();
		filterRouteIds = new LongAVLTreeSet();
		for (int i = 0; i < routeIdCount; i++) {
			filterRouteIds.add(packetBufferReceiver.readLong());
		}
		stoppedOnly = packetBufferReceiver.readBoolean();
		movingOnly = packetBufferReceiver.readBoolean();
		number = packetBufferReceiver.readInt();
		final int stringCount = packetBufferReceiver.readInt();
		strings = new String[stringCount];
		for (int i = 0; i < stringCount; i++) {
			strings[i] = packetBufferReceiver.readString();
		}
	}

	public PacketUpdateTrainSensorConfig(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, int number, String[] strings) {
		this.blockPos = blockPos;
		this.filterRouteIds = filterRouteIds;
		this.stoppedOnly = stoppedOnly;
		this.movingOnly = movingOnly;
		this.number = number;
		this.strings = strings;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeInt(filterRouteIds.size());
		filterRouteIds.forEach(packetBufferSender::writeLong);
		packetBufferSender.writeBoolean(stoppedOnly);
		packetBufferSender.writeBoolean(movingOnly);
		packetBufferSender.writeInt(number);
		packetBufferSender.writeInt(strings.length);
		for (final String string : strings) {
			packetBufferSender.writeString(string);
		}
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockTrainSensorBase.BlockEntityBase) {
			((BlockTrainSensorBase.BlockEntityBase) entity.data).setData(filterRouteIds, stoppedOnly, movingOnly, number, strings);
		}
	}
}
