package rebue.wxx;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringCloudApplication
@EnableScheduling
@EnableFeignClients(basePackages = { "rebue.wxx.svr.**.feign" })
public class WxxAccessTokenServerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(WxxAccessTokenServerApplication.class, args);
    }

}
