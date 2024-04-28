import java.io.File;
import java.net.URISyntaxException;

public class Constant {
    public static String key = "";
    public static String cf_recordsListUrl = "https://api.cloudflare.com/client/v4/zones/%s/dns_records";
    public static String cf_patchDNSUrl = "https://api.cloudflare.com/client/v4/zones/%s/dns_records/%s";

    public static String ip_query = "http://ipv4.icanhazip.com/";

    static String mainPath = null;
    public static String configPath = GetProgramPath() + "/config/config.json";
    public static String logPath = GetProgramPath() + "/log/log.txt";
    public static String GetProgramPath(){
        if(mainPath != null){
            return mainPath;
        }
        mainPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        if(System.getProperty("os.name").contains("dows"))
        {
            mainPath = mainPath.substring(1,mainPath.length());
        }
        if(mainPath.contains(".jar"))
        {
            mainPath = mainPath.substring(0,mainPath.lastIndexOf("."));
            mainPath = mainPath.substring(0,mainPath.lastIndexOf("/"));
        }
        mainPath = mainPath.replace("target/classes/", "");
        
        return mainPath;
    }
}
