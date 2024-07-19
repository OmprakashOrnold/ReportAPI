package com.pt.reports.controller;

import com.pt.reports.model.SearchRequest;
import com.pt.reports.model.SearchResponse;
import com.pt.reports.service.CourseDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling course detail operations.
 *
 * @author Om Prakash Peddamadthala
 * @version 1.0
 * @since 7/14/2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/reporting/api")
public class CourseDetailOperationController {

    private static final Logger logger = LoggerFactory.getLogger(CourseDetailOperationController.class);

    private final CourseDetailsService courseDetailsService;

    @GetMapping("/courses")
    public ResponseEntity<?> fetchAllCourses() {
        try {
            logger.info("Fetching all course names");
            return ResponseEntity.ok(courseDetailsService.showAllCourseNames());
        } catch (Exception e) {
            logger.error("Error fetching all course names", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/faculties")
    public ResponseEntity<?> fetchAllFaculties() {
        try {
            logger.info("Fetching all faculties");
            return ResponseEntity.ok(courseDetailsService.showAllFaculties());
        } catch (Exception e) {
            logger.error("Error fetching all faculties", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/training-modes")
    public ResponseEntity<?> fetchAllTrainingModes() {
        try {
            logger.info("Fetching all training modes");
            return ResponseEntity.ok(courseDetailsService.showAllTrainingModes());
        } catch (Exception e) {
            logger.error("Error fetching all training modes", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchCoursesByFilter(@RequestBody SearchRequest searchRequest) {
        try {
            logger.info("Searching courses by filters: {}", searchRequest);
            List<SearchResponse> searchResponseList = courseDetailsService.searchCoursesByFilters(searchRequest);
            return ResponseEntity.ok(searchResponseList);
        } catch (Exception e) {
            logger.error("Error searching courses by filters", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/download")
    public void generatePdfReport(@RequestBody SearchRequest searchRequest, @RequestParam String downloadFormat, HttpServletResponse httpServletResponse) {
        try {
            logger.info("Generating report to download with format: {}", downloadFormat);
            courseDetailsService.generateReportToDownload(searchRequest, downloadFormat, httpServletResponse);
            logger.info("Report generated successfully with format: {}", downloadFormat);
        } catch (Exception e) {
            logger.error("Error generating report to download", e);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        logger.error("Unhandled exception occurred", e);
        return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
    }
}
