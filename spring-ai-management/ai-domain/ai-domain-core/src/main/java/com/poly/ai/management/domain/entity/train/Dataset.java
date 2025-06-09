package com.poly.ai.management.domain.entity.train;

import com.poly.ai.management.domain.valueobject.DatasetID;
import com.poly.domain.entity.BaseEntity;

public class Dataset extends BaseEntity<DatasetID> {

    private String name;
    private String source;
    private long size;

    public void split(double ratio) {
        if (ratio <= 0 || ratio >= 1) {
            throw new IllegalArgumentException("Split ratio must be between 0 and 1");
        }
        // Logic chia dataset, có thể trả về hai Dataset mới hoặc cập nhật trạng thái nội bộ
    }

    public void validate() {
        if (size < 100) {
            throw new IllegalStateException("Dataset size must be at least 100 records");
        }
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public long getSize() {
        return size;
    }

    private Dataset(Builder builder) {
        super.setId(builder.datasetID);
        name = builder.name;
        source = builder.source;
        size = builder.size;
    }


    public static final class Builder {
        private DatasetID datasetID;
        private String name;
        private String source;
        private long size;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(DatasetID val) {
            datasetID = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder source(String val) {
            source = val;
            return this;
        }

        public Builder size(long val) {
            size = val;
            return this;
        }

        public Dataset build() {
            return new Dataset(this);
        }
    }
}
