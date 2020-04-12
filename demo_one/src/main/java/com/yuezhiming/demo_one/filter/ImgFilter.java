package com.yuezhiming.demo_one.filter;/**
 * Created by ASUSon 2020/4/12 18:34
 */


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: demo_one

 * @description: 资源防盗

 * @author: yuezm

 * @create: 2020-04-12 18:34
 **/
@WebFilter(filterName = "imgFilter", urlPatterns = "/img/*")
@ServletComponentScan
public class ImgFilter implements Filter {

    @Value("${domain.name}")
    private String domainName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String referer = req.getHeader("Referer");
        if (StringUtils.isEmpty(referer)) {
            request.getRequestDispatcher("/img/图片二.jpg").forward(request, response);
            return;
        }
        String domain = getDomain(referer);
        if (!domain.equals(domainName)) {
            request.getRequestDispatcher("/img/图片二.jpg").forward(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    public String getDomain(String url) {
        String result = "";
        int j = 0, startIndex = 0, endIndex = 0;
        for (int i = 0; i < url.length(); i++) {
            if (url.charAt(i) == '/') {
                j++;
                if (j == 2)
                    startIndex = i;
                else if (j == 3)
                    endIndex = i;
            }

        }
        result = url.substring(startIndex + 1, endIndex);
        return result;
    }

}
