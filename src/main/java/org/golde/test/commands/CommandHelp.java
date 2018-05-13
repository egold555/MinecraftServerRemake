package org.golde.test.commands;

import java.util.List;

import org.golde.test.entities.EntityPlayer;

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
	public void execute(EntityPlayer player, String[] args) throws Exception {
		player.sendChatMessage("There are " + registered.size() + " commands registered. That's all I know. Try /<tab> ?");
	}

}
