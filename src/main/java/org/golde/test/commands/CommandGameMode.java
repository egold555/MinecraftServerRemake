package org.golde.test.commands;

import org.golde.test.entities.EntityPlayer;

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.world.notify.ClientNotification;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;

public class CommandGameMode extends Command {

	@Override
	public String getName() {
		return "gamemode";
	}

	@Override
	public String[] getArgs() {
		return new String[] {"mode"};
	}

	@Override
	public void execute(EntityPlayer player, String[] args) {
		GameMode gm = GameMode.valueOf(args[0]);
		player.sendPacket(new ServerNotifyClientPacket(ClientNotification.CHANGE_GAMEMODE, gm));
		player.sendChatMessage("Updated gamemode to: " + gm.name());
	}

	@Override
	public String[] onTabComplete(EntityPlayer player, int index) {
		return enumArrayToStringArray(GameMode.class);
	}

}
