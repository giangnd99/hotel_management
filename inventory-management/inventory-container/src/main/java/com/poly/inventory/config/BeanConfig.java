package com.poly.inventory.config;

import com.poly.inventory.application.handler.*;
import com.poly.inventory.application.handler.impl.*;
import com.poly.inventory.application.port.in.impl.InventoryUseCaseImpl;
import com.poly.inventory.application.port.out.DeleteInventoryPort;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan
        (basePackages = "com.poly.inventory.application.handler.impl",
                includeFilters =
                @ComponentScan.Filter(type = FilterType.ANNOTATION,
                        classes = {com.poly.inventory.application.annotation.DomainHandler.class}),
                useDefaultFilters = false
        )
@Configuration
public class BeanConfig {

    @Bean
    public GetItemByIdHandler getItemByIdHandler(LoadInventoryPort loadInventoryPort) {
        return new GetItemByIdHandlerImpl(loadInventoryPort);
    }

    @Bean
    public GetItemsHandler getItemsHandler(LoadInventoryPort loadInventoryPort) {
        return new GetItemsHandlerImpl(loadInventoryPort);
    }

    @Bean
    public CreateItemHandler createItemHandler(SaveInventoryPort saveInventoryPort) {
        return new CreateItemHandlerImpl(saveInventoryPort);
    }

    @Bean
    public UpdateItemHandler updateHandler(SaveInventoryPort saveInventoryPort, LoadInventoryPort loadInventoryPort) {
        return new UpdateItemHandlerImpl(saveInventoryPort, loadInventoryPort);
    }

    @Bean
    public DeleteItemHandler deleteItemHandler(DeleteInventoryPort deleteInventoryPort) {
        return new DeleteItemHandlerImpl(deleteInventoryPort);
    }

    @Bean
    public InventoryUseCaseImpl inventoryUseCaseImpl(
            GetItemsHandler getItemsHandler,
            GetItemByIdHandler getItemByIdHandler,
            CreateItemHandler createItemHandler,
            UpdateItemHandler updateItemHandler,
            DeleteItemHandler deleteItemHandler
    ) {
        return new InventoryUseCaseImpl(
                getItemsHandler,
                getItemByIdHandler,
                createItemHandler,
                updateItemHandler,
                deleteItemHandler
        );
    }
}
