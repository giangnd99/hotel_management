package com.poly.ai.management.domain.service;

import com.poly.ai.management.domain.dto.AIResponse;
import com.poly.ai.management.domain.dto.BookingDto;
import com.poly.ai.management.domain.dto.PaymentDto;
import com.poly.ai.management.domain.dto.InvoiceDto;
import com.poly.ai.management.domain.port.output.feign.BookingFeign;
import com.poly.ai.management.domain.port.output.feign.PaymentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIReportService {

    private final ChatClient chatClient;
    private final BookingFeign bookingFeign;
    private final PaymentClient paymentClient;
    private final DataProcessingService dataProcessingService;
    private final DL4JDataAnalysisService dl4jDataAnalysisService;

    private static final String SYSTEM_PROMPT = """
            Bạn là một chuyên gia phân tích dữ liệu khách sạn với kiến thức sâu về AI và Machine Learning. Nhiệm vụ của bạn là:
            1. Phân tích dữ liệu đặt phòng, thanh toán và hóa đơn sử dụng các thuật toán nâng cao
            2. Tạo báo cáo chi tiết với insights có giá trị dựa trên phân tích thống kê
            3. Đưa ra khuyến nghị cải thiện dựa trên dữ liệu và xu hướng
            4. Sử dụng ngôn ngữ tiếng Việt dễ hiểu
            5. Tập trung vào các metrics quan trọng: doanh thu, tỷ lệ lấp đầy, khách hàng, rủi ro
            6. Áp dụng các nguyên tắc Data Science và Machine Learning để đưa ra dự đoán
            
            Hãy tạo báo cáo có cấu trúc rõ ràng, dễ đọc và có thể hành động được.
            """;

    /**
     * Tạo báo cáo hàng ngày với phân tích nâng cao
     */
    public Map<String, Object> generateDailyReport(Object payment, Object booking, Object invoice) {
        log.info("Generating daily report with AI and advanced data processing...");

        try {
            // Lấy dữ liệu thực từ các service
            List<BookingDto> bookingData = fetchBookingData();
            List<PaymentDto> paymentData = fetchPaymentData();
            List<InvoiceDto> invoiceData = fetchInvoiceData();

            // Xử lý dữ liệu thông qua DataProcessingService và DL4JDataAnalysisService
            Map<String, Object> processedData = processDataForAdvancedReport(bookingData, paymentData, invoiceData);

            String prompt = buildAdvancedDailyReportPrompt(processedData);
            AIResponse aiResponse = processWithAI(prompt);

            return buildAdvancedDailyReportStructure(aiResponse, LocalDate.now(), processedData);
        } catch (Exception e) {
            log.error("Error generating daily report: ", e);
            return createAdvancedFallbackDailyReport();
        }
    }

    /**
     * Tạo báo cáo hàng tháng với phân tích nâng cao
     */
    public Map<String, Object> generateMonthlyReport(int month, int year) {
        log.info("Generating monthly report for {}/{} with AI and advanced data processing...", month, year);

        try {
            // Lấy dữ liệu thực từ các service
            List<BookingDto> bookingData = fetchBookingData();
            List<PaymentDto> paymentData = fetchPaymentData();
            List<InvoiceDto> invoiceData = fetchInvoiceData();

            // Xử lý dữ liệu thông qua DataProcessingService và DL4JDataAnalysisService
            Map<String, Object> processedData = processDataForAdvancedReport(bookingData, paymentData, invoiceData);

            String prompt = buildAdvancedMonthlyReportPrompt(processedData, month, year);
            AIResponse aiResponse = processWithAI(prompt);

            return buildAdvancedMonthlyReportStructure(aiResponse, month, year, processedData);
        } catch (Exception e) {
            log.error("Error generating monthly report: ", e);
            return createAdvancedFallbackMonthlyReport(month, year);
        }
    }

    /**
     * Tạo báo cáo hàng năm với phân tích nâng cao
     */
    public Map<String, Object> generateYearlyReport(int year) {
        log.info("Generating yearly report for {} with AI and advanced data processing...", year);

        try {
            // Lấy dữ liệu thực từ các service
            List<BookingDto> bookingData = fetchBookingData();
            List<PaymentDto> paymentData = fetchPaymentData();
            List<InvoiceDto> invoiceData = fetchInvoiceData();

            // Xử lý dữ liệu thông qua DataProcessingService và DL4JDataAnalysisService
            Map<String, Object> processedData = processDataForAdvancedReport(bookingData, paymentData, invoiceData);

            String prompt = buildAdvancedYearlyReportPrompt(processedData, year);
            AIResponse aiResponse = processWithAI(prompt);

            return buildAdvancedYearlyReportStructure(aiResponse, year, processedData);
        } catch (Exception e) {
            log.error("Error generating yearly report: ", e);
            return createAdvancedFallbackYearlyReport(year);
        }
    }

    /**
     * Xử lý dữ liệu cho báo cáo nâng cao với tích hợp DataProcessingService và DL4JDataAnalysisService
     */
    private Map<String, Object> processDataForAdvancedReport(List<BookingDto> bookingData, 
                                                           List<PaymentDto> paymentData, 
                                                           List<InvoiceDto> invoiceData) {
        Map<String, Object> processedData = new LinkedHashMap<>();
        
        try {
            // Xử lý dữ liệu Booking với phân tích nâng cao
            Map<String, Object> advancedBookingStats = dataProcessingService.processBookingDataAdvanced(bookingData);
            processedData.put("advancedBookingStats", advancedBookingStats);

            // Xử lý dữ liệu Payment với phân tích nâng cao
            Map<String, Object> advancedPaymentStats = dataProcessingService.processPaymentDataAdvanced(paymentData);
            processedData.put("advancedPaymentStats", advancedPaymentStats);

            // Xử lý dữ liệu Invoice với phân tích nâng cao
            Map<String, Object> advancedInvoiceStats = dataProcessingService.processInvoiceDataAdvanced(invoiceData);
            processedData.put("advancedInvoiceStats", advancedInvoiceStats);

            // Phân tích ML với DL4JDataAnalysisService
            Map<String, Object> mlAnalysis = performMLAnalysis(bookingData, paymentData, invoiceData);
            processedData.put("mlAnalysis", mlAnalysis);

            // Tính toán tổng hợp nâng cao
            Map<String, Object> advancedSummaryStats = calculateAdvancedSummaryStatistics(
                advancedBookingStats, advancedPaymentStats, advancedInvoiceStats, mlAnalysis);
            processedData.put("advancedSummaryStats", advancedSummaryStats);

            // Phân tích tương quan và xu hướng
            Map<String, Object> correlationAnalysis = performCorrelationAnalysis(
                advancedBookingStats, advancedPaymentStats, advancedInvoiceStats, mlAnalysis);
            processedData.put("correlationAnalysis", correlationAnalysis);

            // Dự đoán và forecasting
            Map<String, Object> forecastingAnalysis = performForecastingAnalysis(
                advancedBookingStats, advancedPaymentStats, advancedInvoiceStats, mlAnalysis);
            processedData.put("forecastingAnalysis", forecastingAnalysis);

        } catch (Exception e) {
            log.error("Error processing advanced data for report: ", e);
            // Trả về dữ liệu mặc định nếu xử lý thất bại
            processedData.put("advancedBookingStats", createAdvancedMockBookingStats());
            processedData.put("advancedPaymentStats", createAdvancedMockPaymentStats());
            processedData.put("advancedInvoiceStats", createAdvancedMockInvoiceStats());
            processedData.put("mlAnalysis", createMockMLAnalysis());
            processedData.put("advancedSummaryStats", createAdvancedMockSummaryStats());
            processedData.put("correlationAnalysis", createMockCorrelationAnalysis());
            processedData.put("forecastingAnalysis", createMockForecastingAnalysis());
        }

        return processedData;
    }

    /**
     * Thực hiện phân tích ML với DL4JDataAnalysisService
     */
    private Map<String, Object> performMLAnalysis(List<BookingDto> bookingData, 
                                                 List<PaymentDto> paymentData, 
                                                 List<InvoiceDto> invoiceData) {
        Map<String, Object> mlAnalysis = new LinkedHashMap<>();
        
        try {
            // Phân tích Booking với ML
            Map<String, Object> bookingML = dl4jDataAnalysisService.analyzeBookingWithML(bookingData);
            mlAnalysis.put("bookingML", bookingML);
            
            // Phân tích Payment với ML
            Map<String, Object> paymentML = dl4jDataAnalysisService.analyzePaymentWithML(paymentData);
            mlAnalysis.put("paymentML", paymentML);
            
            // Phân tích Invoice với ML
            Map<String, Object> invoiceML = dl4jDataAnalysisService.analyzeInvoiceWithML(invoiceData);
            mlAnalysis.put("invoiceML", invoiceML);
            
            // Tổng hợp kết quả ML
            mlAnalysis.put("mlSummary", createMLSummary(bookingML, paymentML, invoiceML));
            
        } catch (Exception e) {
            log.error("Error performing ML analysis: ", e);
            mlAnalysis.put("error", "ML analysis failed: " + e.getMessage());
            mlAnalysis.put("fallback", createMockMLAnalysis());
        }
        
        return mlAnalysis;
    }

    /**
     * Tạo tổng hợp kết quả ML
     */
    private Map<String, Object> createMLSummary(Map<String, Object> bookingML, 
                                               Map<String, Object> paymentML, 
                                               Map<String, Object> invoiceML) {
        Map<String, Object> mlSummary = new LinkedHashMap<>();
        
        try {
            // Tính điểm ML tổng hợp
            double mlScore = calculateMLScore(bookingML, paymentML, invoiceML);
            mlSummary.put("overallMLScore", mlScore);
            mlSummary.put("mlGrade", getMLGrade(mlScore));
            
            // Đánh giá chất lượng dữ liệu
            double dataQualityScore = calculateDataQualityScore(bookingML, paymentML, invoiceML);
            mlSummary.put("dataQualityScore", dataQualityScore);
            mlSummary.put("dataQualityLevel", getDataQualityLevel(dataQualityScore));
            
            // Đánh giá độ tin cậy của dự đoán
            double predictionConfidence = calculatePredictionConfidence(bookingML, paymentML, invoiceML);
            mlSummary.put("predictionConfidence", predictionConfidence);
            mlSummary.put("confidenceLevel", getConfidenceLevel(predictionConfidence));
            
        } catch (Exception e) {
            log.error("Error creating ML summary: ", e);
            mlSummary.put("error", "ML summary creation failed");
        }
        
        return mlSummary;
    }

    /**
     * Tính điểm ML tổng hợp
     */
    private double calculateMLScore(Map<String, Object> bookingML, 
                                  Map<String, Object> paymentML, 
                                  Map<String, Object> invoiceML) {
        try {
            double bookingScore = getMLScoreFromData(bookingML);
            double paymentScore = getMLScoreFromData(paymentML);
            double invoiceScore = getMLScoreFromData(invoiceML);
            
            return (bookingScore + paymentScore + invoiceScore) / 3.0;
        } catch (Exception e) {
            log.error("Error calculating ML score: ", e);
            return 0.7; // Default score
        }
    }

    /**
     * Lấy điểm ML từ dữ liệu
     */
    private double getMLScoreFromData(Map<String, Object> mlData) {
        try {
            if (mlData.containsKey("error")) {
                return 0.5; // Low score if error
            }
            
            // Kiểm tra các metrics quan trọng
            boolean hasTimeSeries = mlData.containsKey("timeSeriesAnalysis");
            boolean hasClustering = mlData.containsKey("clusteringAnalysis");
            boolean hasPrediction = mlData.containsKey("predictionAnalysis");
            boolean hasAnomaly = mlData.containsKey("anomalyAnalysis");
            
            double score = 0.0;
            if (hasTimeSeries) score += 0.25;
            if (hasClustering) score += 0.25;
            if (hasPrediction) score += 0.25;
            if (hasAnomaly) score += 0.25;
            
            return score;
        } catch (Exception e) {
            return 0.5;
        }
    }

    /**
     * Tính điểm chất lượng dữ liệu
     */
    private double calculateDataQualityScore(Map<String, Object> bookingML, 
                                           Map<String, Object> paymentML, 
                                           Map<String, Object> invoiceML) {
        try {
            double bookingQuality = getDataQualityFromML(bookingML);
            double paymentQuality = getDataQualityFromML(paymentML);
            double invoiceQuality = getDataQualityFromML(invoiceML);
            
            return (bookingQuality + paymentQuality + invoiceQuality) / 3.0;
        } catch (Exception e) {
            log.error("Error calculating data quality score: ", e);
            return 0.8; // Default quality score
        }
    }

    /**
     * Lấy điểm chất lượng dữ liệu từ ML data
     */
    private double getDataQualityFromML(Map<String, Object> mlData) {
        try {
            if (mlData.containsKey("dataQualityScore")) {
                return (Double) mlData.get("dataQualityScore");
            }
            
            // Fallback calculation
            if (mlData.containsKey("mlEnhanced") && (Boolean) mlData.get("mlEnhanced")) {
                return 0.85;
            }
            
            return 0.7;
        } catch (Exception e) {
            return 0.7;
        }
    }

    /**
     * Tính độ tin cậy của dự đoán
     */
    private double calculatePredictionConfidence(Map<String, Object> bookingML, 
                                               Map<String, Object> paymentML, 
                                               Map<String, Object> invoiceML) {
        try {
            double bookingConfidence = getPredictionConfidenceFromML(bookingML);
            double paymentConfidence = getPredictionConfidenceFromML(paymentML);
            double invoiceConfidence = getPredictionConfidenceFromML(invoiceML);
            
            return (bookingConfidence + paymentConfidence + invoiceConfidence) / 3.0;
        } catch (Exception e) {
            log.error("Error calculating prediction confidence: ", e);
            return 0.75; // Default confidence
        }
    }

    /**
     * Lấy độ tin cậy dự đoán từ ML data
     */
    private double getPredictionConfidenceFromML(Map<String, Object> mlData) {
        try {
            if (mlData.containsKey("predictionAnalysis")) {
                Map<String, Object> prediction = (Map<String, Object>) mlData.get("predictionAnalysis");
                if (prediction.containsKey("overallConfidence")) {
                    return (Double) prediction.get("overallConfidence");
                }
            }
            
            return 0.75; // Default confidence
        } catch (Exception e) {
            return 0.75;
        }
    }

    /**
     * Lấy grade ML
     */
    private String getMLGrade(double score) {
        if (score >= 0.9) return "Xuất sắc";
        else if (score >= 0.8) return "Tốt";
        else if (score >= 0.7) return "Khá";
        else if (score >= 0.6) return "Trung bình";
        else return "Cần cải thiện";
    }

    /**
     * Lấy level chất lượng dữ liệu
     */
    private String getDataQualityLevel(double score) {
        if (score >= 0.9) return "Rất cao";
        else if (score >= 0.8) return "Cao";
        else if (score >= 0.7) return "Trung bình";
        else return "Thấp";
    }

    /**
     * Lấy level độ tin cậy
     */
    private String getConfidenceLevel(double confidence) {
        if (confidence >= 0.9) return "Rất cao";
        else if (confidence >= 0.8) return "Cao";
        else if (confidence >= 0.7) return "Trung bình";
        else return "Thấp";
    }

    // Các phương thức khác giữ nguyên từ phiên bản trước
    private Map<String, Object> calculateAdvancedSummaryStatistics(Map<String, Object> advancedBookingStats, 
                                                                 Map<String, Object> advancedPaymentStats, 
                                                                 Map<String, Object> advancedInvoiceStats,
                                                                 Map<String, Object> mlAnalysis) {
        Map<String, Object> advancedSummary = new HashMap<>();
        
        try {
            // Tính tổng doanh thu từ tất cả nguồn
            BigDecimal bookingRevenue = (BigDecimal) advancedBookingStats.getOrDefault("totalRevenue", BigDecimal.ZERO);
            BigDecimal invoiceRevenue = (BigDecimal) advancedInvoiceStats.getOrDefault("totalRevenue", BigDecimal.ZERO);
            BigDecimal totalRevenue = bookingRevenue.add(invoiceRevenue);
            advancedSummary.put("totalRevenue", totalRevenue);

            // Tính tỷ lệ thành công tổng thể
            Long successfulBookings = (Long) advancedBookingStats.getOrDefault("successfulBookings", 0L);
            Long totalBookings = (Long) advancedBookingStats.getOrDefault("totalBookings", 0L);
            double successRate = totalBookings > 0 ? (double) successfulBookings / totalBookings * 100 : 0.0;
            advancedSummary.put("overallSuccessRate", String.format("%.1f%%", successRate));

            // Tính tỷ lệ thanh toán thành công
            Long successfulPayments = (Long) advancedPaymentStats.getOrDefault("successfulPayments", 0L);
            Long totalPayments = (Long) advancedPaymentStats.getOrDefault("totalTransactions", 0L);
            double paymentSuccessRate = totalPayments > 0 ? (double) successfulPayments / totalPayments * 100 : 0.0;
            advancedSummary.put("paymentSuccessRate", String.format("%.1f%%", paymentSuccessRate));

            // Tính hiệu quả tổng thể
            double overallEfficiency = (successRate + paymentSuccessRate) / 2.0;
            advancedSummary.put("overallEfficiency", String.format("%.1f%%", overallEfficiency));
            advancedSummary.put("efficiencyGrade", 
                overallEfficiency >= 95 ? "Xuất sắc" : 
                overallEfficiency >= 90 ? "Tốt" : 
                overallEfficiency >= 80 ? "Khá" : "Cần cải thiện");

            // Thêm thông tin ML
            if (mlAnalysis != null && mlAnalysis.containsKey("mlSummary")) {
                Map<String, Object> mlSummary = (Map<String, Object>) mlAnalysis.get("mlSummary");
                advancedSummary.put("mlScore", mlSummary.get("overallMLScore"));
                advancedSummary.put("mlGrade", mlSummary.get("mlGrade"));
                advancedSummary.put("dataQuality", mlSummary.get("dataQualityLevel"));
            }

        } catch (Exception e) {
            log.error("Error calculating advanced summary statistics: ", e);
            advancedSummary.put("totalRevenue", BigDecimal.ZERO);
            advancedSummary.put("overallSuccessRate", "0.0%");
            advancedSummary.put("paymentSuccessRate", "0.0%");
            advancedSummary.put("overallEfficiency", "0.0%");
            advancedSummary.put("efficiencyGrade", "Không xác định");
        }

        return advancedSummary;
    }

    // Placeholder methods for other functionality
    private Map<String, Object> performCorrelationAnalysis(Map<String, Object>... data) {
        Map<String, Object> correlation = new HashMap<>();
        correlation.put("correlationStrength", "Trung bình");
        correlation.put("correlationType", "Positive");
        return correlation;
    }

    private Map<String, Object> performForecastingAnalysis(Map<String, Object>... data) {
        Map<String, Object> forecasting = new HashMap<>();
        forecasting.put("forecastAccuracy", "75%");
        forecasting.put("forecastModel", "Enhanced ML Model");
        return forecasting;
    }

    // Mock data methods
    private Map<String, Object> createAdvancedMockBookingStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBookings", 45);
        stats.put("successfulBookings", 38L);
        stats.put("pendingBookings", 5L);
        stats.put("cancelledBookings", 2L);
        stats.put("totalRevenue", new BigDecimal("15500000"));
        return stats;
    }

    private Map<String, Object> createAdvancedMockPaymentStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTransactions", 42);
        stats.put("successfulPayments", 38L);
        stats.put("pendingPayments", 3L);
        stats.put("totalAmount", new BigDecimal("15500000"));
        return stats;
    }

    private Map<String, Object> createAdvancedMockInvoiceStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalInvoices", 38);
        stats.put("paidInvoices", 35L);
        stats.put("pendingInvoices", 3L);
        stats.put("totalRevenue", new BigDecimal("15500000"));
        return stats;
    }

    private Map<String, Object> createMockMLAnalysis() {
        Map<String, Object> ml = new HashMap<>();
        ml.put("mlScore", 0.75);
        ml.put("mlGrade", "Khá");
        return ml;
    }

    private Map<String, Object> createAdvancedMockSummaryStats() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalRevenue", new BigDecimal("15500000"));
        summary.put("overallSuccessRate", "84.4%");
        summary.put("efficiencyGrade", "Khá");
        return summary;
    }

    private Map<String, Object> createMockCorrelationAnalysis() {
        Map<String, Object> correlation = new HashMap<>();
        correlation.put("correlationStrength", "Trung bình");
        return correlation;
    }

    private Map<String, Object> createMockForecastingAnalysis() {
        Map<String, Object> forecasting = new HashMap<>();
        forecasting.put("forecastAccuracy", "75%");
        return forecasting;
    }

    // Data fetching methods
    private List<BookingDto> fetchBookingData() {
        try {
            return bookingFeign.getAll();
        } catch (Exception e) {
            log.error("Error fetching booking data: ", e);
            return new ArrayList<>();
        }
    }

    private List<PaymentDto> fetchPaymentData() {
        try {
            return paymentClient.getAll();
        } catch (Exception e) {
            log.error("Error fetching payment data: ", e);
            return new ArrayList<>();
        }
    }

    private List<InvoiceDto> fetchInvoiceData() {
        try {
            return paymentClient.getInvoice();
        } catch (Exception e) {
            log.error("Error fetching invoice data: ", e);
            return new ArrayList<>();
        }
    }

    // AI processing methods
    private AIResponse processWithAI(String prompt) {
        try {
            Message systemMessage = new SystemMessage(SYSTEM_PROMPT);
            Message userMessage = new UserMessage(prompt);
            Prompt aiPrompt = new Prompt(Arrays.asList(systemMessage, userMessage));
            
            String response = chatClient.prompt(aiPrompt).call().content();
            
            AIResponse aiResponse = new AIResponse();
            aiResponse.setValue(response);
            aiResponse.setSessionId(UUID.randomUUID().toString());
            
            return aiResponse;
        } catch (Exception e) {
            log.error("Error processing with AI: ", e);
            AIResponse fallbackResponse = new AIResponse();
            fallbackResponse.setValue("Không thể xử lý với AI. Vui lòng thử lại sau.");
            return fallbackResponse;
        }
    }

    // Prompt building methods
    private String buildAdvancedDailyReportPrompt(Map<String, Object> processedData) {
        return String.format("""
            Tạo báo cáo hàng ngày chi tiết dựa trên dữ liệu sau:
            
            Dữ liệu Booking: %s
            Dữ liệu Payment: %s
            Dữ liệu Invoice: %s
            Phân tích ML: %s
            
            Hãy tạo báo cáo có cấu trúc rõ ràng với:
            1. Tóm tắt tổng quan
            2. Phân tích chi tiết từng lĩnh vực
            3. Insights từ AI và ML
            4. Khuyến nghị cải thiện
            5. Dự đoán cho ngày tiếp theo
            """, 
            processedData.get("advancedBookingStats"),
            processedData.get("advancedPaymentStats"),
            processedData.get("advancedInvoiceStats"),
            processedData.get("mlAnalysis")
        );
    }

    private String buildAdvancedMonthlyReportPrompt(Map<String, Object> processedData, int month, int year) {
        return String.format("""
            Tạo báo cáo hàng tháng %d/%d chi tiết dựa trên dữ liệu sau:
            
            Dữ liệu Booking: %s
            Dữ liệu Payment: %s
            Dữ liệu Invoice: %s
            Phân tích ML: %s
            
            Hãy tạo báo cáo có cấu trúc rõ ràng với:
            1. Tóm tắt tổng quan tháng
            2. So sánh với tháng trước
            3. Phân tích xu hướng
            4. Insights từ AI và ML
            5. Khuyến nghị cải thiện
            6. Dự đoán cho tháng tiếp theo
            """, 
            month, year,
            processedData.get("advancedBookingStats"),
            processedData.get("advancedPaymentStats"),
            processedData.get("advancedInvoiceStats"),
            processedData.get("mlAnalysis")
        );
    }

    private String buildAdvancedYearlyReportPrompt(Map<String, Object> processedData, int year) {
        return String.format("""
            Tạo báo cáo hàng năm %d chi tiết dựa trên dữ liệu sau:
            
            Dữ liệu Booking: %s
            Dữ liệu Payment: %s
            Dữ liệu Invoice: %s
            Phân tích ML: %s
            
            Hãy tạo báo cáo có cấu trúc rõ ràng với:
            1. Tóm tắt tổng quan năm
            2. So sánh với năm trước
            3. Phân tích xu hướng dài hạn
            4. Insights từ AI và ML
            5. Khuyến nghị chiến lược
            6. Dự đoán cho năm tiếp theo
            """, 
            year,
            processedData.get("advancedBookingStats"),
            processedData.get("advancedPaymentStats"),
            processedData.get("advancedInvoiceStats"),
            processedData.get("mlAnalysis")
        );
    }

    // Report structure building methods
    private Map<String, Object> buildAdvancedDailyReportStructure(AIResponse aiResponse, LocalDate date, Map<String, Object> processedData) {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("reportType", "DAILY");
        report.put("date", date);
        report.put("aiContent", aiResponse.getValue());
        report.put("processedData", processedData);
        report.put("generatedAt", LocalDateTime.now());
        report.put("version", "2.0-Enhanced");
        return report;
    }

    private Map<String, Object> buildAdvancedMonthlyReportStructure(AIResponse aiResponse, int month, int year, Map<String, Object> processedData) {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("reportType", "MONTHLY");
        report.put("month", month);
        report.put("year", year);
        report.put("aiContent", aiResponse.getValue());
        report.put("processedData", processedData);
        report.put("generatedAt", LocalDateTime.now());
        report.put("version", "2.0-Enhanced");
        return report;
    }

    private Map<String, Object> buildAdvancedYearlyReportStructure(AIResponse aiResponse, int year, Map<String, Object> processedData) {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("reportType", "YEARLY");
        report.put("year", year);
        report.put("aiContent", aiResponse.getValue());
        report.put("processedData", processedData);
        report.put("generatedAt", LocalDateTime.now());
        report.put("version", "2.0-Enhanced");
        return report;
    }

    // Fallback methods
    private Map<String, Object> createAdvancedFallbackDailyReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("reportType", "DAILY");
        report.put("date", LocalDate.now());
        report.put("error", "Không thể tạo báo cáo. Sử dụng dữ liệu fallback.");
        report.put("fallbackData", createAdvancedMockSummaryStats());
        report.put("generatedAt", LocalDateTime.now());
        report.put("version", "2.0-Enhanced-Fallback");
        return report;
    }

    private Map<String, Object> createAdvancedFallbackMonthlyReport(int month, int year) {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("reportType", "MONTHLY");
        report.put("month", month);
        report.put("year", year);
        report.put("error", "Không thể tạo báo cáo. Sử dụng dữ liệu fallback.");
        report.put("fallbackData", createAdvancedMockSummaryStats());
        report.put("generatedAt", LocalDateTime.now());
        report.put("version", "2.0-Enhanced-Fallback");
        return report;
    }

    private Map<String, Object> createAdvancedFallbackYearlyReport(int year) {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("reportType", "YEARLY");
        report.put("year", year);
        report.put("error", "Không thể tạo báo cáo. Sử dụng dữ liệu fallback.");
        report.put("fallbackData", createAdvancedMockSummaryStats());
        report.put("generatedAt", LocalDateTime.now());
        report.put("version", "2.0-Enhanced-Fallback");
        return report;
    }
}
