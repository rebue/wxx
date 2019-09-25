package rebue.wxx.svr.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rebue.sbs.feign.FeignConfig;

@FeignClient(name = "wxx-svr", configuration = FeignConfig.class, contextId = "wxx-svr-wxx-request")
public interface WxxRequestSvc {
    /**
     * 获取AccessToken
     */
    @GetMapping("/wxx/request/accesstoken")
    Map<String, Object> getAccessToken(@RequestParam("appId") String appId);

}
