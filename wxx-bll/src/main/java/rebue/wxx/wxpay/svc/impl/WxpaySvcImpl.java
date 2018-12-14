package rebue.wxx.wxpay.svc.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayRequest;

import rebue.sbs.redis.RedisClient;
import rebue.sbs.redis.RedisSetException;
import rebue.wheel.HttpClientUtils;
import rebue.wheel.MapUtils;
import rebue.wheel.MoneyUtils;
import rebue.wheel.OkhttpUtils;
import rebue.wheel.RandomEx;
import rebue.wheel.XmlUtils;
import rebue.wheel.turing.SignUtils;
import rebue.wxx.wxpay.co.WxpayRedisCo;
import rebue.wxx.wxpay.dic.WxRefundResultDic;
import rebue.wxx.wxpay.dic.WxpayPrepayResultDic;
import rebue.wxx.wxpay.msg.WxpayPayDoneMsg;
import rebue.wxx.wxpay.pub.WxpayPayDonePub;
import rebue.wxx.wxpay.ro.WxRefundRo;
import rebue.wxx.wxpay.ro.WxpayOrderQueryRo;
import rebue.wxx.wxpay.ro.WxpayPrepayRo;
import rebue.wxx.wxpay.svc.WxpaySvc;
import rebue.wxx.wxpay.to.WxRefundTo;
import rebue.wxx.wxpay.to.WxpayPrepayTo;

@Service
public class WxpaySvcImpl implements WxpaySvc, ApplicationListener<ApplicationStartedEvent> {
	private final static Logger _log = LoggerFactory.getLogger(WxpaySvcImpl.class);

	/**
	 * 启动标志，防止多次启动
	 */
	private boolean bStartedFlag = false;

	/**
	 * 获取签名密钥的URL(沙箱测试)
	 */
	private final static String GET_SIGN_KEY_URL_SANDBOX = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";
	/**
	 * 预支付的URL
	 */
	private final static String PREPAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 预支付的URL(沙箱测试)
	 */
	private final static String PREPAY_URL_SANDBOX = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
	/**
	 * 查询订单的URL
	 */
	private final static String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	/**
	 * 查询订单的URL(沙箱测试)
	 */
	private final static String QUERY_ORDER_URL_SANDBOX = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";
	/**
	 * 微信退款URL
	 */
	private final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	/**
	 * 公众账号ID
	 */
	@Value("${wxx.app.id}")
	private String wxAppId;
	/**
	 * 商户号ID
	 */
	@Value("${wxx.wxpay.mchid}")
	private String wxpayMchId;

	/**
	 * 微信支付是否沙箱测试(默认为false)
	 */
	@Value("${wxx.wxpay.test:false}")
	private Boolean wxpayTest;
	/**
	 * 微信支付是否修补数据(默认为false，如果为true，则不校验orderId含金额是否存在缓存)
	 */
	@Value("${wxx.wxpay.repair:false}")
	private Boolean wxpayRepair;
	/**
	 * 微信支付-签名密钥 签名用的key，在商户平台设置（微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
	 */
	@Value("${wxx.wxpay.signKey}")
	private String wxpaySignKey;
	/**
	 * 微信支付-支付完成通知的URL
	 */
	@Value("${wxx.wxpay.payNotifyUrl}")
	private String wxpayPayNotifyUrl;

	/**
	 * 微信退款证书路径
	 */
	@Value("${wxx.wxpay.certUrl}")
	private String certUrl;

	@Resource
	private WxpayPayDonePub wxpayPayDonePub;
	@Resource
	private RedisClient redisClient;

	/**
	 * 沙箱测试的签名密钥
	 */
	private String _signkeyOfSandbox;

	// @PostConstruct
	// private void init() throws IOException, DocumentException {
	// _log.info("微信支付服务初始化");
	// if (wxpayTest)
	// getSignkeyOfSandbox();
	// }

