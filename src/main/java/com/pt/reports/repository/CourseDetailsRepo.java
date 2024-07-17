package com.pt.reports.repository;

import com.pt.reports.entities.CourseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author Om Prakash Peddamadthala
 * @version 1.0
 * @since 7/14/2024
 */
@Repository
public interface CourseDetailsRepo extends JpaRepository<CourseDetails,Long> {

    @Query("select distinct(c.courseName) from CourseDetails c")
    Set<String> getUniqueCourseNames();

    @Query("select distinct(c.facultyName) from CourseDetails c")
    Set<String> getUniqueFacultyNames();

    @Query("select distinct(c.trainingMode) from CourseDetails c")
    Set<String> getUniqueTrainingModes();



}
