package ca.mcgill.ecse321.scs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.InstructorRepository;
import ca.mcgill.ecse321.scs.dao.SpecificClassRepository;
import ca.mcgill.ecse321.scs.dao.TeachingInfoRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Instructor;
import ca.mcgill.ecse321.scs.model.SpecificClass;
import ca.mcgill.ecse321.scs.model.TeachingInfo;

@Service
public class TeachingInfoService {
    @Autowired
    private TeachingInfoRepository teachingInfoRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private SpecificClassRepository specificClassRepository;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private SpecificClassService specificClassService;


    public void setInstructorService(InstructorService instructorService) {
        this.instructorService = instructorService;
    }
    public void setSpecificClassService(SpecificClassService specificClassService) {
        this.specificClassService = specificClassService;
    }

    @Transactional
    public TeachingInfo createTeachingInfo(int accountId, int specificClassId, Integer teachingInfoId) {
        Instructor instructor = instructorService.getInstructorById(accountId);
        if (instructor == null){
            throw new SCSException(HttpStatus.NOT_FOUND, "Instructor with id " + accountId + " not found.");
        }

        SpecificClass specificClass = specificClassService.getSpecificClass(specificClassId);
        if (specificClass == null){
            throw new SCSException(HttpStatus.NOT_FOUND, "Specific class with id " + specificClassId + " not found.");
        }

        if (teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId) != null){
            throw new SCSException(HttpStatus.BAD_REQUEST, "Teaching info with id " + teachingInfoId + " already exists."); //is this check needed?
        }

        TeachingInfo teachingInfo = new TeachingInfo(teachingInfoId, instructor, specificClass);
        teachingInfoRepository.save(teachingInfo);
        return teachingInfo;
    }

    @Transactional
    public TeachingInfo getTeachingInfo(Integer teachingInfoId) {
        TeachingInfo teachingInfo = teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId);
        if (teachingInfo == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Teaching info with id " + teachingInfoId + " not found.");
        }
        return teachingInfo;
    }

    @Transactional
    public List<TeachingInfo> getAllTeachingInfos() {
        return ServiceUtils.toList(teachingInfoRepository.findAll());
    }

    @Transactional
    public TeachingInfo updateTeachingInfo(Integer teachingInfoId, int accountId, int specificClassId) {
        TeachingInfo teachingInfo = teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId);
        if (teachingInfo == null){
            throw new SCSException(HttpStatus.NOT_FOUND, "Teaching info with id " + teachingInfoId + " not found.");
        }
        Instructor instructor = instructorService.getInstructorById(accountId);
        if (instructor == null){
            throw new SCSException(HttpStatus.NOT_FOUND, "Instructor with id " + accountId + " not found.");
        }

        SpecificClass specificClass = specificClassService.getSpecificClass(specificClassId);
        if (specificClass == null){
            throw new SCSException(HttpStatus.NOT_FOUND, "Specific class with id " + specificClassId + " not found.");
        }


        teachingInfo.setInstructor(instructor);
        teachingInfo.setSpecificClass(specificClass);
        teachingInfoRepository.save(teachingInfo);
        return teachingInfo;
    }

    @Transactional
    public void deleteTeachingInfo(Integer teachingInfoId) {
        TeachingInfo teachingInfo = teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId);
        if (teachingInfo == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Teaching info with id " + teachingInfoId + " not found.");
        }
        teachingInfoRepository.delete(teachingInfo);
    }

    @Transactional
    public void deleteAllTeachingInfos() {
        teachingInfoRepository.deleteAll();
    }
}