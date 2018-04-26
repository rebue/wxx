package rebue.wxx.utils;

/**
 * 微信支付签名的相关应用
 */
public class WxpaySignUtils {
//    private final static Logger _log = LoggerFactory.getLogger(WxpaySignUtils.class);

    /**
     * 微信支付签名
     * 在请求时，通过签名算法算出签名，并将其放入请求的Map中
     * 签名算法: 将所有参数排序生成新的Map->拼接成key1=value2&key2=value2....的字符串->md5hex->大写
     */
//    public static void sign(Map<String, Object> requestParams, String signKey) {
//        _log.info("微信支付签名：{} {}", requestParams, signKey);
//        // 排序生成新的Map
//        Map<String, Object> sortMap = new TreeMap<>(requestParams);
//        // 拼接成key1=value2&key2=value2....的字符串
//        StringBuilder sb = new StringBuilder();
//        for (Entry<String, Object> item : sortMap.entrySet()) {
//            if (StringUtils.isBlank(String.valueOf(item.getValue())))
//                continue;
//            if (sb.length() > 0)
//                sb.append("&");
//            sb.append(item.getKey() + "=" + item.getValue());
//        }
//        sb.append("&key=" + signKey);
//        // md5hex->大写->放入请求的Map中
//        requestParams.put("sign", DigestUtils.md5DigestAsHex(sb.toString().getBytes()).toUpperCase());
//    }

    /**
     * 验证微信支付的签名
     * 接收到的请求时，对请求的参数进行
     */
//    public static boolean verify(Map<String, Object> requestParams, String signKey) {
//        _log.info("微信支付通过请求的参数验证签名：{} {}", requestParams, signKey);
//        StringBuilder sb = new StringBuilder();
//        Map<String, Object> sortMap = new TreeMap<>(requestParams);
//
//        for (Entry<String, Object> item : sortMap.entrySet()) {
//            if (StringUtils.isBlank(String.valueOf(item.getValue())))
//                continue;
//            if ("sign".equalsIgnoreCase(item.getKey())) {
//                continue;
//            }
//            if (sb.length() > 0)
//                sb.append("&");
//            sb.append(item.getKey() + "=" + item.getValue());
//        }
//        sb.append("&key=" + signKey);
//        String sign = DigestUtils.md5DigestAsHex(sb.toString().getBytes()).toUpperCase();
//        _log.info("验证的签名为: {}", sign);
//        return sign.equalsIgnoreCase(String.valueOf(sortMap.get("sign")));
//    }
}
