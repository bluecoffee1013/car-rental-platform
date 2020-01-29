package com.yepit.mapp.framework.util.sms;

import com.yepit.mapp.framework.util.HttpUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DianTaoSMSUtils {

    private static Logger logger = Logger.getLogger(DianTaoSMSUtils.class);

    //发送传递的参数
    private static String username = "zhming1115";
    private static String secretkey = "zm123456";
    private static String mobile;
    private static String content;
    private static String sendTime;
    private static Integer productid;
    private static String extno;
    private static String sendurl = "http://112.124.17.46:7001/";

    public static String sendSms(String mobile, String content) throws Exception{
        String sendUrl = null;
        try {//
            sendUrl = sendurl + "sms_token?ddtkey=" + username + "&secretkey="+ secretkey
                    + "&mobile=" + mobile + "&content="+URLEncoder.encode(content, "UTF-8")+"&sendTime="+sendTime;

            logger.info("短信内容为===:"+ sendUrl);

            String result = HttpUtils.doGet(sendUrl);
            return result;
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.toString());
            throw ex;
        }
    }
}
