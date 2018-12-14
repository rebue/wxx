package rebue.wxx.wxpay.svc.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;

@Service
public class WxPayConfigEx extends WXPayConfig{
	
	private String wxAppId;
	
	private String wxpayMchId;
	
	private String wxpaySignKey;
	
	private String fileRoute;
	
	public void setWxAppId(String wxAppId) {
		this.wxAppId = wxAppId;
	}

	public void setWxpayMchId(String wxpayMchId) {
		this.wxpayMchId = wxpayMchId;
	}

	public void setWxpaySignKey(String wxpaySignKey) {
		this.wxpaySignKey = wxpaySignKey;
	}

	public void setFileRoute(String fileRoute) {
		this.fileRoute = fileRoute;
	}

	@Override
	public String getAppID() {
		return wxAppId;
	}

	@Override
	public String getMchID() {
		return wxpayMchId;
	}

	@Override
	public String getKey() {
		return wxpaySignKey;
	}
	
	@Override
	public InputStream getCertStream() {
		FileInputStream instream = null;
		try {
			 instream = new FileInputStream(new File(fileRoute));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return instream;
	}

	@Override
	public IWXPayDomain getWXPayDomain() {
		return new WxPayDomainImp();
	}

}
