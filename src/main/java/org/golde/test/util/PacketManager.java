package org.golde.test.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.golde.test._Main;
import org.golde.test.commands.Command;
import org.golde.test.entities.EntityPlayer;

import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.PositionElement;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.data.game.world.WorldType;
import com.github.steveice10.mc.protocol.data.message.ChatColor;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.MessageStyle;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.packetlib.Session;
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
			sendChatMessageToEveryoneExcept(except, new TextMessage(msg));
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
		
		public static void handleClientPlayerPositionPacket(ClientPlayerPositionPacket packetIn, EntityPlayer player) {
			player.setLocation(new Location(packetIn.getX(), packetIn.getY(), packetIn.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), packetIn.isOnGround()));
		}
		
		public static void handleClientPlayerPositionRotationPacket(ClientPlayerPositionRotationPacket packetIn, EntityPlayer player) {
			player.setLocation(new Location(packetIn.getX(), packetIn.getY(), packetIn.getZ(), (float)packetIn.getYaw(), (float)packetIn.getPitch(), packetIn.isOnGround()));
		}
		
		public static void handleClientPlayerRotationPacket(ClientPlayerRotationPacket packetIn, EntityPlayer player) {
			player.setLocation(new Location(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), (float)packetIn.getYaw(), (float)packetIn.getPitch(), player.getLocation().isOnGround()));
		}
		
		public static void handleClientPlayerMovementPacket(ClientPlayerMovementPacket packetIn, EntityPlayer player) {
			player.setLocation(new Location(packetIn.getX(), packetIn.getY(), packetIn.getZ(), (float)packetIn.getYaw(), (float)packetIn.getPitch()));
		}
		
		public static void handleClientChatPacketCommand(ClientChatPacket packetIn, EntityPlayer player) {
			for(Command cmd : _Main.getInstance().getCommands()) {
				String comm = packetIn.getMessage().substring(1, packetIn.getMessage().length());
				String[] split = StringUtils.splitBySpace(comm);
				if(cmd.getName().equalsIgnoreCase(split[0])) {
					String[] args = StringUtils.nudgeArrayDownByXRemovingFirstToLast(split, 1);
					if(args.length < cmd.getArgs().length) {
						cmd.notEnoughArgs(player);
						return;
					}
					try {
						cmd.execute(player, args);
						return;
					} catch (Exception e) {
						player.sendChatMessage(ExceptionUtils.getMessage(e));
						cmd.notEnoughArgs(player);
						e.printStackTrace();
						return;
					}


				}
			}

			player.sendChatMessage(new TextMessage("Unknown command!"));
		}
		
		public static void handleClientChatPacketChat(ClientChatPacket packetIn, EntityPlayer player) {
			Log.info("[CHAT] " + player.getName() + ": " + packetIn.getMessage());
			PacketManager.Players.sendChatMessageToEveryone("[" + player.getName() + "] " + packetIn.getMessage());
		}
		
		public static void handelClientTabCompletePacket(ClientTabCompletePacket packetIn, EntityPlayer player) {
			String rawText = packetIn.getText();

			if(rawText.length() >= 1 && rawText.charAt(0) == '/' && !rawText.contains(" ")) {
				List<String> sortedCommands =new ArrayList<String>();
				for(int i=0; i < _Main.getInstance().getCommands().size(); i++) {
					if(_Main.getInstance().getCommands().get(i).getName().toLowerCase().startsWith(rawText.substring(1, rawText.length()).toLowerCase())) {
						sortedCommands.add("/" + _Main.getInstance().getCommands().get(i).getName());
					}
				}
				PacketManager.Players.sendPacketTo(player, new ServerTabCompletePacket(sortedCommands.toArray(new String[0])));
				return;
			}

			String[] split = StringUtils.splitBySpace(rawText.substring(1, packetIn.getText().length()));
			for(Command cmd : _Main.getInstance().getCommands()) {
				if(cmd.getName().equalsIgnoreCase(split[0])){
					String[] args = StringUtils.nudgeArrayDownByXRemovingFirstToLast(split, 1);
					int argumentIndex = args.length - 1;
					if (rawText.endsWith(" ")) {
						argumentIndex += 1;
					}

					String[] toSend = cmd.onTabComplete(player, argumentIndex);
					if(toSend != null) {
						if (rawText.endsWith(" ")) {
							PacketManager.Players.sendPacketTo(player, new ServerTabCompletePacket(toSend));
						}
						else {
							String lastSplit = split[split.length -1];
							List<String> sortedArgs =new ArrayList<String>();
							for(int i=0; i < toSend.length; i++) {
								if(toSend[i].toLowerCase().startsWith(lastSplit.toLowerCase())) {
									sortedArgs.add(toSend[i]);
								}
							}
							PacketManager.Players.sendPacketTo(player, new ServerTabCompletePacket(sortedArgs.toArray(new String[0])));
						}
						return;
					}
				}
			}
		}
		
		public static void handleClientDisconnect(EntityPlayer player) {
			
			PacketManager.Players.sendPacketToEveryone(new ServerPlayerListEntryPacket(PlayerListEntryAction.REMOVE_PLAYER, new PlayerListEntry[] {new PlayerListEntry(player.getGameProfile(), GameMode.CREATIVE, 1, new TextMessage(player.getName()))}));
			PacketManager.Players.sendChatMessageToEveryone(new TextMessage(player.getName() + " left the game.").setStyle(new MessageStyle().setColor(ChatColor.LIGHT_PURPLE)));

			Log.info(player.getName() + " left the game");
			_Main.getInstance().getEntities().remove(player).getId();
			
			PacketManager.Players.sendPacketToEveryoneExcept(player, new ServerEntityDestroyPacket(player.getId()));
			
		}
		
		public static void handleClientJoinGame(EntityPlayer player, Location spawnLocation) {

			_Main.getInstance().getEntities().put(player.getId(), player);

			//http://wiki.vg/Protocol_FAQ#What.27s_the_normal_login_sequence_for_a_client.3F
			PacketManager.Players.sendPacketTo(player, new ServerJoinGamePacket(player.getId(), false, GameMode.CREATIVE, 0, Difficulty.PEACEFUL, 100, WorldType.DEFAULT, false));
			//session.send(new ServerPluginMessagePacket("MC|Brand", "vanilla".getBytes()));
			PacketManager.Players.sendPacketTo(player, new ServerDifficultyPacket(Difficulty.PEACEFUL));
			PacketManager.Players.sendPacketTo(player, new ServerSpawnPositionPacket(new Position((int)spawnLocation.getX(), (int)spawnLocation.getY(), (int)spawnLocation.getZ())));
			//http://wiki.vg/Protocol#Entity_Properties
			PacketManager.Players.sendPacketTo(player, new ServerPlayerAbilitiesPacket(true, true, true, true, 0.05F, 0.100000001490116f));
			PacketManager.Players.sendPacketTo(player, new ServerPlayerPositionRotationPacket(5, 255, 5, 0, 0, 0, new PositionElement[0]));

			Log.info(player.getName() + " joined the game");
			
			PacketManager.Players.sendChatMessageToEveryone(new TextMessage(player.getName() + " joined the game.").setStyle(new MessageStyle().setColor(ChatColor.LIGHT_PURPLE)));
			PacketManager.Players.sendPacketToEveryone(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[] {new PlayerListEntry(player.getGameProfile(), GameMode.CREATIVE, 1, new TextMessage(player.getName()))}));
			
			for(Packet packet : player.getSpawnPackets()) {
				PacketManager.Players.sendPacketToEveryoneExcept(player, packet);
			}
		}
		
		public static void handleClientPluginMessagePacket(ClientPluginMessagePacket packetIn, EntityPlayer player) {
			for(EntityPlayer other : _Main.getInstance().getOnlinePlayers()) {
				for(Packet spacket : other.getSpawnPackets()) {
					PacketManager.Players.sendPacketToEveryoneExcept(other, spacket);
				}
			}
		}
		
	}
	
}