	@Override
	public void onApplicationEvent(final ApplicationStartedEvent event) {
		// 防止多次启动
		if (bStartedFlag) {
			return;
		}
		bStartedFlag = true;

		_log.info("微信支付服务初始化");
		if (wxpayTest) {
			try {
				getSignkeyOfSandbox();
			} catch (final SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取沙箱测试的签名密钥
	 * 
	 * @throws SAXException
	 */
	private void getSignkeyOfSandbox() throws SAXException {
		_log.info("准备获取沙箱测试的签名密钥");
		// 获取沙箱测试的密钥
		final Map<String, Object> requestParams = new LinkedHashMap<>();
		requestParams.put("mch_id", wxpayMchId);
		requestParams.put("nonce_str", RandomEx.randomUUID());
		SignUtils.sign2(requestParams, wxpaySignKey);
		_log.info("发送获取沙箱测试的签名密钥的请求");
		Map<String, Object> respMap;
		try {
			// respMap = OkhttpUtils.postByXmlParams(GET_SIGN_KEY_URL_SANDBOX,
			// requestParams);
			respMap = HttpClientUtils.postByXmlParams(GET_SIGN_KEY_URL_SANDBOX, requestParams);
		} catch (IOException | DocumentException e) {
			throw new RuntimeException("获取沙箱测试的签名密钥失败", e);
		}
		_log.info("接收到响应: {}", MapUtils.map2Str(respMap));
		_signkeyOfSandbox = String.valueOf(respMap.get("sandbox_signkey"));
		_log.info("获取到沙箱测试的密钥: {}", _signkeyOfSandbox);
	}

	/**
	 * 微信支付-预支付
	 * 
	 * @throws SAXException
	 */
	@Override
	public WxpayPrepayRo prepay(final WxpayPrepayTo to) throws SAXException {
		_log.info("微信支付-预支付：{}", to);
		final WxpayPrepayRo ro = new WxpayPrepayRo();

		_log.info("检验参数是否正确");
		if (to.getUserId() == null || to.getTradeAmount() == null
				|| StringUtils.isAnyBlank(to.getWxId(), to.getTradeTitle(), to.getIp())) {
			final String msg = "没有填写必要的参数: " + to;
			_log.warn(msg);
			ro.setResult(WxpayPrepayResultDic.PARAM_ERROR);
			ro.setFailReason(msg);
			return ro;
		}

		_log.info("组织要传递的参数");
		final Map<String, Object> requestParams = new LinkedHashMap<>();
		requestParams.put("appid", wxAppId);
		requestParams.put("mch_id", wxpayMchId);
		requestParams.put("openid", to.getWxId());
		requestParams.put("nonce_str", RandomEx.randomUUID());
		requestParams.put("out_trade_no", to.getOrderId());
		requestParams.put("body", to.getTradeTitle());
		requestParams.put("detail", to.getTradeDetail());
		requestParams.put("total_fee", String.valueOf(MoneyUtils.yuan2fen(to.getTradeAmount()))); // 将“元”转为“分”
		requestParams.put("spbill_create_ip", to.getIp());
		requestParams.put("notify_url", wxpayPayNotifyUrl);
		requestParams.put("trade_type", "JSAPI");
		requestParams.put("attach", to.getUserId().toString());
		Map<String, Object> respMap = null;
		String signKey;
		String url;
		if (wxpayTest) {
			signKey = _signkeyOfSandbox;
			url = PREPAY_URL_SANDBOX;
		} else {
			signKey = wxpaySignKey;
			url = PREPAY_URL;
		}
		// 签名
		SignUtils.sign2(requestParams, signKey);
		try {
			_log.info("请求微信支付-预支付");
			respMap = OkhttpUtils.postByXmlParams(url, requestParams);
		} catch (IOException | DocumentException e) {
			final String msg = "请求微信支付-预支付出现异常";
			_log.error(msg, e);
			ro.setResult(WxpayPrepayResultDic.FAILT);
			ro.setFailReason(msg);
			return ro;
		}

		_log.info("微信支付-预支付响应内容: {}", MapUtils.map2Str(respMap));
		// 验证签名
		if (!SignUtils.verify2(respMap, signKey)) {
			final String msg = "请求微信支付-预支付返回的签名有问题，很可能是有人想模仿微信服务器返回“假”的支付成功的讯息！";
			_log.error(msg);
			ro.setResult(WxpayPrepayResultDic.FAILT);
			ro.setFailReason(msg);
			return ro;
		}
		// 返回成功，获取预支付ID
		if ("SUCCESS".equals(respMap.get("return_code")) && "SUCCESS".equals(respMap.get("result_code"))) {
			_log.info("检验传回来的参数是否和传上去的参数对应");
			if (!wxAppId.equals(respMap.get("appid")) || !wxpayMchId.equals(respMap.get("mch_id"))) {
				final String msg = "请求微信支付-预支付返回的值与传递的参数未对应，很可能是有人想模仿微信服务器返回“假”的支付成功的讯息！";
				_log.error(msg);
				ro.setResult(WxpayPrepayResultDic.FAILT);
				ro.setFailReason(msg);
				return ro;
			}

			// 取出预支付ID
			String prepayId = String.valueOf(respMap.get("prepay_id"));
			// 缓存销售订单ID/金额，以备接收到支付通知后校验通知是否正确（有效期2小时）
			try {
				redisClient.set(WxpayRedisCo.REDIS_KEY_WXPAY_ORDERID_PREFIX + to.getOrderId(),
						to.getTradeAmount().toString(), 2 * 60 * 60);
			} catch (final RedisSetException e) {
				final String msg = "微信支付-预支付获取到预支付ID后，使用redis缓存失败:" + prepayId;
				_log.error(msg, e);
				ro.setResult(WxpayPrepayResultDic.CACHE_FAIL);
				ro.setFailReason(msg);
				return ro;
			}

			// 如果是沙箱测试会返回签名密钥
			if (wxpayTest) {
				prepayId += ";" + _signkeyOfSandbox;
			}
			_log.info("微信支付-预支付成功: {}", prepayId);
			ro.setResult(WxpayPrepayResultDic.SUCCESS);
			ro.setPrepayId(prepayId);
			return ro;
		} else {
			_log.error("微信支付-预支付没有成功！");
			ro.setResult(WxpayPrepayResultDic.FAILT);
			ro.setFailReason(
					respMap.get("return_msg") + " " + respMap.get("err_code") + " " + respMap.get("err_code_des"));
			return ro;
		}
	}

	/**
	 * 微信支付-查询订单
	 * 
	 * @param orderId
	 *            销售订单ID
	 * @throws SAXException
	 */
	@Override
	public WxpayOrderQueryRo queryOrder(final String orderId) throws SAXException {
		_log.info("微信支付-查询订单：订单号-{}", orderId);
		final WxpayOrderQueryRo ro = new WxpayOrderQueryRo();

		_log.info("检验参数是否正确");
		if (StringUtils.isBlank(orderId)) {
			_log.warn("没有填写销售订单ID");
			return null;
		}

		_log.info("组织请求要传递的参数");
		final Map<String, Object> requestParams = new LinkedHashMap<>();
		requestParams.put("appid", wxAppId);
		requestParams.put("mch_id", wxpayMchId);
		requestParams.put("nonce_str", RandomEx.randomUUID());
		requestParams.put("out_trade_no", orderId);
		Map<String, Object> respMap = null;
		String signKey;
		String url;
		if (wxpayTest) {
			signKey = _signkeyOfSandbox;
			url = QUERY_ORDER_URL_SANDBOX;
		} else {
			signKey = wxpaySignKey;
			url = QUERY_ORDER_URL;
		}
		// 签名
		SignUtils.sign2(requestParams, signKey);
		try {
			_log.info("请求微信支付-查询订单");
			respMap = OkhttpUtils.postByXmlParams(url, requestParams);
		} catch (IOException | DocumentException e) {
			final String msg = "请求微信支付-查询订单出现异常";
			_log.error(msg, e);
			return null;
		}
		_log.info("微信支付-查询订单响应内容: {}", MapUtils.map2Str(respMap));
		// 验证签名
		if (!SignUtils.verify2(respMap, signKey)) {
			_log.error("请求微信支付-查询订单返回的签名有问题，很可能是有人想模仿微信服务器返回“假”的支付成功的讯息！");
			return null;
		}
		// 返回成功，获取查询的结果
		if ("SUCCESS".equals(respMap.get("return_code")) && "SUCCESS".equals(respMap.get("result_code"))
				&& "SUCCESS".equals(respMap.get("trade_state"))) {
			_log.info("检验传回来的参数是否和传上去的参数对应");
			if (!wxAppId.equals(respMap.get("appid")) || !wxpayMchId.equals(respMap.get("mch_id"))
					|| !orderId.equals(respMap.get("out_trade_no"))) {
				_log.error("请求微信支付-查询订单返回的值与传递的参数未对应，很可能是有人想模仿微信服务器返回“假”的支付成功的讯息！");
				return null;
			}

			// 取出响应的关键内容作为返回值
			ro.setTradeAmount(MoneyUtils.fen2yuan(String.valueOf(respMap.get("total_fee")))); // 订单金额(将“分”转为“元”)
			ro.setPayOrderId(String.valueOf(respMap.get("transaction_id"))); // 微信支付订单号
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				ro.setPayTime(sdf.parse(String.valueOf(respMap.get("time_end")))); // 支付完成时间
			} catch (final ParseException e) {
				_log.error("解析支付完成时间格式出错: {}", respMap.get("time_end"));
				return null;
			}
			return ro;
		} else {
			_log.error("微信支付-查询订单没有成功！");
			return null;
		}
	}

	/**
	 * 微信支付-处理支付完成的通知
	 */
	@Override
	public String handleNotify(final Map<String, Object> reqParams) {
		_log.info("处理微信支付-支付通知：{}", reqParams);
		final Map<String, Object> roMap = new LinkedHashMap<>();

		String signKey;
		if (wxpayTest) {
			signKey = _signkeyOfSandbox;
		} else {
			signKey = wxpaySignKey;
		}

		// 验证签名
		if (!SignUtils.verify2(reqParams, signKey)) {
			_log.error("请求微信支付-支付通知返回的签名有问题，很可能是有人想模仿微信服务器返回“假”的支付成功的讯息！");
			_log.info("返回微信签名失败");
			roMap.put("return_code", "FAIL");
			roMap.put("return_msg", "签名失败");
			return XmlUtils.mapToXml(roMap);
		}

		// 返回成功，获取查询的结果
		if ("SUCCESS".equals(reqParams.get("return_code")) && "SUCCESS".equals(reqParams.get("result_code"))) {
			_log.info("检验传回来的参数是否和传上去的参数对应");
			if (!wxAppId.equals(reqParams.get("appid")) || !wxpayMchId.equals(reqParams.get("mch_id"))) {
				_log.error("请求微信支付-支付通知返回的值与传递的参数未对应，很可能是有人想模仿微信服务器返回“假”的支付成功的讯息！" + reqParams.get("appid") + ","
						+ reqParams.get("mch_id"));
				_log.info("返回微信传递参数验证失败");
				roMap.put("return_code", "FAIL");
				roMap.put("return_msg", "传递参数验证失败");
				return XmlUtils.mapToXml(roMap);
			}

			_log.info("检查是否有此订单号及金额是否正确");
			// 金额
			final Double payAmount = MoneyUtils.fen2yuan(String.valueOf(reqParams.get("total_fee")));
			// 销售订单ID
			final String orderId = String.valueOf(reqParams.get("out_trade_no"));
			if (!wxpayRepair) {
				// 获取缓存中销售订单的金额
				final String sPayAmount = redisClient.get(WxpayRedisCo.REDIS_KEY_WXPAY_ORDERID_PREFIX + orderId);
				if (sPayAmount == null) {
					_log.error("在缓存中找不到此销售订单: {}", orderId);
					_log.info("返回微信找不到此销售订单");
					roMap.put("return_code", "FAIL");
					roMap.put("return_msg", "找不到此销售订单");
					return XmlUtils.mapToXml(roMap);
				} else if (Double.parseDouble(sPayAmount) != payAmount) {
					_log.error("此支付订单的金额与缓存中的不匹配: 预期{},实际{}", sPayAmount, payAmount);
					_log.info("返回微信此支付订单的金额与预支付请求的不一致");
					roMap.put("return_code", "FAIL");
					roMap.put("return_msg", "此支付订单的金额与预支付请求的不一致");
					return XmlUtils.mapToXml(roMap);
				}
			}

			_log.info("将微信支付完成的消息加入消息队列");
			// 取出响应的关键内容作为返回值
			final WxpayPayDoneMsg msg = new WxpayPayDoneMsg();
			msg.setUserId(Long.parseLong(String.valueOf(reqParams.get("attach")))); // 用户ID
			msg.setPayAccountId(String.valueOf(reqParams.get("openid"))); // 微信ID
			msg.setPayAmount(new BigDecimal(payAmount.toString())); // 订单金额(将“分”转为“元”)
			msg.setTradeId(String.valueOf(reqParams.get("transaction_id"))); // 微信支付订单号
			msg.setOrderId(String.valueOf(reqParams.get("out_trade_no"))); // 订单号
			final String sPayTime = String.valueOf(reqParams.get("time_end")); // 支付完成时间
			// 解析支付完成时间
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				msg.setPayTime(sdf.parse(sPayTime));
			} catch (final ParseException e) {
				_log.error("解析支付完成时间格式失败: {}", sPayTime);
				_log.info("返回解析支付完成时间格式失败");
				roMap.put("return_code", "FAIL");
				roMap.put("return_msg", "解析支付完成时间格式失败: " + sPayTime);
				return XmlUtils.mapToXml(roMap);
			}
			wxpayPayDonePub.send(msg);
		}

		_log.info("返回微信成功处理数据: {}", roMap);
		roMap.put("return_code", "SUCCESS");
		roMap.put("return_msg", "OK");
		return XmlUtils.mapToXml(roMap);
	}

	@Override
	public WxRefundRo wxRefund(WxRefundTo to) throws SAXException {
		_log.info("微信退款：{}", to);
		final WxRefundRo ro = new WxRefundRo();
		_log.info("检验参数是否正确");
		if ((to.getOrderPayId() == null && to.getTransactionId() == null) || to.getOrderAmount() == null
				|| to.getRefundAmount() == null || to.getRefundId() == null) {
			final String msg = "没有填写必要的参数: " + to;
			_log.warn(msg);
			ro.setResult(WxRefundResultDic.PARAM_ERROR);
			ro.setFailReason(msg);
			return ro;
		}
		_log.info("组织微信退款要传递的参数");
		final Map<String, Object> requestParams = new LinkedHashMap<>();
		requestParams.put("appid", wxAppId);
		requestParams.put("mch_id", wxpayMchId);
		requestParams.put("nonce_str", RandomEx.randomUUID());
		if (to.getTransactionId() != null) {
			requestParams.put("transaction_id", to.getTransactionId());
		}
		if (to.getOrderPayId() != null) {
			requestParams.put("out_trade_no", to.getOrderPayId());
		}
		requestParams.put("out_refund_no", to.getRefundId());
		requestParams.put("total_fee", String.valueOf(MoneyUtils.yuan2fen(to.getOrderAmount())));
		requestParams.put("refund_fee", String.valueOf(MoneyUtils.yuan2fen(to.getRefundAmount())));
		String refundData = XmlUtils.mapToXml(requestParams);
		String result = null;
		try {
			WxPayConfigEx config = new WxPayConfigEx();
			config.setWxAppId(wxAppId);
			config.setWxpayMchId(wxpayMchId);
			config.setWxpaySignKey(wxpaySignKey);
			config.setFileRoute(certUrl);
			_log.info("微信配置信息：" + config);
			String signKey;
			String url;
			if (wxpayTest) {
				signKey = _signkeyOfSandbox;
				url = WXPayConstants.SANDBOX_REFUND_URL_SUFFIX;
			} else {
				signKey = wxpaySignKey;
				url = WXPayConstants.REFUND_URL_SUFFIX;
			}
			_log.info("签名信息：" + signKey);
			// 签名
			SignUtils.sign2(requestParams, signKey);
			WXPayRequest wxPayRequest = new WXPayRequest(config);
			result = wxPayRequest.requestWithCert(url, RandomEx.randomUUID(), refundData, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> refundResultMap = new LinkedHashMap<>();
		try {
			refundResultMap = XmlUtils.xmlToMap(result);
			_log.info("微信退款返回值" + refundResultMap);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		WxRefundRo refundRo = new WxRefundRo();
		if ("SUCCESS".equals(refundResultMap.get("return_code"))) {
			_log.info("微信退款成功");
			refundRo.setResult(WxRefundResultDic.SUCCESS);
		} else if ("FAIL".equals(refundResultMap.get("return_code"))) {
			_log.info("微信退款失败");
			refundRo.setResult(WxRefundResultDic.FAILT);
		}
		return refundRo;
	}
}
