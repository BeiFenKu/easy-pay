package com.niezhiliang.simple.pay.strategy;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.niezhiliang.simple.pay.config.AlipayConfig;
import com.niezhiliang.simple.pay.dto.AlipayRefundDTO;
import com.niezhiliang.simple.pay.utils.JsonUtils;
import com.niezhiliang.simple.pay.vos.RefundVO;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2019/4/27 上午11:34
 * 退款策略
 */
public class AliPayRefundStrategy implements PayStrategy<RefundVO,AlipayRefundDTO> {

    @Override
    public RefundVO operate(AlipayRefundDTO alipayRefundDTO) {
        AlipayClient alipayClient = AlipayConfig.getAlipayClient();
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(JsonUtils.jsonFormat(alipayRefundDTO));
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        JSONObject refundResponse = JSON.parseObject(response.getBody());
        RefundVO refundVO = refundResponse.getObject("alipay_trade_refund_response",RefundVO.class);
        return refundVO;
    }
}