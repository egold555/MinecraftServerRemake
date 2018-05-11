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
import org.golde.test.commands.CommandTest;
import org.golde.test.entities.Entity;
import org.golde.test.entities.EntityPlayer;
import org.golde.test.util.Location;
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
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;

public class SingleInstance {

	private final String HOST = "localhost";
	private final int PORT = 25560;

	private Server server;
	private List<Command> commands = new ArrayList<Command>();
	private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();


	public void run() throws Exception {
		log("Run");

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

				//get new id, and add them as a new entity to the server
				final int entityId = getFreeEntityId();
				final EntityPlayer player = new EntityPlayer(entityId, session);
				entities.put(entityId, player);

				//http://wiki.vg/Protocol_FAQ#What.27s_the_normal_login_sequence_for_a_client.3F
				session.send(new ServerJoinGamePacket(entityId, false, GameMode.CREATIVE, 0, Difficulty.PEACEFUL, 100, WorldType.DEFAULT, false));
				//session.send(new ServerPluginMessagePacket("MC|Brand", "vanilla".getBytes()));
				session.send(new ServerDifficultyPacket(Difficulty.PEACEFUL));
				session.send(new ServerSpawnPositionPacket(new Position(0, 0, 0)));
				//http://wiki.vg/Protocol#Entity_Properties
				session.send(new ServerPlayerAbilitiesPacket(true, true, true, true, 0.05F, 0.100000001490116f));
				session.send(new ServerPlayerPositionRotationPacket(5, 255, 5, 0, 0, 0, new PositionElement[0]));

				for(EntityPlayer other : getOnlinePlayers()) {
					other.getSession().send(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[] {new PlayerListEntry(player.getGameProfile(), GameMode.CREATIVE, 1, new TextMessage(player.getName()))}));
					other.sendChatMessage(new TextMessage(player.getName() + " joined the game.").setStyle(new MessageStyle().setColor(ChatColor.LIGHT_PURPLE)));
				}

