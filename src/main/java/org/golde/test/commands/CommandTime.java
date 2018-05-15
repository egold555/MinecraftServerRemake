package org.golde.test.commands;

import org.golde.test.entities.EntityPlayer;
import org.golde.test.util.PacketManager;

import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;

public class CommandTime extends Command {

	@Override
	public String getName() {
		return "time";
	}
	
	@Override
	public String[] getArgs() {
		return new String[] {"time"};
	}

	@Override
	public void execute(EntityPlayer player, String[] args) throws Exception {
		PacketManager.Players.sendPacketToEveryone(new ServerUpdateTimePacket(0, Long.parseLong(args[0])));
	}

}
