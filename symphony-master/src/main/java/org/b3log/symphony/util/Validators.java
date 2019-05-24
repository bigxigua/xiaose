package org.b3log.symphony.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Create Time: 2019年05月10日</p>
 * <p>@author tangxd</p>
 **/
public class Validators {

    static Pattern mobilePattern = Pattern.compile("^[1][0,3,4,5,6,7,8,9][0-9]{9}$");
    static Pattern userNamePattern = Pattern.compile("^[a-zA-Z0-9\\u4E00-\\u9FA5]+$");

    /**
     * 手机号验证
     *
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Matcher m = mobilePattern.matcher(str);
        return m.matches();
    }

    public static boolean verifyUserName(String str) {
        Matcher m = userNamePattern.matcher(str);
        return m.matches();
    }
}
