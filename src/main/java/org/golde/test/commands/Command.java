package org.golde.test.commands;

import java.util.Arrays;

import org.golde.test.entities.EntityPlayer;

public abstract class Command {

	public abstract String getName();
	
	public String[] getArgs() {
		return new String[0];
	}
	
	public String[] onTabComplete(EntityPlayer player, int index) {
		return null;
	}
	
	public abstract void execute(EntityPlayer player, String[] args) throws Exception;
	
	public final void notEnoughArgs(EntityPlayer player) {
		player.sendChatMessage("Not enough args! " + getName() + " " + Arrays.toString(getArgs()));
	}
	
	protected static final String[] enumArrayToStringArray(Class<? extends Enum<?>> enumType){
		String[] toReturn = new String[enumType.getEnumConstants().length];
		
		for(int i = 0; i < toReturn.length; i++) {
			toReturn[i] = enumType.getEnumConstants()[i].name();
		}
		
		return toReturn;
	}
	
	protected final String[] doubleConvert(double num) {
		return new String[] {String.valueOf(num)};
	}

	
}
