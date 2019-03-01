package cn.xtits.imonitor.controller;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class BaseController {


    public Integer getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getAttribute("userId") == null ? -1 : Integer.parseInt(request.getAttribute("userId").toString());
    }

    public String getUserName() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getAttribute("userName")== null ? "test" : request.getAttribute("userName").toString();
    }

    public String getOauth() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getParameter("oauth") == null ? "test" : request.getParameter("oauth");
    }

    public Date getDateNow() {
        return new Date();
    }


}
