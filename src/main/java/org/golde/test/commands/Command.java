package org.golde.test.commands;

import java.util.Arrays;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.packetlib.Session;

public abstract class Command {

	public abstract String getName();
	
	public String[] getArgs() {
		return new String[0];
	}
	
	public String[] onTabComplete(int index) {
		return null;
	}
	
	public abstract void execute(Session session, String[] args) throws Exception;
	
	public void notEnoughArgs(Session session) {
		sendChatMessage(session, "Not enough args! " + getName() + " " + Arrays.toString(getArgs()));
	}
	
	protected static final void sendChatMessage(Session session, String msg) {
		sendChatMessage(session, new TextMessage(msg));
	}
	
	protected static final void sendChatMessage(Session session, Message msg) {
		session.send(new ServerChatPacket(msg));
	}
	
	protected static final String[] enumArrayToStringArray(Class<? extends Enum<?>> enumType){
		String[] toReturn = new String[enumType.getEnumConstants().length];
		
		for(int i = 0; i < toReturn.length; i++) {
			toReturn[i] = enumType.getEnumConstants()[i].name();
		}
		
		return toReturn;
	}
	
}
