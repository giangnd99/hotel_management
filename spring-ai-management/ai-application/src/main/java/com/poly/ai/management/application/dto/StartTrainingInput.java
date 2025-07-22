package com.poly.ai.management.application.dto;

import lombok.Data;

@Data
public class StartTrainingInput {
    private String modelId;
    private String datasetId;
}
