package rebue.wxx.svc.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import rebue.robotech.svc.impl.BaseSvcImpl;
import rebue.wxx.dao.WxxAppDao;
import rebue.wxx.jo.WxxAppJo;
import rebue.wxx.mapper.WxxAppMapper;
import rebue.wxx.mo.WxxAppMo;
import rebue.wxx.redis.svc.WxxAppRedisSvc;
import rebue.wxx.svc.WxxAppSvc;

/**
 * App信息
 *
 * 在单独使用不带任何参数的 @Transactional 注释时，
 * propagation(传播模式)=REQUIRED，readOnly=false，
 * isolation(事务隔离级别)=READ_COMMITTED，
 * 而且事务不会针对受控异常（checked exception）回滚。
 *
 * 注意：
 * 一般是查询的数据库操作，默认设置readOnly=true, propagation=Propagation.SUPPORTS
 * 而涉及到增删改的数据库操作的方法，要设置 readOnly=false, propagation=Propagation.REQUIRED
 *
 * @mbg.generated 自动生成的注释，如需修改本注释，请删除本行
 */
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
@Slf4j
public class WxxAppSvcImpl extends BaseSvcImpl<java.lang.String, WxxAppJo, WxxAppDao, WxxAppMo, WxxAppMapper> implements WxxAppSvc {

    /**
     * @mbg.generated 自动生成，如需修改，请删除本行
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public int add(final WxxAppMo mo) {
        log.info("wxxAppSvc.add: 添加App信息 mo-", mo);
        // 如果id为空那么自动生成分布式id
        if (mo.getId() == null || mo.getId().trim().isEmpty()) {
            mo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        return super.add(mo);
    }

    @Resource
    private WxxAppRedisSvc appRedisSvc;

    /**
     * 获取微信的App缓存信息
     * 
     * @param id
     *            微信的appid
     */
    @Override
    public WxxAppJo getJoById(final String id) {
        WxxAppJo appJo = appRedisSvc.get(id);
        if (appJo == null) {
            appJo = super.getJoById(id);
        }
        if (appJo == null) {
            throw new RuntimeException("获取不到App信息: " + id);
        }
        appRedisSvc.set(appJo);
        return appJo;
    }

    /**
     * 获取微信的所有AppId的信息
     */
    @Override
    public List<String> listAppIds() {
        final List<String> appIds = new LinkedList<>();
        List<WxxAppJo> appJos = appRedisSvc.listAll();
        if (appJos == null || appJos.isEmpty()) {
            appJos = super.listJoAll();
            if (appJos != null && !appJos.isEmpty()) {
                appJos = new LinkedList<>();
                for (final WxxAppJo appJo : appJos) {
                    appRedisSvc.set(appJo);
                    appIds.add(appJo.getId());
                }
            }
        } else {
            for (final WxxAppJo appJo : appJos) {
                appIds.add(appJo.getId());
            }
        }
        return appIds;
    }

}
