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
	
	protected String[] getArgs() {
		return new String[0];
	}
	
	public abstract void execute(Session session, String[] args) throws Exception;
	
	public final boolean hasEnoughArgs(Session session, String[] args) {
		if(args.length < getArgs().length) {
			sendChatMessage(session, "Not enough args! " + getName() + " " + getArgs());
			return false;
		}
		return true;
	}
	
	protected final void sendChatMessage(Session session, String msg) {
		sendChatMessage(session, new TextMessage(msg));
	}
	
	protected final void sendChatMessage(Session session, Message msg) {
		session.send(new ServerChatPacket(msg));
	}
	
}
