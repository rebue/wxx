package rebue.wxx.svr.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import rebue.sbs.feign.FeignConfig;

@FeignClient(name = "wxx-svr", configuration = FeignConfig.class)
public interface WxxRequestSvc {
    /**
     * 获取AccessToken
     */
    @GetMapping("/wxx/request/accesstoken")
    Map<String, Object> getAccessToken();

    /**
     * 更新微信公众号菜单
     */
    @PutMapping("/wxx/request/menu")
    String updateMenu();
}
