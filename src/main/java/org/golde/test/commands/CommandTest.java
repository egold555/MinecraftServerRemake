package org.golde.test.commands;

import java.util.Arrays;

import org.golde.test.entities.EntityPlayer;

public class CommandTest extends Command{

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public void execute(EntityPlayer player, String[] args) {
		player.sendChatMessage("This is a test command! Args: " + Arrays.toString(args));
	}

}
