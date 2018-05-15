package org.golde.test;

import static com.github.steveice10.mc.protocol.MinecraftConstants.GAME_VERSION;
import static com.github.steveice10.mc.protocol.MinecraftConstants.PROTOCOL_VERSION;
import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_COMPRESSION_THRESHOLD;
import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_INFO_BUILDER_KEY;
import static com.github.steveice10.mc.protocol.MinecraftConstants.SERVER_LOGIN_HANDLER_KEY;
import static com.github.steveice10.mc.protocol.MinecraftConstants.VERIFY_USERS_KEY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.golde.test.commands.Command;
import org.golde.test.commands.CommandEffect;
import org.golde.test.commands.CommandGameMode;
import org.golde.test.commands.CommandHelp;
import org.golde.test.commands.CommandPlaySound;
import org.golde.test.commands.CommandSummon;
import org.golde.test.commands.CommandTeleport;
import org.golde.test.commands.CommandTest;
import org.golde.test.commands.CommandTime;
import org.golde.test.entities.Entity;
import org.golde.test.entities.EntityPlayer;
import org.golde.test.util.Location;
import org.golde.test.util.Log;
import org.golde.test.util.PacketManager;
import org.golde.test.util.StringUtils;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.ServerLoginHandler;
import com.github.steveice10.mc.protocol.data.SubProtocol;
import com.github.steveice10.mc.protocol.data.game.BossBarAction;
import com.github.steveice10.mc.protocol.data.game.BossBarColor;
import com.github.steveice10.mc.protocol.data.game.BossBarDivision;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.player.PositionElement;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.data.game.world.WorldType;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.SoundCategory;
import com.github.steveice10.mc.protocol.data.message.ChatColor;
import com.github.steveice10.mc.protocol.data.message.ChatFormat;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.MessageStyle;
import com.github.steveice10.mc.protocol.data.message.TextMessage;
import com.github.steveice10.mc.protocol.data.status.PlayerInfo;
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo;
import com.github.steveice10.mc.protocol.data.status.VersionInfo;
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.server.ServerAdapter;
import com.github.steveice10.packetlib.event.server.SessionAddedEvent;
import com.github.steveice10.packetlib.event.server.SessionRemovedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.packet.PacketHeader;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;


//Glo 
public class SingleInstance {

	private final String HOST = "localhost";
	private final int PORT = 25560;

	public Server server;
	private List<Command> commands = new ArrayList<Command>();
	private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();


