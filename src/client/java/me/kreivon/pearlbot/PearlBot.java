package me.kreivon.pearlbot;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PearlBot implements ModInitializer {

	public static MinecraftClient client;
	public static Path gameDir;

	public static final Text PREFIX = Text.literal("")
			.append(Text.literal("[").formatted(Formatting.DARK_GRAY))
			.append(Text.literal("PearlBot").formatted(Formatting.DARK_PURPLE))
			.append(Text.literal("]").formatted(Formatting.DARK_GRAY));

	@Override
	public void onInitialize() {
		client = MinecraftClient.getInstance();
		gameDir = Paths.get(client.runDirectory.getPath());

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			// pearl -- PearlBot
			dispatcher.register(literal("pearl").executes(context -> {
				error(Text.translatable("command.pearlbot.incomplete"));
				return 0;
			}).then(argument("chamber", StringArgumentType.word()).executes(context -> {
				error(Text.translatable("command.pearlbot.incomplete"));
				return 0;
			}).then(argument("pearl", StringArgumentType.word()).executes(context -> {
				String chamberId = StringArgumentType.getString(context, "chamber");
				String pearlId = StringArgumentType.getString(context, "pearl");
				File pearlBot = Paths.get(gameDir.toString(), "PearlBot/pearlbot.js").toFile();
				if (pearlBot.exists()) {
					Thread thread = new Thread(new PearlBotProcess(pearlBot, chamberId, pearlId));
					thread.start();
					return SINGLE_SUCCESS;
				} else {
					error(Text.translatable("command.pearlbot.notinstalled"));
					return 0;
				}
			}))));
		});
	}

	/**
	 * Displays info to the chat.
	 *
	 * @param message Text to display as the message
	 */
	public static void info(Text message) {
		if (client.player != null) {
			client.player.sendMessage(PREFIX.copy().append(" ").append(message).formatted(Formatting.RESET).formatted(Formatting.GRAY));
		}
	}

	/**
	 * Displays info to the chat.
	 *
	 * @param message Text to display as the message
	 */
	public static void error(Text message) {
		info(message.copy().formatted(Formatting.RED));
	}

}