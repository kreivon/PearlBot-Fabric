package me.kreivon.pearlbot;

import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class PearlBotProcess implements Runnable {

    private final File PEARL_BOT;
    private final String CHAMBER_ID;
    private final String PEARL_ID;

    PearlBotProcess(File pearlbot, String chamberId, String pearlId) {
        PEARL_BOT = pearlbot;
        CHAMBER_ID = chamberId;
        PEARL_ID = pearlId;
    }

    @Override
    public void run() {
        try {
            // Start pearlbot process
            String command = "node pearlbot.js %s %s".formatted(CHAMBER_ID, PEARL_ID);
            ProcessBuilder pb = new ProcessBuilder(command.split(" "));
            pb.directory(PEARL_BOT.getParentFile());
            Process process = pb.start();
            PearlBot.info(Text.translatable("command.pearlbot.start"));

            // Send output to game chat
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                PearlBot.info(Text.of(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PearlBot.info(Text.translatable("command.pearlbot.stop"));
    }

}