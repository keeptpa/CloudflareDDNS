import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class Logger {

    public static void log(String message){
        //process log
        message = String.format("[%s] %s", new Date().toString(), message);
        System.out.println(message);
        //add to bottom of log
        try {
            Files.write(new File(Constant.logPath).toPath(), (message + "\n").getBytes(), Files.exists(new File(Constant.logPath).toPath()) ?
                    StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean existLogFile(){
        return Files.exists(new File(Constant.logPath).toPath());
    }
}
