package com.poly.promotion.domain.application.mapper.base;

import com.poly.domain.entity.BaseEntity;
import com.poly.promotion.domain.application.model.BaseModel;

public interface ModelToEntityTransformer<K extends BaseModel, V extends BaseEntity> extends ITransformer<K, V> {
}
