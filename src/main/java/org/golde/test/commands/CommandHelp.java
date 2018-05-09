package org.golde.test.commands;

import java.util.List;

import com.github.steveice10.packetlib.Session;

public class CommandHelp extends Command {

	private final List<Command> registered;
	
	public CommandHelp(List<Command> reg) {
		this.registered = reg;
	}
	
	@Override
	public String getName() {
		return "help";
	}

	@Override
	public void execute(Session session, String[] args) throws Exception {
		sendChatMessage(session, "There are " + registered.size() + " commands registered. That's all I know. Try /<tab> ?");
	}

}
