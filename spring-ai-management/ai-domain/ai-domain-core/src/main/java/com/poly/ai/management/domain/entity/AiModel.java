package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.domain.entity.BaseEntity;

public class AiModel extends BaseEntity<AiModelID> {
    private final String name;
    private final String provider;
    private String version;
    private boolean isActive;

    // Constructor riêng để sử dụng Builder
    private AiModel(Builder builder) {
        super.setId(builder.aiModelID);
        this.name = builder.name;
        this.provider = builder.provider;
        this.version = builder.version;
        this.isActive = builder.isActive;
    }

    // Logic nghiệp vụ: Xác thực AiModel
    public void validate() {
        if (name == null || name.isEmpty()) {
            throw new AiDomainException("AiModel name cannot be empty!");
        }
        if (provider == null || provider.isEmpty()) {
            throw new AiDomainException("AiModel provider cannot be empty!");
        }
        if (version == null || version.isEmpty()) {
            throw new AiDomainException("AiModel version cannot be empty!");
        }
    }

    // Logic nghiệp vụ: Cập nhật phiên bản
    public void updateVersion(String newVersion) {
        if (newVersion == null || newVersion.isEmpty()) {
            throw new AiDomainException("Version cannot be empty!");
        }
        this.version = newVersion;
    }

    // Logic nghiệp vụ: Kiểm tra tính tương thích với tính năng
    public boolean isCompatibleWith(String feature) {
        if ("function-calling".equals(feature)) {
            return "OpenAI".equals(provider) && version.compareTo("3.5") >= 0;
        }
        return false; // Logic kiểm tra tính tương thích khác có thể mở rộng
    }

    // Logic nghiệp vụ: Kích hoạt mô hình
    public void activate() {
        if (!isActive) {
            this.isActive = true;
        }
    }

    // Logic nghiệp vụ: Tắt mô hình
    public void deactivate() {
        if (isActive) {
            this.isActive = false;
        }
    }

    // Logic nghiệp vụ: Kiểm tra trạng thái hoạt động
    public boolean isActive() {
        return isActive;
    }

    // Getters (chỉ đọc)
    public String getName() {
        return name;
    }

    public String getProvider() {
        return provider;
    }

    public String getVersion() {
        return version;
    }

    // Builder
    public static final class Builder {
        private AiModelID aiModelID;
        private String name;
        private String provider;
        private String version;
        private boolean isActive = true; // Giá trị mặc định là active

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder aiModelID(AiModelID val) {
            aiModelID = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder provider(String val) {
            provider = val;
            return this;
        }

        public Builder version(String val) {
            version = val;
            return this;
        }

        public Builder isActive(boolean val) {
            isActive = val;
            return this;
        }

        public AiModel build() {
            return new AiModel(this);
        }
    }
}