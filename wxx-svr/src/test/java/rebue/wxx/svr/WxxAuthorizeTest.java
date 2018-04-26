package rebue.wxx.svr;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;

import rebue.wheel.OkhttpUtils;
import rebue.wheel.RandomEx;
import rebue.wxx.vo.WxAuthorizeVo;

public class WxxAuthorizeTest {
    private static final Logger _log    = LoggerFactory.getLogger(WxxAuthorizeTest.class);

    private String              wxToken = "eb6f6aa0b4cf51eb9c48f87305b69890";

    private String              hostUrl = "http://nnzbz.free.ngrok.cc/";

    /**
     * 测试提供给微信验证本服务器身份的接口-测试参数为空的情况
     */
    @Test
    public void test01() throws IOException {
        String contentPath = "/wxx/response";
        Map<String, Object> requestParams = null;
        try {
            requestParams = new LinkedHashMap<String, Object>();
            OkhttpUtils.get(hostUrl + contentPath, requestParams);
            Assert.fail("没有传参数，服务器应该返回500错误");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(500, e.getStatusCode().value());
        }
        try {
            requestParams = new LinkedHashMap<String, Object>();
            requestParams.put("signature", " ");
            requestParams.put("timestamp", " ");
            requestParams.put("nonce", " ");
            requestParams.put("echostr", " ");
            OkhttpUtils.get(hostUrl + contentPath, requestParams);
            Assert.fail("参数有空字符串，服务器应该返回500错误");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(500, e.getStatusCode().value());
        }
        try {
            requestParams = new LinkedHashMap<String, Object>();
            requestParams.put("signature", " ");
            requestParams.put("timestamp", " bbb ");
            requestParams.put("nonce", " ccc ");
            requestParams.put("echostr", " ddd ");
            OkhttpUtils.get(hostUrl + contentPath, requestParams);
            Assert.fail("参数有空字符串，服务器应该返回500错误");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(500, e.getStatusCode().value());
        }
        try {
            requestParams = new LinkedHashMap<String, Object>();
            requestParams.put("signature", " aaa ");
            requestParams.put("timestamp", "  ");
            requestParams.put("nonce", " ccc ");
            requestParams.put("echostr", " ddd ");
            OkhttpUtils.get(hostUrl + contentPath, requestParams);
            Assert.fail("参数有空字符串，服务器应该返回500错误");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(500, e.getStatusCode().value());
        }
        try {
            requestParams = new LinkedHashMap<String, Object>();
            requestParams.put("signature", " aaa ");
            requestParams.put("timestamp", " bbb ");
            requestParams.put("nonce", "  ");
            requestParams.put("echostr", " ddd ");
            OkhttpUtils.get(hostUrl + contentPath, requestParams);
            Assert.fail("参数有空字符串，服务器应该返回500错误");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(500, e.getStatusCode().value());
        }
        try {
            requestParams = new LinkedHashMap<String, Object>();
            requestParams.put("signature", " aaa ");
            requestParams.put("timestamp", " bbb ");
            requestParams.put("nonce", " ccc ");
            requestParams.put("echostr", "  ");
            OkhttpUtils.get(hostUrl + contentPath, requestParams);
            Assert.fail("参数有空字符串，服务器应该返回500错误");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(500, e.getStatusCode().value());
        }
        // requestParam.put(key, value);
    }

    /**
     * 测试提供给微信验证本服务器身份的接口-测试非正常的数据
     * 
     * @throws IOException
     */
    @Test
    public void test02() throws IOException {
        String contentPath = "/wxx/response";
        Map<String, Object> requestParams = null;
        try {
            requestParams = new LinkedHashMap<String, Object>();
            requestParams.put("signature", "aaa");
            requestParams.put("timestamp", "bbb");
            requestParams.put("nonce", "ccc");
            requestParams.put("echostr", "ddd");
            OkhttpUtils.get(hostUrl + contentPath, requestParams);
            Assert.fail("参数timestamp为字符串，服务器应该返回500错误");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(500, e.getStatusCode().value());
        }
    }

    /**
     * 测试提供给微信验证本服务器身份的接口-测试多条正常的数据
     */
    @Test
    public void test03() throws IOException {
        String contentPath = "/wxx/response";
        Map<String, Object> requestParams = null;
        for (int i = 0; i < 100; i++) {
            WxAuthorizeVo initVo = genInitVo();
            requestParams = new LinkedHashMap<String, Object>();
            requestParams.put("signature", initVo.getSignature());
            requestParams.put("timestamp", initVo.getTimestamp());
            requestParams.put("nonce", initVo.getNonce());
            requestParams.put("echostr", initVo.getEchostr());
            _log.info("requestParams: " + requestParams);
            Assert.assertEquals(initVo.getEchostr(), OkhttpUtils.get(hostUrl + contentPath, requestParams));
        }
    }

    /**
     * 生成校验微信确认本服务器身份接口的参数
     */
    private WxAuthorizeVo genInitVo() {
        WxAuthorizeVo initVo = new WxAuthorizeVo();
        initVo.setTimestamp(Long.toString(System.currentTimeMillis()));
        initVo.setNonce(RandomEx.random1(32));
        initVo.setEchostr(RandomEx.random1(32));
        String[] array = { wxToken, initVo.getTimestamp(), initVo.getNonce() };
        Arrays.sort(array);
        initVo.setSignature(DigestUtils.sha1Hex(array[0] + array[1] + array[2]));
        return initVo;
    }

}
