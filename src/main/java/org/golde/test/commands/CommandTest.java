package org.golde.test.commands;

import java.util.Arrays;

import com.github.steveice10.packetlib.Session;

public class CommandTest extends Command{

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public void execute(Session session, String[] args) {
		sendChatMessage(session, "This is a test command! Args: " + Arrays.toString(args));
	}

}
