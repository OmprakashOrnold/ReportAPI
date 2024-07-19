package com.pt.reports.service;

import com.pt.reports.entities.CourseDetails;
import com.pt.reports.jasper.JasperReportService;
import com.pt.reports.model.SearchRequest;
import com.pt.reports.model.SearchResponse;
import com.pt.reports.repository.CourseDetailsRepo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Om Prakash Peddamadthala
 * @version 1.0
 * @since 7/14/2024
 */
@Service
@RequiredArgsConstructor
public class CourseDetailsServiceImpl implements CourseDetailsService {

    private final CourseDetailsRepo courseDetailsRepository;

    private final JasperReportService jasperReportService;

    @Override
    public Set<String> showAllCourseNames() {
        return courseDetailsRepository.getUniqueCourseNames();
    }

    @Override
    public Set<String> showAllFaculties() {
        return courseDetailsRepository.getUniqueFacultyNames();
    }

    @Override
    public Set<String> showAllTrainingModes() {
        return courseDetailsRepository.getUniqueTrainingModes();
    }

    @Override
    public List<SearchResponse> searchCoursesByFilters(SearchRequest searchRequest) {

        CourseDetails courseDetails = new CourseDetails();
        if (StringUtils.hasLength( searchRequest.getCourseCategory() )) {
            courseDetails.setCourseCategory( searchRequest.getCourseCategory() );
        }
        if (StringUtils.hasLength( searchRequest.getFacultyName() )) {
            courseDetails.setFacultyName( searchRequest.getFacultyName() );
        }
        if (StringUtils.hasLength( searchRequest.getTrainingMode() )) {
            courseDetails.setTrainingMode( searchRequest.getTrainingMode() );
        }
        Example<CourseDetails> exampleEntity = Example.of( courseDetails );
        List<CourseDetails> courseDetailList = courseDetailsRepository.findAll( exampleEntity );
        List<SearchResponse> searchResponseList = new ArrayList<>();
        courseDetailList.forEach( courseDetail -> {
            SearchResponse searchResponse = new SearchResponse();
            BeanUtils.copyProperties( courseDetail, searchResponse );
            searchResponseList.add( searchResponse );
        } );
        return searchResponseList;
    }

    @Override
    public void  generateReportToDownload(SearchRequest searchRequest, String format, HttpServletResponse httpServletResponse) throws  Exception {
        String reportName = "item-report";
        List<SearchResponse> searchResponses= searchCoursesByFilters( searchRequest );
        jasperReportService.generateReport(searchResponses, format, reportName ,httpServletResponse);
    }

}
