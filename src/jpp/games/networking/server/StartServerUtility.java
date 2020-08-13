package jpp.games.networking.server;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Properties;
import java.util.Scanner;

public class StartServerUtility {

    public static ConfigurableApplicationContext startServer(Class<? extends Server> applicationClass, Integer port){
        SpringApplication server = new SpringApplication(applicationClass);
        Properties properties = new Properties();
        properties.setProperty("server.port", port.toString());
        //disable logs
        //properties.setProperty("spring.main.banner-mode", "off");
        //properties.setProperty("logging.pattern.console", "");

        server.setDefaultProperties(properties);
        return server.run();
    }

    public static void closeServer(ConfigurableApplicationContext serverContext){
        if(serverContext != null && serverContext.isRunning()) {
            serverContext.close();
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = startServer(Server_Impl.class, 61361);

        // wait for 'close' command
        Scanner scan = new Scanner(System.in);
        String line = "";
        while(!line.startsWith("close")) {
            line = scan.nextLine();
        }
        scan.close();

        closeServer(ctx);
    }
}
