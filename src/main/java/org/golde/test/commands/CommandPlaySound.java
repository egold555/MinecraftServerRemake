package org.golde.test.commands;

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.SoundCategory;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import com.github.steveice10.packetlib.Session;

public class CommandPlaySound extends Command {

	@Override
	public String getName() {
		return "playsound";
	}
	
	@Override
	public String[] getArgs() {
		return new String[] {"sound", "category", "x", "y", "z", "volume", "pitch"};
	}

	@Override
	public void execute(Session session, String[] args) throws Exception {
		session.send(new ServerPlayBuiltinSoundPacket(BuiltinSound.valueOf(args[0]), SoundCategory.valueOf(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]), Float.parseFloat(args[6])));
	}
	
	@Override
	public String[] onTabComplete(int index) {
		if(index == 0) {
			return enumArrayToStringArray(BuiltinSound.class);
		}
		if(index == 1) {
			return enumArrayToStringArray(SoundCategory.class);
		}
		return null;
	}

}
