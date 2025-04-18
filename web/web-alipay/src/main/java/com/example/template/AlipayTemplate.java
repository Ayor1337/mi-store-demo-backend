package com.example.template;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.example.entity.entity.PayOrder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Data
@Component
public class AlipayTemplate {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    @Value("${alipay.appId}")
    public String appId;

    // 应用私钥，就是工具生成的应用私钥
    @Value("${alipay.merchantPrivateKey}")
    public String merchantPrivateKey;
    // 支付宝公钥,对应APPID下的支付宝公钥。
    @Value("${alipay.alipayPublicKey}")
    public String alipayPublicKey;

    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    @Value("${alipay.notifyUrl}")
    public String notifyUrl;
    //同步通知，支付成功，一般跳转到成功页
    @Value("${alipay.returnUrl}")
    public String returnUrl;

    // 签名方式
    @Value("${alipay.signType}")
    private String signType;

    // 字符编码格式
    @Value("${alipay.charset}")
    private String charset;

    //订单超时时间
    private String timeout = "5m";
    // 支付宝网关；https://openapi-sandbox.dl.alipaydev.com/gateway.do
    @Value("${alipay.gatewayUrl}")
    public String gatewayUrl;

    public String pay(PayOrder order) throws AlipayApiException {
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new
                DefaultAlipayClient(gatewayUrl, appId, merchantPrivateKey,
                "json", charset, alipayPublicKey, signType);

        //2、创建一个支付请求，并设置请求参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        model.setOutTradeNo(order.getOrderId());

        model.setTotalAmount(order.getPrice());

        model.setSubject(order.getSubject());

        model.setBody("购买商品");

        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        model.setTimeoutExpress(timeout);

        request.setBizModel(model);
        request.setReturnUrl(returnUrl);
        request.setNotifyUrl(notifyUrl);

        return alipayClient.pageExecute(request).getBody();
    }

}
