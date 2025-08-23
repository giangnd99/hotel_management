package com.poly.ai.management.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AIResponse {
    String sessionId;
    String value;
}
