package com.niezhiliang.simple.pay.strategy;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.niezhiliang.simple.pay.config.AlipayConfig;
import com.niezhiliang.simple.pay.utils.JsonUtils;
import com.niezhiliang.simple.pay.vos.AliPayCallBackVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2019/4/27 下午2:25
 * 支付回调策略
 */
public class AliPayCallBackStrategy implements PayStrategy<AliPayCallBackVO,HttpServletRequest>{

    /**
     * 支付成功返回的状态值
     */
    private static final String SUCCESS_PAY_STATUS = "TRADE_SUCCESS";

    /**
     * 支付成功返给支付宝的值
     */
    private static final String SUCCESS = "SUCCESS";

    /**
     * 支付宝回调
     * @param request
     * @return
     */
    @Override
    public AliPayCallBackVO operate(HttpServletRequest request) {
        Map<String, String> map = JsonUtils.requestToMap(request);
        AliPayCallBackVO aliPayCallBackVO = null;
        try {
            //验签
            boolean flag = AlipaySignature.rsaCheckV1(map,AlipayConfig.getInstance().getPublicKey() ,AlipayConfig.getInstance().getCharset(),
                    AlipayConfig.getInstance().getSignType());
            String json = JSON.toJSONString(map);
            System.out.println(json);
            if (!flag) {
               throw new RuntimeException("Alipay payment callback sign check failed");
            }
            aliPayCallBackVO = (AliPayCallBackVO) JsonUtils.mapToObject(map,AliPayCallBackVO.class);
            //判断订单的支付结果
            if (SUCCESS_PAY_STATUS.equals(aliPayCallBackVO.getTrade_status())){
                aliPayCallBackVO.setShouldResonse(SUCCESS);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return aliPayCallBackVO;
    }
}
