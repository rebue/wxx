package rebue.wxx.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rebue.wxx.utils.example.aes.ByteGroup;
import rebue.wxx.utils.example.aes.PKCS7Encoder;

/**
 * 微信发送接收消息加密的相关应用
 */
public class WxxMsgCryptUtils {
    private final static Logger _log   = LoggerFactory.getLogger(WxxMsgCryptUtils.class);

    private static Base64       base64 = new Base64();

    /**
     * 对明文进行加密
     * 
     * @param text
     *            需要加密的明文
     * @param randomStr
     *            随机字符串
     * @param appId
     *            用来给解密后验证的appid是否正确
     * @param aesKey
     *            加密用的密钥
     * @return 加密后base64编码的字符串，如果
     */
    public static String encrypt(String text, String randomStr, String appId, String encodingAesKey) {
        _log.info("对明文进行加密: {}", text);

        byte[] aesKey = Base64.decodeBase64(encodingAesKey + "=");

        ByteGroup byteCollector = new ByteGroup();
        byte[] randomStrBytes = null;
        byte[] textBytes = null;
        byte[] networkBytesOrder = null;
        byte[] appidBytes = null;
        try {
            randomStrBytes = randomStr.getBytes("utf-8");
            textBytes = text.getBytes("utf-8");
            networkBytesOrder = getNetworkBytesOrder(textBytes.length);
            appidBytes = appId.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支付utf-8解码？？？，不可能的");
        }

        // randomStr + networkBytesOrder + text + appid
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);
        byteCollector.addBytes(appidBytes);

        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        byte[] unencrypted = byteCollector.toBytes();

        try {
            // 设置加密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            // 加密
            byte[] encrypted = cipher.doFinal(unencrypted);

            // 使用BASE64对加密后的字符串进行编码
            String base64Encrypted = base64.encodeToString(encrypted);

            return base64Encrypted;
        } catch (Exception e) {
            _log.error("加密错误", e);
            return null;
        }
    }

    /**
     * 对密文进行解密
     * 
     * @param encryptedText
     *            密文
     * @param appId
     *            用来验证解密后的appid是否正确
     * @param aesKey
     *            解密用的密钥
     * @return 解密的明文，如果解密失败，返回null
     */
    public static String decrypt(String encryptedText, String appId, String encodingAesKey) {
        _log.info("对密文进行解密: {}", encryptedText);
        byte[] aesKey = Base64.decodeBase64(encodingAesKey + "=");

        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            // 使用BASE64对密文进行解码
            byte[] encrypted = Base64.decodeBase64(encryptedText);

            // 解密
            original = cipher.doFinal(encrypted);
            _log.info("解密出明文: {}", new String(original, "utf-8"));
        } catch (Exception e) {
            _log.error("解密失败", e);
            return null;
        }

        String xmlContent, from_appid;
        try {
            // 去除补位字符
            byte[] bytes = PKCS7Encoder.decode(original);

            // 分离16位随机字符串,网络字节序和AppId
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

            int xmlLength = recoverNetworkBytesOrder(networkOrder);

            xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), "utf-8");
            from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), "utf-8");
        } catch (Exception e) {
            _log.error("解密失败：", e);
            return null;
        }

        if (!from_appid.equals(appId)) {
            _log.error("解密失败：错误的appId-" + appId);
            return null;
        }
        return xmlContent;
    }

    // 生成4个字节的网络字节序
    static byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    // 还原4个字节的网络字节序
    static int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }
}
