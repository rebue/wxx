package rebue.wxx.wxpay.svc.impl;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;

public class WxPayDomainImp implements IWXPayDomain{

	@Override
	public void report(String domain, long elapsedTimeMillis, Exception ex) {
		
	}

	@Override
	public DomainInfo getDomain(WXPayConfig config) {
		return new DomainInfo(WXPayConstants.DOMAIN_API,true);
	}

}
