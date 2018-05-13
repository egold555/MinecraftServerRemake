package org.golde.test.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.golde.test.util.Location;
import org.golde.test.util.PacketManager;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.packet.Packet;

public class EntityPlayer extends EntityLiving {

	private final Session session;
	private final GameProfile gameProfile;
	
	public EntityPlayer(final int id, Location location, final Session session) {
		super(id, location);
		this.session = session;
		gameProfile = session.getFlag(MinecraftConstants.PROFILE_KEY);
	}
	
	public Session getSession() {
		return session;
	}
	
	/*public void sendPacket(Packet packet) {
		session.send(packet);
	}*/
	
	public GameProfile getGameProfile() {
		return gameProfile;
	}
	
	public UUID getUniqueId() {
		return gameProfile.getId();
	}
	
	public String getName() {
		return gameProfile.getName();
	}
	
	@Deprecated
	public void sendChatMessage(String msg) {
		PacketManager.Players.sendChatMessageTo(this, msg);
	}
	
	public void sendChatMessage(Message msg) {
		PacketManager.Players.sendChatMessageTo(this, msg);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EntityPlayer) {
			return getId() == ((EntityPlayer)obj).getId();
		}
		return super.equals(obj);
	}
	
	public  List<Packet> getSpawnPackets() {
        List<Packet> list = new ArrayList<Packet>();
        list.add(new ServerSpawnPlayerPacket(getId(), getUniqueId(), getLocation().getX(), getLocation().getY(), getLocation().getZ(), getLocation().getYaw(), getLocation().getPitch(), new EntityMetadata[0]));
        list.add(new ServerEntityHeadLookPacket(getId(), (float) getLocation().getYaw()));
        // Inventory and etc.

        // TODO

        return list;
    }

	@Override
	public int getMaxHealth() {
		return 20;
	}

}
