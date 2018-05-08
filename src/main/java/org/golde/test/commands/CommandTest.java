package org.golde.test.commands;

import com.github.steveice10.packetlib.Session;

public class CommandTest extends Command{

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public void execute(Session session) {
		sendChatMessage(session, "This is a test command!");
	}

}
