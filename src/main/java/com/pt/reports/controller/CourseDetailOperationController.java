package com.pt.reports.controller;

import com.pt.reports.model.SearchRequest;
import com.pt.reports.model.SearchResponse;
import com.pt.reports.service.CourseDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Om Prakash Peddamadthala
 * @version 1.0
 * @since 7/14/2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/repoting/api")
public class CourseDetailOperationController {

    private final CourseDetailsService courseDetailsService ;

    @GetMapping("/courses")
    public ResponseEntity<?> fetchAllCourses(){
        try{
            return ResponseEntity.ok(courseDetailsService.showAllCourseNames());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/faculties")
    public ResponseEntity<?> fetchAllFaculties(){
        try{
            return ResponseEntity.ok(courseDetailsService.showAllFaculties());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/training-modes")
    public ResponseEntity<?> fetchAllTrainingModes(){
        try{
            return ResponseEntity.ok(courseDetailsService.showAllTrainingModes());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchCoursesByFilter(@RequestBody SearchRequest searchRequest){
        try{
            List<SearchResponse> searchResponseList=courseDetailsService.searchCoursesByFilters(searchRequest);
            return ResponseEntity.ok(searchResponseList);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("/download-pdf")
    public ResponseEntity<?> generatePdfReport(@RequestBody SearchRequest searchRequest) {
        try {
            byte[] reportContent = courseDetailsService.generatePdfReport(searchRequest);
            ByteArrayResource resource = new ByteArrayResource( reportContent );
            return ResponseEntity.ok()
                    .contentType( MediaType.APPLICATION_OCTET_STREAM )
                    .contentLength( resource.contentLength() )
                    .header( HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename( "item-report." + "pdf" )
                                    .build().toString() )
                    .body( resource );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body( e.getMessage() );
        }
    }

    @PostMapping("/download-xls")
    public ResponseEntity<?> generateExcelReport(@RequestBody SearchRequest searchRequest) {
        try {
            byte[] reportContent = courseDetailsService.generatePdfReport(searchRequest);
            ByteArrayResource resource = new ByteArrayResource( reportContent );
            return ResponseEntity.ok()
                    .contentType( MediaType.APPLICATION_OCTET_STREAM )
                    .contentLength( resource.contentLength() )
                    .header( HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename( "item-report." + "xls" )
                                    .build().toString() )
                    .body( resource );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body( e.getMessage() );
        }
    }

}
