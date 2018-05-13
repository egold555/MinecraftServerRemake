package org.golde.test.commands;

import java.util.List;

import org.golde.test._Main;
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
		String toSend = "Commands: \n";
		for(Command cmd: _Main.getInstance().getCommands()) {
			toSend += "  /" + cmd.getName() + "\n";
		}
		player.sendChatMessage(toSend);
	}

}