				//Add every person to the game for the new person who just joined
				for(EntityPlayer other : getOnlinePlayers()) {
					if(other != player) {
						player.getSession().send(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[] {new PlayerListEntry(other.getGameProfile(), GameMode.CREATIVE, 1, new TextMessage(other.getName()))}));
					}
				}
				
				log(player.getName() + " joined the game");

				//Fooling around
				session.send(new ServerBossBarPacket(UUID.randomUUID(), BossBarAction.ADD, new TextMessage("Testing123"), 0.4f, BossBarColor.YELLOW, BossBarDivision.NOTCHES_10, false, false));
				//session.send(new ServerUpdateTimePacket(0, 16000));


				Chunk[] chunks = new Chunk[16];

				for(int i = 0; i < 16; i++) {
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

						//Log except spammy packets
						if(!(event.getPacket() instanceof ClientKeepAlivePacket) && 
								!(event.getPacket() instanceof ClientPlayerPositionPacket) &&
								!(event.getPacket() instanceof ClientPlayerRotationPacket) &&
								!(event.getPacket() instanceof ClientPlayerPositionRotationPacket)) {

							log("Recieved Packet: " + ToStringBuilder.reflectionToString(event.getPacket(), ToStringStyle.SHORT_PREFIX_STYLE));
						}
						
						if(event.getPacket() instanceof ClientPlayerMovementPacket) {
							ClientPlayerMovementPacket packet = event.getPacket();
							EntityPlayer player = getPlayer(event.getSession());
							player.setLoccation(new Location(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch()));
						}
						
						

						if(event.getPacket() instanceof ClientChatPacket) {
							ClientChatPacket packet = event.getPacket();

							if(packet.getMessage().charAt(0) == '/') {
								for(Command cmd : commands) {
									String comm = packet.getMessage().substring(1, packet.getMessage().length());
									String[] split = StringUtils.splitBySpace(comm);
									if(cmd.getName().equalsIgnoreCase(split[0])) {
										String[] args = StringUtils.nudgeArrayDownByXRemovingFirstToLast(split, 1);
										if(args.length < cmd.getArgs().length) {
											cmd.notEnoughArgs(event.getSession());
											return;
										}
										try {
											cmd.execute(event.getSession(), args);
											return;
										} catch (Exception e) {
											event.getSession().send(new ServerChatPacket(new TextMessage(ExceptionUtils.getMessage(e))));
											cmd.notEnoughArgs(event.getSession());
											e.printStackTrace();
											return;
										}


									}
								}

								event.getSession().send(new ServerChatPacket(new TextMessage("Unknown command!")));
								return;
							}

							if(packet.getMessage().equalsIgnoreCase("test")) {
								event.getSession().send(new ServerTitlePacket("Title", false));
								event.getSession().send(new ServerTitlePacket("Sub", true));
								event.getSession().send(new ServerChatPacket(new TextMessage("Executed.")));
								event.getSession().send(new ServerSpawnMobPacket(1, UUID.randomUUID(), MobType.GUARDIAN, 5, 255, 5, 0, 0, 0, 0, 0, 0, new EntityMetadata[0]));
								event.getSession().send(new ServerPlayBuiltinSoundPacket(BuiltinSound.ENTITY_ENDERDRAGON_AMBIENT, SoundCategory.HOSTILE, 5, 255, 5, 1, 1));
								return;
							}

							GameProfile profile = event.getSession().getFlag(MinecraftConstants.PROFILE_KEY);
							System.out.println(profile.getName() + ": " + packet.getMessage());
							Message msg = new TextMessage("Hello, ").setStyle(new MessageStyle().setColor(ChatColor.GREEN));
							Message name = new TextMessage(profile.getName()).setStyle(new MessageStyle().setColor(ChatColor.AQUA).addFormat(ChatFormat.UNDERLINED));
							Message end = new TextMessage("!");
							msg.addExtra(name);
							msg.addExtra(end);
							event.getSession().send(new ServerChatPacket(msg));
						}

						if(event.getPacket() instanceof ClientTabCompletePacket) {
							ClientTabCompletePacket packet = event.getPacket();
							String rawText = packet.getText();

							if(rawText.length() >= 1 && rawText.charAt(0) == '/' && !rawText.contains(" ")) {
								List<String> sortedCommands =new ArrayList<String>();
								for(int i=0; i < commands.size(); i++) {
									if(commands.get(i).getName().toLowerCase().startsWith(rawText.substring(1, rawText.length()).toLowerCase())) {
										sortedCommands.add("/" + commands.get(i).getName());
									}
								}
								event.getSession().send(new ServerTabCompletePacket(sortedCommands.toArray(new String[0])));
								return;
							}

							String[] split = StringUtils.splitBySpace(rawText.substring(1, packet.getText().length()));
							for(Command cmd : commands) {
								if(cmd.getName().equalsIgnoreCase(split[0])){
									String[] args = StringUtils.nudgeArrayDownByXRemovingFirstToLast(split, 1);
									int argumentIndex = args.length - 1;
									if (rawText.endsWith(" ")) {
										argumentIndex += 1;
									}

									String[] toSend = cmd.onTabComplete(argumentIndex);
									if(toSend != null) {
										if (rawText.endsWith(" ")) {
											event.getSession().send(new ServerTabCompletePacket(toSend));
										}
										else {
											String lastSplit = split[split.length -1];
											List<String> sortedArgs =new ArrayList<String>();
											for(int i=0; i < toSend.length; i++) {
												if(toSend[i].toLowerCase().startsWith(lastSplit.toLowerCase())) {
													sortedArgs.add(toSend[i]);
												}
											}
											event.getSession().send(new ServerTabCompletePacket(sortedArgs.toArray(new String[0])));
										}
										return;
									}
								}
							}
						}
					}
				});
			}

			@Override
			public void sessionRemoved(SessionRemovedEvent event) {
				MinecraftProtocol protocol = (MinecraftProtocol) event.getSession().getPacketProtocol();
				if(protocol.getSubProtocol() == SubProtocol.GAME) {

					EntityPlayer player = (EntityPlayer) entities.get(event.getSession());

					//remove player from tab list and from game with packets
					for(EntityPlayer other : getOnlinePlayers()) {
						other.getSession().send(new ServerPlayerListEntryPacket(PlayerListEntryAction.REMOVE_PLAYER, new PlayerListEntry[] {new PlayerListEntry(player.getGameProfile(), GameMode.CREATIVE, 1, new TextMessage(player.getName()))}));
						other.sendChatMessage(new TextMessage(player.getName() + " joined the game.").setStyle(new MessageStyle().setColor(ChatColor.LIGHT_PURPLE)));
						
					}

					log(player.getName() + " left the game");
					entities.remove(getPlayer(event.getSession()).getId());
				}
			}
		});

		commands.add(new CommandTest());
		commands.add(new CommandGameMode());
		commands.add(new CommandEffect());
		commands.add(new CommandSummon());
		commands.add(new CommandPlaySound());

		commands.add(new CommandHelp(commands));

		server.bind();
		log("Server running: " + HOST + ":" + PORT);

		while(server.isListening()) {
			/*Do Nothing*/
			Thread.sleep(1);
		}

		log("End");
	}


	//Utils

	private void log(String msg) {
		System.out.println(msg);
	}

	private int getFreeEntityId() {
		return entities.size()+1;
	}

	private List<EntityPlayer> getOnlinePlayers(){
		List<EntityPlayer> players = new ArrayList<EntityPlayer>();
		for(Entity entity : entities.values()) {
			if(entity instanceof EntityPlayer) {
				players.add((EntityPlayer)entity);
			}
		}
		return players;
	}
	
	private EntityPlayer getPlayer(Session session) {
		for(EntityPlayer player : getOnlinePlayers()) {
			if(player.getSession() == session) {
				return player;
			}
		}
		return null;
	}

}
