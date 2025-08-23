package com.poly.ai.management.domain.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AIResponse {
    String sessionId;
    String value;
}
