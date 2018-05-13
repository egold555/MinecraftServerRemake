package org.golde.test.util;

import org.golde.test._Main;
import org.golde.test.entities.EntityPlayer;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.packetlib.packet.Packet;

public class PacketManager {

	public static class Players {
		
		public static void sendPacketTo(EntityPlayer player, Packet packet) {
			player.getSession().send(packet);
			Log.packetOut(Log.toStringObj(packet) + " TO: " + Log.toStringObj(player));
		}
		
		public static void sendPacketToEveryone(Packet packet) {
			for(EntityPlayer player: _Main.getInstance().getOnlinePlayers()) {
				sendPacketTo(player, packet);
			}
		}
		
		public static void sendPacketToEveryoneExcept(EntityPlayer except, Packet packet) {
			for(EntityPlayer player: _Main.getInstance().getOnlinePlayers()) {
				if(!(player.getUniqueId().equals(except.getUniqueId()))) {
					sendPacketTo(player, packet);
				}
			}
		}
		
		@Deprecated
		public static void sendChatMessageTo(EntityPlayer player, String msg) {
			sendChatMessageTo(player, new TextMessage(msg));
		}
		
		public static void sendChatMessageTo(EntityPlayer player, Message msg) {
			sendPacketTo(player, new ServerChatPacket(msg));
		}
		
		@Deprecated
		public static void sendChatMessageToEveryone(String msg) {
			sendChatMessageToEveryone(new TextMessage(msg));
		}
		
		public static void sendChatMessageToEveryone(Message msg) {
			for(EntityPlayer player: _Main.getInstance().getOnlinePlayers()) {
				sendChatMessageTo(player, msg);
			}
		}
		
		@Deprecated
		public static void sendChatMessageToEveryoneExcept(EntityPlayer except, String msg) {
			sendChatMessageToEveryone(new TextMessage(msg));
		}
		
		public static void sendChatMessageToEveryoneExcept(EntityPlayer except, Message msg) {
			for(EntityPlayer player: _Main.getInstance().getOnlinePlayers()) {
				if(!(player.getUniqueId().equals(except.getUniqueId()))) {
					sendChatMessageTo(player, msg);
				}
			}
		}
		
	}
	
	public static class Handlers {
		
	}
	
}
