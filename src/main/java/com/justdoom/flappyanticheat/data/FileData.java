package com.justdoom.flappyanticheat.data;

import com.justdoom.flappyanticheat.FlappyAnticheat;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.minestom.server.color.Color;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileData {

    public FileData(){

    }

    public void addToFile(String fileName, String message) {
        File log = new File(FlappyAnticheat.getInstance().getDataDirectory().toString(), fileName);
        try{
            if(!log.exists()){
                createFiles(fileName);
            }
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            out.append(PlainComponentSerializer.plain().serialize(LegacyComponentSerializer.legacySection().deserialize(message)));
            out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void createFiles(String name) {
        if (!new File(FlappyAnticheat.getInstance().getDataDirectory().toString(), name).exists()) {
            File todayFile = new File(FlappyAnticheat.getInstance().getDataDirectory().toString(), name);
            try {
                todayFile.createNewFile(); // Error
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
