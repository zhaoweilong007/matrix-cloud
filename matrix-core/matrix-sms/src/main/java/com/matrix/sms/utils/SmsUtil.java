/**
 * @author LeonZhou
 * @since 2023/6/20
 **/
package com.matrix.sms.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/*
 * 短信服务实现类-阿里云
 * @author LeonZhou
 * @since 2023/6/20
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Deprecated
public class SmsUtil {
//
//    private static final SmsProperties smsProperties = SpringUtil.getBean(SmsProperties.class);
//
//    private static Client client;
//
//    public static Client createClient() throws Exception {
//        if (client == null) {
//            Config config = new Config();
//            config.accessKeyId = smsProperties.getAccessKey();
//            config.accessKeySecret = smsProperties.getAccessKeySecret();
//            client = new Client(config);
//        }
//        return client;
//    }
//
//    public static boolean sendSms(SmsVo smsVo) {
//        try {
//            Client client = createClient();
//            // 1.发送短信
//            SendSmsRequest sendReq = new SendSmsRequest()
//                .setSignName(smsVo.getSignName())//短信签名
//                .setTemplateCode(smsVo.getTemplateId())//短信模板
//                .setPhoneNumbers(smsVo.getPhone())//这里填写接受短信的手机号码
//                .setTemplateParam(smsVo.getParams().toJSONString());//短信内容
//            log.info("[阿里云短信]SendSmsRequest：{}", JSON.toJSONString(sendReq));
//            SendSmsResponse sendResp = client.sendSms(sendReq);
//            log.info("[阿里云短信]SendSmsResponse：{}", JSON.toJSONString(sendResp));
//            String code = sendResp.body.code;
//            if (!com.aliyun.teautil.Common.equalString(code, "OK")) {
//                log.info("错误信息: {}", sendResp.body.message);
//                //return ; 报错了不再往下走查询接口
//                return false;
//            }
//
//            /* 是否需要查询发送结果
//            String bizId = sendResp.body.bizId;
//            // 2. 等待 10 秒后查询结果
//            com.aliyun.teautil.Common.sleep(10000);
//            // 3.查询结果
//            List<String> phoneNums = com.aliyun.darabonbastring.Client.split(phone, ",", -1);
//            for (String phoneNum : phoneNums) {
//                QuerySendDetailsRequest queryReq = new QuerySendDetailsRequest()
//                    .setPhoneNumber(com.aliyun.teautil.Common.assertAsString(phoneNum))
//                    .setBizId(bizId)
//                    .setSendDate(DateUtils.dateTimeNum(new Date()))
//                    .setPageSize(10L)
//                    .setCurrentPage(1L);
//                QuerySendDetailsResponse queryResp = client.querySendDetails(queryReq);
//                List<QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO> dtos = queryResp.body.smsSendDetailDTOs.smsSendDetailDTO;
//                // 打印结果
//                for (QuerySendDetailsResponseBody.QuerySendDetailsResponseBodySmsSendDetailDTOsSmsSendDetailDTO dto : dtos) {
//                    if (com.aliyun.teautil.Common.equalString("" + dto.sendStatus + "", "3")) {
//                        log.info("" + dto.phoneNum + " 发送成功，接收时间: " + dto.receiveDate + "");
//                    } else if (com.aliyun.teautil.Common.equalString("" + dto.sendStatus + "", "2")) {
//                        log.info("" + dto.phoneNum + " 发送失败");
//                    } else {
//                        log.info("" + dto.phoneNum + " 正在发送中...");
//                    }
//                }
//            }*/
//        } catch (Exception e) {
//            log.error("短信发送失败，原因：{}", e);
//            return false;
//        }
//        return true;
//    }
}

