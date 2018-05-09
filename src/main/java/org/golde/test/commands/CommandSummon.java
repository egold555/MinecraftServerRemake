package org.golde.test.commands;

import java.util.UUID;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.packetlib.Session;

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
	public void execute(Session session, String[] args) throws Exception {
		session.send(new ServerSpawnMobPacket(1, UUID.randomUUID(), MobType.valueOf(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 0, 0, 0, 0, 0, 0, new EntityMetadata[0]));
	}
	
	@Override
	public String[] onTabComplete(int index) {
		if(index == 0) {
			return enumArrayToStringArray(MobType.class);
		}
		return null;
	}

}
