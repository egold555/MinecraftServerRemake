package org.golde.test.commands;

import org.golde.test.entities.EntityPlayer;
import org.golde.test.util.PacketManager;

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.SoundCategory;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;

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
	public void execute(EntityPlayer player, String[] args) throws Exception {
		PacketManager.Players.sendPacketToEveryone(new ServerPlayBuiltinSoundPacket(BuiltinSound.valueOf(args[0]), SoundCategory.valueOf(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Float.parseFloat(args[5]), Float.parseFloat(args[6])));
	}
	
	@Override
	public String[] onTabComplete(EntityPlayer player, int index) {
		if(index == 0) {
			return enumArrayToStringArray(BuiltinSound.class);
		}
		else if(index == 1) {
			return enumArrayToStringArray(SoundCategory.class);
		}
		else if(index == 2) {
			return doubleConvert(player.getLocation().getX());
		}
		else if(index == 3) {
			return doubleConvert(player.getLocation().getY());
		}
		else if(index == 4) {
			return doubleConvert(player.getLocation().getZ());
		}
		else if(index == 5 || index == 6) {
			return doubleConvert(1);
		}
		return null;
	}
	
	
}
