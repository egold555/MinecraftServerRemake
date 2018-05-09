package org.golde.test.commands;

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.world.notify.ClientNotification;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.github.steveice10.packetlib.Session;

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
	public void execute(Session session, String[] args) {
		GameMode gm = GameMode.valueOf(args[0]);
		session.send(new ServerNotifyClientPacket(ClientNotification.CHANGE_GAMEMODE, gm));
		sendChatMessage(session, "Updated gamemode to: " + gm.name());
	}

	@Override
	public String[] onTabComplete(int index) {
		return enumArrayToStringArray(GameMode.class);
	}

}
