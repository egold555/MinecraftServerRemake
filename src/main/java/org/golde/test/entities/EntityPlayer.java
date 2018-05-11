package org.golde.test.entities;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.packetlib.Session;

public class EntityPlayer extends Entity {

	private final Session session;
	private final GameProfile gameProfile;
	
	public EntityPlayer(final int id, final Session session) {
		super(id);
		this.session = session;
		gameProfile = session.getFlag(MinecraftConstants.PROFILE_KEY);
	}
	
	public Session getSession() {
		return session;
	}
	
	public GameProfile getGameProfile() {
		return gameProfile;
	}
	
	public String getName() {
		return gameProfile.getName();
	}
	
	public void sendChatMessage(String msg) {
		sendChatMessage(new TextMessage(msg));
	}
	
	public void sendChatMessage(Message msg) {
		session.send(new ServerChatPacket(msg));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EntityPlayer) {
			return getId() == ((EntityPlayer)obj).getId();
		}
		return super.equals(obj);
	}
	
	

}
