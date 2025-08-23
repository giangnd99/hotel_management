package com.poly.inventory.config;

import com.poly.inventory.application.handler.*;
import com.poly.inventory.application.handler.impl.*;
import com.poly.inventory.application.port.in.impl.TransactionUseCaseImpl;
import com.poly.inventory.application.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

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
