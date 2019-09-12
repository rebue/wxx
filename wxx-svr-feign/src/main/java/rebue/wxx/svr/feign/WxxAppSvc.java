package rebue.wxx.svr.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import rebue.sbs.feign.FeignConfig;

@FeignClient(name = "wxx-svr", configuration = FeignConfig.class, contextId = "wxx-svr-wxx-app")
public interface WxxAppSvc {
    /**
     * 获取微信的所有AppId的信息
     */
    @GetMapping("/wxx/app/list-appids")
    List<String> listAppIds();

}
