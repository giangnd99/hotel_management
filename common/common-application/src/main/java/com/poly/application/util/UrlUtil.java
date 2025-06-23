package com.springboot.asm.fpoly_asm_springboot.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class UrlUtil {

    public String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme(); // http hoặc https
        String serverName = request.getServerName(); // Tên server (localhost, domain, IP)
        int serverPort = request.getServerPort(); // Cổng của server
        String contextPath = request.getContextPath(); // Lấy context path nếu có

        // Bỏ qua port nếu là cổng mặc định (80 cho HTTP, 443 cho HTTPS)
        String portPart = (serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort;

        return scheme + "://" + serverName + portPart + contextPath;
    }

    public String buildResetPasswordLink(HttpServletRequest request, String token) {
        return getBaseUrl(request) + "/reset-password?token=" + token;
    }
}
