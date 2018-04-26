package rebue.wxx.access.token.svr;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients(basePackages = { "rebue.wxx.svr.**.feign" })
public class WxxAccessTokenServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxxAccessTokenServerApplication.class, args);
    }

}
