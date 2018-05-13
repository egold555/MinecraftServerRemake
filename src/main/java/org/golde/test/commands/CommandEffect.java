package org.golde.test.commands;

import org.golde.test.entities.EntityPlayer;
import org.golde.test.util.PacketManager;

import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;

public class CommandEffect extends Command {

	@Override
	public String getName() {
		return "effect";
	}

	@Override
	public String[] getArgs() {
		return new String[] {"effect", "duration", "amplifier"};
	}

	@Override
	public void execute(EntityPlayer player, String[] args) throws Exception {
		PacketManager.Players.sendPacketTo(player, new ServerEntityEffectPacket(player.getId(), Effect.valueOf(args[0]), Integer.parseInt(args[2]), Integer.parseInt(args[1]), false, true));
	}

	@Override
	public String[] onTabComplete(EntityPlayer player, int index) {
		if(index == 0) {
			return enumArrayToStringArray(Effect.class);
		}
		return null;
	}

}
