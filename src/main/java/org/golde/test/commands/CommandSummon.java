package org.golde.test.commands;

import java.util.UUID;

import org.golde.test._Main;
import org.golde.test.entities.EntityPlayer;
import org.golde.test.util.PacketManager;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;

public class CommandSummon extends Command {

	@Override
	public String getName() {
		return "summon";
	}
	
	@Override
	public String[] getArgs() {
		return new String[] {"mob", "x", "y", "z"};
	}

	@Override
	public void execute(EntityPlayer player, String[] args) throws Exception {
		PacketManager.Players.sendPacketToEveryone(new ServerSpawnMobPacket(_Main.getInstance().getFreeEntityId(), UUID.randomUUID(), MobType.valueOf(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 0, 0, 0, 0, 0, 0, new EntityMetadata[0]));
	}
	
	@Override
	public String[] onTabComplete(EntityPlayer player, int index) {
		if(index == 0) {
			return enumArrayToStringArray(MobType.class);
		}
		else if(index == 1) {
			return doubleConvert(player.getLocation().getX());
		}
		else if(index == 2) {
			return doubleConvert(player.getLocation().getY());
		}
		else if(index == 3) {
			return doubleConvert(player.getLocation().getZ());
		}
		return null;
	}

}
