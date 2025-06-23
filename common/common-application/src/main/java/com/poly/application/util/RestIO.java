package com.springboot.asm.fpoly_asm_springboot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class RestIO {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RestIO() {
    }

    /**
     * Đọc chuỗi JSON từ yêu cầu HTTP.
     *
     * @param req HTTP request
     * @return JSON string
     * @throws IOException nếu xảy ra lỗi khi đọc
     */
    public static String readJson(HttpServletRequest req) throws IOException {
        req.setCharacterEncoding("UTF-8");
        StringBuilder buffer = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        }
        return buffer.toString();
    }

    /**
     * Chuyển chuỗi JSON từ yêu cầu HTTP thành đối tượng Java.
     *
     * @param req   HTTP request
     * @param clazz Lớp của đối tượng cần chuyển
     * @param <T>   Kiểu của đối tượng
     * @return Đối tượng Java
     * @throws IOException nếu xảy ra lỗi khi đọc hoặc chuyển đổi
     */
    public static <T> T readObject(HttpServletRequest req, Class<T> clazz) throws IOException {
        return MAPPER.readValue(readJson(req), clazz);
    }

    /**
     * Gửi chuỗi JSON về phản hồi HTTP.
     *
     * @param resp HTTP response
     * @param json Chuỗi JSON cần gửi
     * @throws IOException nếu xảy ra lỗi khi ghi
     */
    public static void writeJson(HttpServletResponse resp, String json) throws IOException {
        setupResponse(resp);
        resp.getWriter().write(json);
        resp.flushBuffer();
    }

    /**
     * Chuyển đổi đối tượng Java thành JSON và gửi về phản hồi HTTP.
     *
     * @param resp HTTP response
     * @param data Đối tượng Java cần gửi
     * @throws IOException nếu xảy ra lỗi khi chuyển đổi hoặc ghi
     */
    public static void writeObject(HttpServletResponse resp, Object data) throws IOException {
        writeJson(resp, MAPPER.writeValueAsString(data));
    }

    /**
     * Gửi một đối tượng JSON rỗng về phản hồi HTTP.
     *
     * @param resp HTTP response
     * @throws IOException nếu xảy ra lỗi khi ghi
     */
    public static void writeEmptyObject(HttpServletResponse resp) throws IOException {
        writeObject(resp, Collections.emptyMap());
    }

    /**
     * Gửi một thông báo lỗi JSON về phản hồi HTTP.
     *
     * @param resp         HTTP response
     * @param errorMessage Thông báo lỗi
     * @throws IOException nếu xảy ra lỗi khi ghi
     */
    public static void writeError(HttpServletResponse resp, String errorMessage) throws IOException {
        writeObject(resp, Map.of("error", errorMessage));
    }

    /**
     * Thiết lập các thuộc tính chung cho phản hồi HTTP (UTF-8 và Content-Type).
     *
     * @param resp HTTP response
     */
    private static void setupResponse(HttpServletResponse resp) {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
    }
}
