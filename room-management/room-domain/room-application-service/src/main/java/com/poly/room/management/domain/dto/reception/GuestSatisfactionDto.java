package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestSatisfactionDto {
    private LocalDate date;
    private Long totalReviews;
    private Double averageRating;
    private Long excellentReviews;
    private Long goodReviews;
    private Long averageReviews;
    private Long poorReviews;
    private Long complaints;
    private Long compliments;
    private String topComplaint;
    private String topCompliment;
    private Double satisfactionScore;
}
