import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<String> dnsNameNeedsControl = new ArrayList<>();
    public static void main(String[] args){
        initFolder();
        Logger.log("<<Starting...>>");
        Constant.key = args[0];
        //check config
        String inputZoneID = null;
        String lastIP = null;
        boolean iPOnDemand = false;

        if(Config.existConfig()){
            //read config
            Logger.log("Found config, reading...");
            try {
                String configJson = new String(Files.readAllBytes(Path.of(Constant.configPath)));
                ConfigPalette config = new Gson().fromJson(configJson, ConfigPalette.class);
                for (RecordPalette recordPalette : config.recordPalettes) {
                    if(recordPalette.isControlling){
                        dnsNameNeedsControl.add(recordPalette.id);
                    }
                }
                inputZoneID = config.zone_id;
                lastIP = config.lastIPRecorded;
                iPOnDemand = config.enableIPOnDemand;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            Logger.log("Config not found, generating...");
            System.out.println("Input Zone ID: ");
            Scanner sc = new Scanner(System.in);
            inputZoneID = sc.nextLine();
        }
        Logger.log("Getting external IP...");
        String externalIP = CFHttpClient.requestExternalIP();

        if(iPOnDemand){
            if(lastIP != null && externalIP.equals(lastIP)){
                Logger.log("The external IP has not changed. Exiting...");
                return;
            }else if(lastIP != null){
                Logger.log(String.format("The external IP has changed from %s to %s", lastIP, externalIP));
            }
        }

        Logger.log("Query all DNS records...");
        RecordInfo allInfo = CFHttpClient.requestDNSList(inputZoneID);
        Config.generateConfigFromInfos(allInfo, inputZoneID, externalIP);

        //Patch up changed DNS record
        if(!dnsNameNeedsControl.isEmpty()){
            boolean isChanged = false;
            //for (String id : dnsNameNeedsControl) {
                for (Result result : allInfo.result) {
                    String id = result.id;
                    if(dnsNameNeedsControl.contains(id) && !result.content.equals(externalIP)){
                        Logger.log(String.format("Update %s 's record to %s", id, externalIP));
                        CFHttpClient.requestPatchADNS(inputZoneID, id, externalIP);
                        isChanged = true;
                    }
                }
            //}
            if(!isChanged){
                Logger.log("No record need to update");
            }
        }else{
            Logger.log("You have not marked any of the records that needs controlled");
        }
        Logger.log("<<Finished>>");
        return;
    }

    public static void initFolder(){
        //setting folder
        File folder = new File(Constant.GetProgramPath() + "/config");
        if(!folder.exists()){
            folder.mkdirs();
        }
        //log folder
        File logFolder = new File(Constant.GetProgramPath() + "/log");
        if(!logFolder.exists()){
            logFolder.mkdirs();
        }
    }
}