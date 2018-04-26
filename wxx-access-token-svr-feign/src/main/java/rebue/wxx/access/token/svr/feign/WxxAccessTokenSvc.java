package rebue.wxx.access.token.svr.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import rebue.sbs.feign.FeignConfig;

@FeignClient(name = "wxx-access-token-svr", configuration = FeignConfig.class)
public interface WxxAccessTokenSvc {
    /**
     * @return 获取access token
     */
    @GetMapping("/wxx/access/token")
    String getAccessToken();

    /**
     * @return 刷新access token
     */
    @PostMapping("/wxx/access/token")
    void refreshAccessToken();

}
