package com.poly.promotion.domain.application.mapper.base;

import com.poly.domain.entity.BaseEntity;
import com.poly.promotion.domain.application.model.BaseModel;

public interface EntityToModelTransformer<K extends BaseEntity, V extends BaseModel> extends ITransformer<K, V> {
}
