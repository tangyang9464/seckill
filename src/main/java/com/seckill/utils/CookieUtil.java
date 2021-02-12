/**
 * @author: ty
 * @create: 2021-02-05 20:44
 **/
package com.seckill.utils;

import com.seckill.exception.GlobalException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
    public static String getCookieValue(HttpServletRequest request,String key){
        Cookie[] cookies = request.getCookies();
        if(cookies==null) {
            return null;
        }
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(key)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
