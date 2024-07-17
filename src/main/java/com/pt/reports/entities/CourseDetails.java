package com.pt.reports.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Om Prakash Peddamadthala
 * @version 1.0
 * @since 7/14/2024
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course_details")
public class CourseDetails {

    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 60)
    private String courseName;
    @Column(length = 30)
    private String location;
    @Column(length = 30)
    private String courseCategory;
    @Column(length = 30)
    private String facultyName;
    @Column(length = 30)
    private BigDecimal fee;
    @Column(length = 30)
    private String adminName;
    @Column(length = 30)
    private Long adminContact;
    @Column(length = 30)
    private String trainingMode;
    private LocalDateTime startDate;
    @Column(length = 10)
    private String courseStatus;
    @CreationTimestamp
    @Column(insertable = true, updatable = false)
    private LocalDateTime creationDate;
    @UpdateTimestamp
    @Column(insertable = false, updatable = true)
    private LocalDateTime updationDate;
    private String createdBy;
    private String updatedBy;

}