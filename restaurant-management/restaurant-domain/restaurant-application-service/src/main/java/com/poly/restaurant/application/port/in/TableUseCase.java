package com.poly.restaurant.application.port.in;

import com.poly.restaurant.application.dto.*;

import java.util.List;

public interface TableUseCase {
    /**
     * Retrieves the list of tables with their current status (AVAILABLE, OCCUPIED, RESERVED).
     *
     * @return list of TableDTO objects representing all tables
     */
    List<TableDTO> getAllTables();

}
