package rebue.wxx.test;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import rebue.robotech.dic.ResultDic;
import rebue.robotech.ro.IdRo;
import rebue.robotech.ro.Ro;
import rebue.wheel.OkhttpUtils;
import rebue.wheel.RandomEx;
import rebue.wxx.mo.WxxMchMo;

/**
 * 商户信息，也就是微信支付账户信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@Slf4j
public class WxxMchTests {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private final String       hostUrl       = "http://127.0.0.1:20080";

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    private final ObjectMapper _objectMapper = new ObjectMapper();

    /**
     * 测试基本的增删改查
     *
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Test
    public void testCrud() throws IOException, ReflectiveOperationException {
        WxxMchMo mo = null;
        for (int i = 0; i < 20; i++) {
            mo = (WxxMchMo) RandomEx.randomPojo(WxxMchMo.class);
            mo.setId(null);
            log.info("添加商户信息，也就是微信支付账户信息的参数为：" + mo);
            final String addResult = OkhttpUtils.postByJsonParams(hostUrl + "/wxx/mch", mo);
            log.info("添加商户信息，也就是微信支付账户信息的返回值为：" + addResult);
            final IdRo idRo = _objectMapper.readValue(addResult, IdRo.class);
            log.info(idRo.toString());
            Assertions.assertEquals(ResultDic.SUCCESS, idRo.getResult());
            mo.setId(idRo.getId());
        }
        final String listResult = OkhttpUtils.get(hostUrl + "/wxx/mch?pageNum=1&pageSize=5");
        log.info("查询商户信息，也就是微信支付账户信息的返回值为：" + listResult);
        log.info("获取单个商户信息，也就是微信支付账户信息的参数为：" + mo.getId());
        final String getByIdResult = OkhttpUtils.get(hostUrl + "/wxx/mch/get-by-id?id=" + mo.getId());
        log.info("获取单个商户信息，也就是微信支付账户信息的返回值为：" + getByIdResult);
        log.info("修改商户信息，也就是微信支付账户信息的参数为：" + mo);
        final String modifyResult = OkhttpUtils.putByJsonParams(hostUrl + "/wxx/mch", mo);
        log.info("修改积分日志类型的返回值为：" + modifyResult);
        final Ro modifyRo = _objectMapper.readValue(modifyResult, Ro.class);
        log.info(modifyRo.toString());
        Assertions.assertEquals(ResultDic.SUCCESS, modifyRo.getResult());
        log.info("删除商户信息，也就是微信支付账户信息的参数为：" + mo.getId());
        final String deleteResult = OkhttpUtils.delete(hostUrl + "/wxx/mch?id=" + mo.getId());
        log.info("删除商户信息，也就是微信支付账户信息的返回值为：" + deleteResult);
        final Ro deleteRo = _objectMapper.readValue(deleteResult, Ro.class);
        log.info(deleteRo.toString());
        Assertions.assertEquals(ResultDic.SUCCESS, deleteRo.getResult());
    }
}
