package org.golde.test.commands;

import org.golde.test.entities.EntityPlayer;
import org.golde.test.util.PacketManager;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;

public class CommandTeleport extends Command {

	@Override
	public String getName() {
		return "tp";
	}

	@Override
	public String[] getArgs() {
		return new String[] {"x", "y", "z"};
	}
	
	@Override
	public void execute(EntityPlayer player, String[] args) throws Exception {
		PacketManager.Players.sendPacketToEveryone(
				new ServerEntityTeleportPacket(
						player.getId(), 
						Double.parseDouble(args[0]),
						Double.parseDouble(args[1]),
						Double.parseDouble(args[2]), 
						0, //Yaw
						0, //Pitch
						false //onGround
					)
				);
	}

	@Override
	public String[] onTabComplete(EntityPlayer player, int index) {
		if(index == 0) {
			return doubleConvert(player.getLocation().getX());
		}
		else if(index == 1) {
			return doubleConvert(player.getLocation().getY());
		}
		else if(index == 2) {
			return doubleConvert(player.getLocation().getZ());
		}
		return null;
	}

}
