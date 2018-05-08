package org.golde.test.commands;

import java.util.ArrayList;
import java.util.List;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.packetlib.Session;

public abstract class Command {
	
	public Command() {
		COMMANDS.add(this);
	}

	public static List<Command> COMMANDS = new ArrayList<Command>();
	
	public abstract String getName();
	
	public abstract void execute(Session session, String[] args);
	
	public final void sendChatMessage(Session session, String msg) {
		sendChatMessage(session, new TextMessage(msg));
	}
	
	public final void sendChatMessage(Session session, Message msg) {
		session.send(new ServerChatPacket(msg));
	}
	
}
