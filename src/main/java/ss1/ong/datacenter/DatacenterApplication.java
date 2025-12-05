package ss1.ong.datacenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import ss1.ong.datacenter.common.config.EnvConfig;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class DatacenterApplication {

	public static void main(String[] args) {
        EnvConfig loader = new EnvConfig();
		SpringApplication.run(DatacenterApplication.class, args);
	}

}
