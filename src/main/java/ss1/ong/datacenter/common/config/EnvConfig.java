package ss1.ong.datacenter.common.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    static {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            dotenv.entries().forEach(e ->
                    System.setProperty(e.getKey(), e.getValue())
            );

        } catch (Exception e) {
            System.out.println("No .env file found. Using system environment variables.");
        }
    }
}
