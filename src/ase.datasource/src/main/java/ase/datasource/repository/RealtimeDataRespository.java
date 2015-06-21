package ase.datasource.repository;

import ase.datasource.model.StoredRealtimeData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 20.06.2015.
 */
public interface RealtimeDataRespository extends MongoRepository<StoredRealtimeData, String> {

    List<StoredRealtimeData> findByStation_Name(@Param("name") String name);

    StoredRealtimeData findByPlannedArrivalGreaterThan(@Param("date") Date date);

    @Query("{'train' : { 'number' : ?0}}")
    List<StoredRealtimeData> findByTrainNumberOrderByCurrentTime(@Param("number") int number);

    //@Query("{'station' : { 'name' : ?, 'number' : ?0}}")
    List<StoredRealtimeData> findByStation_Number(@Param("number") int number);
}
