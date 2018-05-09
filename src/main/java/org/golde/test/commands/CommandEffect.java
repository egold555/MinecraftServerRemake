package org.golde.test.commands;

import com.github.steveice10.mc.protocol.data.game.entity.Effect;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import com.github.steveice10.packetlib.Session;

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
	public void execute(Session session, String[] args) throws Exception {
		session.send(new ServerEntityEffectPacket(0, Effect.valueOf(args[0]), Integer.parseInt(args[2]), Integer.parseInt(args[1]), true, true));
	}
	
	@Override
	public String[] onTabComplete(int index) {
		if(index == 0) {
			return enumArrayToStringArray(Effect.class);
		}
		return null;
	}

}
