import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Config {
    public static void generateConfigFromInfos(RecordInfo recordInfo, String zoneID, String lastIPRecorded){
        ConfigPalette configFile = new ConfigPalette();
        configFile.recordPalettes = new HashSet<>();
        if(!Config.existConfig()){
            for (Result result : recordInfo.result) {
                RecordPalette recordPalette = new RecordPalette();
                recordPalette.name = result.name;
                recordPalette.id = result.id;
                recordPalette.content = result.content;
                recordPalette.lastModified = result.modified_on;
                configFile.recordPalettes.add(recordPalette);
            }
        }else
        {
            HashSet<String> markedControlling = new HashSet<>();

            Gson gson = new Gson();
            String jsonString = null;
            try {
                jsonString = new String(Files.readAllBytes(Path.of(Constant.configPath)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ConfigPalette config = gson.fromJson(jsonString, ConfigPalette.class);
            for (RecordPalette recordPalette : config.recordPalettes) {
                if(recordPalette.isControlling){
                    markedControlling.add(recordPalette.id);
                }
            }

            for (Result result : recordInfo.result) {
                RecordPalette recordPalette = new RecordPalette();
                recordPalette.name = result.name;
                recordPalette.id = result.id;
                recordPalette.content = result.content;
                recordPalette.lastModified = result.modified_on;
                recordPalette.isControlling = markedControlling.contains(result.id);
                configFile.recordPalettes.add(recordPalette);
            }
        }

        configFile.lastIPRecorded = lastIPRecorded;
        configFile.zone_id = zoneID;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(configFile);
        try (java.io.FileWriter file = new java.io.FileWriter(Constant.configPath)) {
            file.write(jsonString);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean existConfig(){
        return Files.exists(Path.of(Constant.configPath));
    }
}

class RecordPalette {
    public String name;
    public String id;
    public String content;
    public Date lastModified;
    public boolean isControlling;
}

class ConfigPalette{
    public HashSet<RecordPalette> recordPalettes;
    public String zone_id;
    public String lastIPRecorded;
    public String IPOnDemandNote = "IP Ondemand means ONLY when external IP has changed, the DNS record will be updated.";
    public boolean enableIPOnDemand = false;
}