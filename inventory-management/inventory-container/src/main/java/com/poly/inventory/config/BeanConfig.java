package com.poly.inventory.config;

import com.poly.inventory.application.handler.*;
import com.poly.inventory.application.handler.impl.*;
import com.poly.inventory.application.port.in.impl.InventoryUseCaseImpl;
import com.poly.inventory.application.port.in.impl.TransactionUseCaseImpl;
import com.poly.inventory.application.port.out.*;
import com.poly.inventory.application.port.out.feign.StaffServiceClient;
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
    public InventoryUseCaseImpl inventoryUseCaseImpl(
            GetItemsHandler getItemsHandler,
            GetItemByIdHandler getItemByIdHandler,
            CreateItemHandler createItemHandler,
            UpdateItemHandler updateItemHandler,
            DeleteItemHandler deleteItemHandler,
            SearchItemHandler searchItemHandler
    ) {
        return new InventoryUseCaseImpl(
                getItemsHandler,
                getItemByIdHandler,
                createItemHandler,
                updateItemHandler,
                deleteItemHandler,
                searchItemHandler
        );
    }

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
    public SearchItemHandler searchItemHandler(LoadInventoryPort searchItemHandler) {
        return new SearchItemHandlerImpl(searchItemHandler);
    }

    @Bean
    public TransactionUseCaseImpl transactionUseCaseImpl(
            GetTransactionsHandler getTransactionsHandler,
            StockInHandler stockInHandler,
            StockOutHandler stockOutHandler,
            InventoryCheckHandler inventoryCheckHandler,
            GetReportHandler getReportHandler
    ) {
        return new TransactionUseCaseImpl(
                getTransactionsHandler,
                stockInHandler,
                stockOutHandler,
                inventoryCheckHandler,
                getReportHandler
        );
    }

    @Bean
    public GetTransactionsHandler getTransactionsHandler(LoadTransactionPort loadTransactionPort) {
        return new GetTransactionsHandlerImpl(loadTransactionPort);
    }

    @Bean
    public StockInHandler stockInHandler(SaveTransactionPort saveTransactionPort,
                                         LoadInventoryPort loadInventoryPort,
                                         SaveInventoryPort saveInventoryPort) {
        return new StockInHandlerImpl(saveTransactionPort, loadInventoryPort, saveInventoryPort);
    }

    @Bean
    public StockOutHandler stockOutHandler(SaveTransactionPort saveTransactionPort,
                                           LoadInventoryPort loadInventoryPort,
                                           SaveInventoryPort saveInventoryPort) {
        return new StockOutHandlerImpl(saveTransactionPort, loadInventoryPort, saveInventoryPort);
    }

    @Bean
    public InventoryCheckHandler inventoryCheckHandler(SaveTransactionPort saveTransactionPort,
                                                       LoadInventoryPort loadInventoryPort,
                                                       SaveInventoryPort saveInventoryPort) {
        return new InventoryCheckHandlerImpl(saveTransactionPort, loadInventoryPort, saveInventoryPort);
    }

    @Bean
    public GetReportHandler getReportHandler(LoadTransactionPort loadTransactionPort) {
        return new GetReportHandlerImpl(loadTransactionPort);
    }
}