	public void run() throws Exception {
		Log.dbg("Program Start");

		server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory());
		server.setGlobalFlag(VERIFY_USERS_KEY, false);
		server.setGlobalFlag(SERVER_COMPRESSION_THRESHOLD, 100);
		server.setGlobalFlag(SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
			@Override
			public ServerStatusInfo buildInfo(Session session) {
				return new ServerStatusInfo(new VersionInfo(GAME_VERSION, PROTOCOL_VERSION),
						new PlayerInfo(100, 0, new GameProfile[0]), new TextMessage("Hello  World!"), null);
			}
		});

		server.setGlobalFlag(SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
			@Override
			public void loggedIn(Session session) {
				//DO NOT TOUCH

				final int entityId = getFreeEntityId();
				Location spawnLocation = new Location(0, 255, 0);
				final EntityPlayer player = new EntityPlayer(entityId, spawnLocation, session);
				
				PacketManager.Handlers.handleClientJoinGame(player, spawnLocation);

				//Fooling around
				session.send(new ServerBossBarPacket(UUID.randomUUID(), BossBarAction.ADD, new TextMessage("Testing123"), 0.4f, BossBarColor.YELLOW, BossBarDivision.NOTCHES_10, false, false));
				//session.send(new ServerUpdateTimePacket(0, 16000));


				Chunk[] chunks = new Chunk[16];

				for(int i = 0; i < chunks.length; i++) {
					Chunk chunk = new Chunk(true);
					for(int x = 0; x < 16; x++) {
						for(int y = 0; y < 5; y++) {
							for(int z = 0; z < 16; z++) {
								chunk.getBlockLight().fill(15);
								chunk.getBlocks().set(x, y, z, new BlockState(95, i));
							}
						}
					}
					chunks[i] = chunk;
				}

				byte[] biomeData = new byte[256]; //BIOME
				Arrays.fill(biomeData, (byte)12);

				for(int x = 0; x < 3; x++) {
					for(int z = 0; z < 3; z++) {
						session.send(new ServerChunkDataPacket(new Column(x, z, chunks, biomeData, new CompoundTag[0])));
					}
				}

			}
		});

		server.addListener(new ServerAdapter() {
			@Override
			public void sessionAdded(SessionAddedEvent event) {
				event.getSession().addListener(new SessionAdapter() {

					@Override
					public void packetReceived(final PacketReceivedEvent event) {
						final EntityPlayer player = getPlayer(event.getSession());
						//Log except spammy packets
						if(!(event.getPacket() instanceof ClientKeepAlivePacket) && 
								!(event.getPacket() instanceof ClientPlayerPositionPacket) &&
								!(event.getPacket() instanceof ClientPlayerRotationPacket) &&
								!(event.getPacket() instanceof ClientPlayerPositionRotationPacket)) {
							Log.packetIn(Log.toStringObj(event.getPacket()));
						}
						
						
						if(event.getPacket() instanceof ClientPlayerPositionPacket) {
							PacketManager.Handlers.handleClientPlayerPositionPacket((ClientPlayerPositionPacket) event.getPacket(), player);
						}
						
						if(event.getPacket() instanceof ClientPlayerPositionRotationPacket) {
							PacketManager.Handlers.handleClientPlayerPositionRotationPacket((ClientPlayerPositionRotationPacket) event.getPacket(), player);
						}
						
						if(event.getPacket() instanceof ClientPlayerRotationPacket) {
							PacketManager.Handlers.handleClientPlayerRotationPacket((ClientPlayerRotationPacket) event.getPacket(), player);
						}
						
						if(event.getPacket() instanceof ClientPlayerMovementPacket) {
							PacketManager.Handlers.handleClientPlayerMovementPacket((ClientPlayerMovementPacket) event.getPacket(), player);
						}

						if(event.getPacket() instanceof ClientPluginMessagePacket) {
							PacketManager.Handlers.handleClientPluginMessagePacket((ClientPluginMessagePacket) event.getPacket(), player);
						}

						



						if(event.getPacket() instanceof ClientChatPacket) {
							ClientChatPacket packet = event.getPacket();

							if(packet.getMessage().charAt(0) == '/') {
								PacketManager.Handlers.handleClientChatPacketCommand(packet, player);
							} 
							else {
								PacketManager.Handlers.handleClientChatPacketChat(packet, player);
							}
							
						}

						if(event.getPacket() instanceof ClientTabCompletePacket) {
							PacketManager.Handlers.handelClientTabCompletePacket((ClientTabCompletePacket) event.getPacket(), player);
						}
					}
				});
			}

			@Override
			public void sessionRemoved(SessionRemovedEvent event) {
				MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
				if(protocol.getSubProtocol() == SubProtocol.GAME) {
					PacketManager.Handlers.handleClientDisconnect(getPlayer(event.getSession()));
				}
			}
		});

		commands.add(new CommandTest());
		commands.add(new CommandGameMode());
		commands.add(new CommandEffect());
		commands.add(new CommandSummon());
		commands.add(new CommandPlaySound());
		commands.add(new CommandTeleport());
		commands.add(new CommandTime());

		commands.add(new CommandHelp(commands));

		server.bind();
		Log.info("Server running: " + HOST + ":" + PORT);

		while(server.isListening()) {
			/*Do Nothing*/
			Thread.sleep(1);
		}

		Log.dbg("End");
	}


	//Utils

	public int getFreeEntityId() {
		return entities.size()+1;
	}

	public List<EntityPlayer> getOnlinePlayers(){
		List<EntityPlayer> players = new ArrayList<EntityPlayer>();
		for(Entity entity : entities.values()) {
			if(entity instanceof EntityPlayer) {
				players.add((EntityPlayer)entity);
			}
		}
		return players;
	}

	public EntityPlayer getPlayer(Session session) {
		for(EntityPlayer player : getOnlinePlayers()) {
			if(player.getSession() == session) {
				return player;
			}
		}
		return null;
	}
	
	public List<Command> getCommands() {
		return commands;
	}
	
	public HashMap<Integer, Entity> getEntities() {
		return entities;
	}

}
