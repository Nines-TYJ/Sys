package com.nines.sys.util;

import javax.servlet.http.HttpServletRequest;

public class BrowserUtil {

    /**
     * 获取操作系统,浏览器及浏览器版本信息
     */
    public static String getOsAndBrowserInfo(HttpServletRequest request){
        String  userAgent       = request.getHeader("User-Agent");
        String  user            =   userAgent.toLowerCase();

        String os;
        String browser = null;

        //=================OS Info=======================
        if (userAgent.toLowerCase().contains("windows")) {
            os = "Windows";
        } else if(userAgent.toLowerCase().contains("mac")) {
            os = "Mac";
        } else if(userAgent.toLowerCase().contains("x11")) {
            os = "Unix";
        } else if(userAgent.toLowerCase().contains("android")) {
            os = "Android";
        } else if(userAgent.toLowerCase().contains("iphone")) {
            os = "IPhone";
        }else{
            os = "UnKnown, More-Info: "+ userAgent;
        }

        //===============Browser===========================
        if (user.contains("edge")) {
            browser=(userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("msie")) {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version")) {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]
                    + "-" +(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera")) {
            if(user.contains("opera")){
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]
                        +"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            }else if(user.contains("opr")){
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
                        .replace("OPR", "Opera");
            }
        } else if (user.contains("chrome")) {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.contains("mozilla/7.0")) || (user.contains("netscape6"))  ||
                (user.contains("mozilla/4.7")) || (user.contains("mozilla/4.78")) ||
                (user.contains("mozilla/4.08")) || (user.contains("mozilla/3")) ) {
            browser = "Netscape-?";
        } else if (user.contains("firefox")) {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv")) {
            String ieVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            browser="IE" + ieVersion.substring(0, ieVersion.length() - 1);
        } else {
            browser = "UnKnown, More-Info: "+userAgent;
        }

        return os +" --- "+ browser ;
    }

}
