package edu.poly.notificationmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final long OTP_EXPIRATION_TIME_MINUTES = 5; // OTP hết hạn 5 phút

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private EmailService emailService; // gửi OTP qua email

    /**
     * Tạo và lưu OTP vào Redis, sau đó gửi OTP qua email.
     * @param email Địa chỉ email nhận OTP.
     * @return OTP đã tạo.
     */
    public String generateAndSendOtp(String email) {
        String otp = generateOtp();
        // Lưu OTP vào Redis với key là email và thời gian hết hạn
        redisTemplate.opsForValue().set(email, otp, OTP_EXPIRATION_TIME_MINUTES, TimeUnit.MINUTES);

        // Gửi OTP qua email
        try {
            emailService.sendSimpleEmail(email, "Mã OTP của bạn", "Mã OTP của bạn là: " + otp + ". Mã này sẽ hết hạn sau " + OTP_EXPIRATION_TIME_MINUTES + " phút.");
        } catch (Exception e) {
            System.err.println("Lỗi khi gửi OTP qua email: " + e.getMessage());
            // Xử lý lỗi gửi email (ví dụ: log lỗi, trả về null hoặc throw exception)
        }
        return otp;
    }

    /**
     * Xác thực OTP.
     * @param email Địa chỉ email đã nhận OTP.
     * @param otp Mã OTP cần xác thực.
     * @return true nếu OTP hợp lệ, false nếu không.
     */
    public boolean validateOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            // Xóa OTP sau khi xác thực thành công để tránh sử dụng lại
            redisTemplate.delete(email);
            return true;
        }
        return false;
    }

    /**
     * Tạo một mã OTP ngẫu nhiên gồm 6 chữ số.
     * @return Mã OTP.
     */
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo số ngẫu nhiên từ 100000 đến 999999
        return String.valueOf(otp);
    }
}
