package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.DatasetID;
import com.poly.domain.entity.BaseEntity;

public class Dataset extends BaseEntity<DatasetID> {

    private String name;
    private String source;
    private long size;

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
