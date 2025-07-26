package com.poly.ai.management.application.dto;

import lombok.Data;

@Data
public class PrepareDatasetInput {
    private String name;
    private long size;
    private String source;
}
