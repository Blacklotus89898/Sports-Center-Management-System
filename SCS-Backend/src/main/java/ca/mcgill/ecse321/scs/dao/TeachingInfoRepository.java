package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.TeachingInfo;

/*
 * DAO (a database link) for crud operations for TeachingInfo.
 */
public interface TeachingInfoRepository extends CrudRepository<TeachingInfo, Integer>{

    public TeachingInfo findTeachingInfoByTeachingInfoId(Integer teachingInfoId);

    @Query("SELECT t FROM TeachingInfo t WHERE t.specificClass.classId = :classId")
    public TeachingInfo findTeachingInfoByClassId(Integer classId);
    
}