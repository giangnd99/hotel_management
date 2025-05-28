package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.domain.entity.BaseEntity;

public class AiModel extends BaseEntity<AiModelID> {
    private String name;
    private String provider;
    private String version;

    public String getName() {
        return name;
    }

    public String getProvider() {
        return provider;
    }

    public String getVersion() {
        return version;
    }

    private AiModel(Builder builder) {
        super.setId(builder.aiModelID);
        name = builder.name;
        provider = builder.provider;
        version = builder.version;
    }


    public static final class Builder {
        private AiModelID aiModelID;
        private String name;
        private String provider;
        private String version;

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

        public AiModel build() {
            return new AiModel(this);
        }
    }
}
