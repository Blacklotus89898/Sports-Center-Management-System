package ca.mcgill.ecse321.scs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.TeachingInfoRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Instructor;
import ca.mcgill.ecse321.scs.model.SpecificClass;
import ca.mcgill.ecse321.scs.model.TeachingInfo;

/**
 * The TeachingInfoService class provides methods for managing teaching info.
 * It interacts with the TeachingInfoRepository, InstructorService, and SpecificClassService
 * to perform CRUD operations on teaching info.
 */
@Service
public class TeachingInfoService {
    @Autowired
    private TeachingInfoRepository teachingInfoRepository;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private SpecificClassService specificClassService;

    /**
     * Set the InstructorService
     * @param instructorService
     */
    public void setInstructorService(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    /**
     * Set the SpecificClassService
     * @param specificClassService
     */
    public void setSpecificClassService(SpecificClassService specificClassService) {
        this.specificClassService = specificClassService;
    }

    /**
     * Create a new teaching info with the specified instructor and specific class.
     * @param accountId
     * @param specificClassId
     * @return
     */
    @Transactional
    public TeachingInfo createTeachingInfo(int accountId, int specificClassId) {
        Instructor instructor = instructorService.getInstructorById(accountId);
        SpecificClass specificClass = specificClassService.getSpecificClass(specificClassId);

        //check that another teachingInfo for a class doesn't already exist
        for (TeachingInfo teachingInfo : teachingInfoRepository.findAll()) {
            if (teachingInfo.getSpecificClass().getClassId() == specificClassId) {
                throw new SCSException(HttpStatus.BAD_REQUEST, "There is already a instructor assigned to this class.");
            }
        }

        TeachingInfo teachingInfo = new TeachingInfo();
        teachingInfo.setInstructor(instructor);
        teachingInfo.setSpecificClass(specificClass);
        
        return teachingInfoRepository.save(teachingInfo);
    }

    /**
     * Get the teaching info with the specified ID.
     * @param teachingInfoId
     * @return
     */
    @Transactional
    public TeachingInfo getTeachingInfo(Integer teachingInfoId) {
        TeachingInfo teachingInfo = teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId);
        if (teachingInfo == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Teaching info with id " + teachingInfoId + " not found.");
        }
        return teachingInfo;
    }

    /**
     * Get all teaching infos.
     * @return
     */
    @Transactional
    public List<TeachingInfo> getAllTeachingInfos() {
        return ServiceUtils.toList(teachingInfoRepository.findAll());
    }

    /**
     * Update the teaching info with the specified ID.
     * @param teachingInfoId
     * @param accountId
     * @param specificClassId
     * @return
     */
    @Transactional
    public TeachingInfo updateTeachingInfo(Integer teachingInfoId, int accountId, int specificClassId) {
        TeachingInfo teachingInfo = teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId);
        if (teachingInfo == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Teaching info with id " + teachingInfoId + " not found.");
        }

        Instructor instructor = instructorService.getInstructorById(accountId);

        SpecificClass specificClass = specificClassService.getSpecificClass(specificClassId);

        teachingInfo.setInstructor(instructor);
        teachingInfo.setSpecificClass(specificClass);
        teachingInfoRepository.save(teachingInfo);
        return teachingInfo;
    }

    /**
     * Delete the teaching info with the specified ID.
     * @param teachingInfoId
     */
    @Transactional
    public void deleteTeachingInfo(Integer teachingInfoId) {
        TeachingInfo teachingInfo = teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId);
        if (teachingInfo == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Teaching info with id " + teachingInfoId + " not found.");
        }
        teachingInfoRepository.delete(teachingInfo);
    }

    /**
     * Delete all teaching infos.
     */
    @Transactional
    public void deleteAllTeachingInfos() {
        teachingInfoRepository.deleteAll();
    }

    //get the teaching info for a specific class given the class id
    @Transactional
    public TeachingInfo getTeachingInfoByClassId(int classId) {
        return teachingInfoRepository.findTeachingInfoByClassId(classId);
    }
}