package rebue.wxx.svc;

import java.util.List;

import rebue.robotech.svc.BaseSvc;
import rebue.wxx.jo.WxxAppJo;
import rebue.wxx.mo.WxxAppMo;

/**
 * App信息
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
public interface WxxAppSvc extends BaseSvc<java.lang.String, WxxAppMo, WxxAppJo> {
    /**
     * 获取微信的App缓存信息
     * 
     * @param id
     *            微信的appid
     */
    @Override
    WxxAppJo getJoById(String id);

    /**
     * 获取微信的所有AppId的信息
     */
    List<String> listAppIds();

}
