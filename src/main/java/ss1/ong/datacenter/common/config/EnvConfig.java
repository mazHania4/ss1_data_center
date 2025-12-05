package ss1.ong.datacenter.common.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    static {
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(e ->
                System.setProperty(e.getKey(), e.getValue())
        );
    }
}
