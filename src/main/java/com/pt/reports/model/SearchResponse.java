package com.pt.reports.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Om Prakash Peddamadthala
 * @version 1.0
 * @since 7/14/2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private String courseName;
    private String location;
    private String courseCategory;
    private String facultyName;
    private BigDecimal fee;
    private Long adminContact;
    private String trainingMode;
    private LocalDateTime startDate;
    private String courseStatus;
}
