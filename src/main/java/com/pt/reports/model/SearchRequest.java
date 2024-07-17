package com.pt.reports.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Om Prakash Peddamadthala
 * @version 1.0
 * @since 7/14/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    private String courseCategory;
    private String facultyName;
    private String trainingMode;
    private LocalDateTime startedDate;
}
