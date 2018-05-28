package rebue.wxx;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

// 排除json的转换器，否则微信要求返回纯文本就会返回json字符串而出错
//@SpringBootApplication(exclude = { rebue.sbs.smx.JacksonConfig.class })
@SpringCloudApplication()
@EnableFeignClients(basePackages = { "rebue.wxx.access.token.svr.feign" })
public class WxxSvrApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxxSvrApplication.class, args);
    }

}
