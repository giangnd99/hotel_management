package com.poly.customerdataaccess.mapper;

import com.poly.customerdataaccess.entity.CustomerEntity;
import com.poly.customerdataaccess.entity.Level;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;

public class CustomerDataMapper {

    public static CustomerEntity mapToEntity(Customer domain) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(domain.getId().getValue());
        customerEntity.setUserId(domain.getUserId().getValue());
        customerEntity.setFirstName(domain.getFullName().getFirstName());
        customerEntity.setLastName(domain.getFullName().getLastName());
        customerEntity.setAddress(domain.getAddress().toFullAddress());
        customerEntity.setDateOfBirth(domain.getDateOfBirth().getValue());
        customerEntity.setAccumulatedSpending(domain.getAccumulatedSpending().getAmount());
        customerEntity.setLevel(Level.valueOf(domain.getLevel().name()));
        customerEntity.setImageUrl(domain.getImage().getUrl());
        customerEntity.setBehaviorData(domain.getBehaviorData().toJson());
        customerEntity.setCreatedAt(domain.getCreatedAt());
        customerEntity.setUpdatedAt(domain.getUpdatedAt());
        return customerEntity;
    }

    public static Customer mapToDomain(CustomerEntity entity) {
        return Customer.builder()
                .customerId(CustomerId.from(entity.getId()))
                .userId(UserId.from(entity.getUserId()))
                .name(Name.from(entity.getFirstName(), entity.getLastName()))
                .address(Address.from(entity.getAddress()))
                .dateOfBirth(DateOfBirth.from(entity.getDateOfBirth()))
                .accumulatedSpending(Money.from(entity.getAccumulatedSpending()))
                .level(com.poly.customerdomain.model.entity.valueobject.Level.valueOf(entity.getLevel().name()))
                .behaviorData(BehaviorData.fromJson(entity.getBehaviorData()))
                .image(ImageUrl.from(entity.getImageUrl()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
