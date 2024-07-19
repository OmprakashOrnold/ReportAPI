package com.pt.reports.service;

import com.pt.reports.entities.CourseDetails;
import com.pt.reports.jasper.JasperReportService;
import com.pt.reports.model.SearchRequest;
import com.pt.reports.model.SearchResponse;
import com.pt.reports.repository.CourseDetailsRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Service implementation for course details.
 *
 * @author Om Prakash Peddamadthala
 * @version 1.0
 * @since 7/14/2024
 */
@Service
@RequiredArgsConstructor
public class CourseDetailsServiceImpl implements CourseDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CourseDetailsServiceImpl.class);

    private final CourseDetailsRepo courseDetailsRepository;
    private final JasperReportService jasperReportService;

    @Override
    public Set<String> showAllCourseNames() {
        logger.info("Fetching all unique course names");
        Set<String> courseNames = courseDetailsRepository.getUniqueCourseNames();
        logger.debug("Unique course names fetched: {}", courseNames);
        return courseNames;
    }

    @Override
    public Set<String> showAllFaculties() {
        logger.info("Fetching all unique faculty names");
        Set<String> facultyNames = courseDetailsRepository.getUniqueFacultyNames();
        logger.debug("Unique faculty names fetched: {}", facultyNames);
        return facultyNames;
    }

    @Override
    public Set<String> showAllTrainingModes() {
        logger.info("Fetching all unique training modes");
        Set<String> trainingModes = courseDetailsRepository.getUniqueTrainingModes();
        logger.debug("Unique training modes fetched: {}", trainingModes);
        return trainingModes;
    }

    @Override
    public List<SearchResponse> searchCoursesByFilters(SearchRequest searchRequest) {
        logger.info("Searching courses by filters: {}", searchRequest);
        CourseDetails courseDetails = new CourseDetails();
        if (StringUtils.hasLength(searchRequest.getCourseCategory())) {
            courseDetails.setCourseCategory(searchRequest.getCourseCategory());
        }
        if (StringUtils.hasLength(searchRequest.getFacultyName())) {
            courseDetails.setFacultyName(searchRequest.getFacultyName());
        }
        if (StringUtils.hasLength(searchRequest.getTrainingMode())) {
            courseDetails.setTrainingMode(searchRequest.getTrainingMode());
        }
        Example<CourseDetails> exampleEntity = Example.of(courseDetails);
        List<CourseDetails> courseDetailList = courseDetailsRepository.findAll(exampleEntity);
        List<SearchResponse> searchResponseList = new ArrayList<>();
        courseDetailList.forEach(courseDetail -> {
            SearchResponse searchResponse = new SearchResponse();
            BeanUtils.copyProperties(courseDetail, searchResponse);
            searchResponseList.add(searchResponse);
        });
        logger.debug("Search results: {}", searchResponseList);
        return searchResponseList;
    }

    @Override
    public void generateReportToDownload(SearchRequest searchRequest, String format, HttpServletResponse httpServletResponse) throws Exception {
        logger.info("Generating report to download with format: {}", format);
        String reportName = "item-report";
        List<SearchResponse> searchResponses = searchCoursesByFilters(searchRequest);
        jasperReportService.generateReport(searchResponses, format, reportName, httpServletResponse);
        logger.info("Report generated successfully with format: {}", format);
    }
}
