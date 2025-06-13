package com.poly.ai.management.domain.service.rag;

import com.poly.ai.management.domain.port.input.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {

    private final EmbeddingModel embeddingModel;

    /**
     * Tạo một danh sách các vector nhúng (mảng float) cho một danh sách tài liệu nhất định.
     * Phương thức này trực tiếp gọi phương thức 'embed' của EmbeddingModel của Spring AI,
     * cung cấp các tùy chọn mặc định và chiến lược phân lô (batching) mặc định.
     *
     * @param documents Danh sách các tài liệu cần tạo vector nhúng.
     * @return Một danh sách các mảng float, trong đó mỗi mảng float là vector nhúng
     * cho một tài liệu tương ứng. Thứ tự của các vector nhúng trong danh sách trả về
     * khớp với thứ tự của các tài liệu trong danh sách đầu vào.
     */
    @Override
    public List<float[]> embedDocuments(List<Document> documents) {
        // 1. Định nghĩa EmbeddingOptions: Các tùy chọn này có thể kiểm soát các khía cạnh
        //    như các tham số cụ thể của mô hình (ví dụ: loại nhúng, định dạng mã hóa).
        //    Để đơn giản, chúng ta sử dụng các tùy chọn mặc định ở đây.
        //    Trong một tình huống thực tế, bạn có thể muốn cấu hình các tùy chọn này
        //    thông qua application.properties hoặc truyền chúng dưới dạng tham số cho phương thức này.
        EmbeddingOptions defaultOptions = EmbeddingOptionsBuilder.builder().build();

        // 2. Định nghĩa BatchingStrategy: Chiến lược này xác định cách các tài liệu được nhóm lại
        //    trước khi được gửi đến mô hình nhúng. Phân lô có thể cải thiện hiệu suất
        //    bằng cách giảm số lượng cuộc gọi API, đặc biệt đối với các mô hình từ xa.
        //    BatchingStrategy.builder().build() sử dụng chiến lược phân lô kích thước cố định mặc định.
        BatchingStrategy defaultBatchingStrategy = new TokenCountBatchingStrategy();

        // 3. Gọi phương thức embed của embeddingModel: Phương thức embed trên giao diện EmbeddingModel
        //    được thiết kế để nhận một danh sách tài liệu, các tùy chọn và một chiến lược phân lô.
        //    Nó sẽ xử lý các tài liệu và trả về các vector nhúng tương ứng của chúng.
        return embeddingModel.embed(documents, defaultOptions, defaultBatchingStrategy);
    }

    /**
     * Tạo một vector nhúng đơn lẻ (mảng float) cho một tài liệu nhất định.
     * Đây là một phương thức tiện ích để nhúng các tài liệu riêng lẻ.
     *
     * @param document Tài liệu cần tạo vector nhúng.
     * @return Một mảng float đại diện cho vector nhúng của tài liệu.
     */
    public float[] embedDocument(Document document) {
        // Giao diện EmbeddingModel cung cấp một phương thức nạp chồng (overloaded) cho tài liệu đơn.
        return embeddingModel.embed(document);
    }
}
