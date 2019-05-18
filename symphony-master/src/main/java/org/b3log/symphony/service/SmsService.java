package org.b3log.symphony.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.service.annotation.Service;
import org.b3log.symphony.cache.CommunalCache;
import org.b3log.symphony.util.Geos;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

import java.util.Random;

/**
 * <p>Create Time: 2019年05月13日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class SmsService {

    public static class Result {
        public Result(boolean Ok, String message) {
            this.ok = Ok;
            this.message = message;
        }

        private boolean ok;
        private String message;

        public boolean isOk() {
            return ok;
        }

        public void setOk(boolean ok) {
            this.ok = ok;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(Geos.class);

    private static final String REG_VERIFY_SMS_CODE = "SMS_165108475";

    @Inject
    private CommunalCache cache;

    public JSONObject sendSms(String phoneNumber, String templateCode, String signName, String templateParam) {
        DefaultProfile profile = DefaultProfile.getProfile("default", Symphonys.SMS_ACCESS_KEYID, Symphonys.SMS_ACCESS_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateParam", templateParam);

        try {
            CommonResponse response = client.getCommonResponse(request);
            return new JSONObject(response.getData());
        } catch (ClientException e) {
            LOGGER.log(Level.WARN, "SmsService Fail:" + e.getErrMsg());
        }
        return null;
    }

    public Result sendRegisterCodeSms(String phoneNumber) {
        int code = new Random().nextInt(10000);
        String codeStr = String.format("%04d", code);
        JSONObject rtn = sendSms(phoneNumber, REG_VERIFY_SMS_CODE, "scboy", "{\"code\":\"" + codeStr + "\"}");
        if (rtn != null) {
            String rCode = rtn.getString("code");
            if ("OK".equals(rCode)) {
                cache.get().put(String.format("PhoneVerifyCodes:%s",phoneNumber),new JSONObject().put("code",rCode),5 * 60);
                return new Result(true, "发送成功");
            }
            return new Result(false, rtn.getString("Message"));
        }
        return new Result(false, "验证码服务器错误");
    }

    public String getPrevCode(String phoneNumber) {
        JSONObject obj = cache.get().get(String.format("PhoneVerifyCodes:%s",phoneNumber));
        if(obj == null) {
            return null;
        }
        return obj.getString("code");
    }
}
