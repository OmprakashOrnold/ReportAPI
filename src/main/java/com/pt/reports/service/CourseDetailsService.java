package com.pt.reports.service;

import com.pt.reports.model.SearchRequest;
import com.pt.reports.model.SearchResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Set;

/**
 * @author Om Prakash Peddamadthala
 * @version 1.0
 * @since 7/14/2024
 */
public interface CourseDetailsService {
    Set<String> showAllCourseNames();
    Set<String> showAllFaculties();
    Set<String> showAllTrainingModes();
    List<SearchResponse> searchCoursesByFilters(SearchRequest searchRequest);
    byte[] generatePdfReport(SearchRequest searchRequest) throws  Exception ;
    byte[] generateExcelReport(SearchRequest searchRequest)  throws  Exception ;


}
