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

        SpecificClass specificClass = specificClassService.getSpecificClass(specificClassId);

        if (teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId) != null){
            throw new SCSException(HttpStatus.BAD_REQUEST, "Teaching info with id " + teachingInfoId + " already exists.");
        }

        //check that if an instructor is already teaching a class then block creation
        for (TeachingInfo teachingInfo : teachingInfoRepository.findAll()) {
            if (teachingInfo.getInstructor().getAccountId() == accountId && teachingInfo.getSpecificClass().getClassId() == specificClassId) {
                throw new SCSException(HttpStatus.BAD_REQUEST, "Instructor with id " + accountId + " is already teaching class with id " + specificClassId);
            }
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

        Instructor instructor = instructorService.getInstructorById(accountId);

        SpecificClass specificClass = specificClassService.getSpecificClass(specificClassId);

        //if the instructor is already teaching the class then block update
        for (TeachingInfo teachingInfo1 : teachingInfoRepository.findAll()) {
            if (teachingInfo1.getInstructor().getAccountId() == accountId && teachingInfo1.getSpecificClass().getClassId() == specificClassId) {
                throw new SCSException(HttpStatus.BAD_REQUEST, "Instructor with id " + accountId + " is already teaching class with id " + specificClassId);
            }
        }

        teachingInfo.setInstructor(instructor);
        teachingInfo.setSpecificClass(specificClass);
        teachingInfoRepository.save(teachingInfo);
        return teachingInfo;
    }

    @Transactional
    public void deleteTeachingInfo(Integer teachingInfoId) {
        TeachingInfo teachingInfo = teachingInfoRepository.findTeachingInfoByTeachingInfoId(teachingInfoId);
        teachingInfoRepository.delete(teachingInfo);
    }

    @Transactional
    public void deleteAllTeachingInfos() {
        teachingInfoRepository.deleteAll();
    }
}