package ase.datasource.repository;

import ase.datasource.model.StoredRealtimeData;
import ase.shared.Constants;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ConcurrentModificationException;
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

    List<StoredRealtimeData> findByStation_Number(@Param("number") int number);


    @Query("{'station.number' : ?0, 'train.number' : ?1, 'currentTime' : { '$gte' : ?2, '$lte' : ?3 }, 'trainInStation' : true}")
    List<StoredRealtimeData> findByStationTrainAndTW(@Param("sNumber") int sNumber, @Param("tNumber") int tNumber,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Param("tFrom") Date tFrom,
                                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Param("tTo") Date tTo);

    @Query("{'line.name' : ?0, 'direction.name' : ?1, 'station.number' : ?2, 'currentTime' : { '$gte' : ?3, '$lte' : ?4 }, 'trainInStation' : true}")
    List<StoredRealtimeData> findByStationAndTW(@Param("lineName") String lineName,
                                                @Param("direction") String direction,
                                                @Param("stationNumber") int stationNumber,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Param("tFrom") Date tFrom,
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Param("tTo") Date tTo);

    @Query("{'line.name' : ?0, 'direction.name' : ?1, 'train.number' : ?2, 'currentTime' : { '$gte' : ?3, '$lte' : ?4 }}")
    List<StoredRealtimeData> findByTrainAndTW(@Param("lineName") String lineName,
                                                       @Param("direction") String direction,
                                                       @Param("trainNumber") int trainNumber,
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Param("tFrom") Date tFrom,
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Param("tTo") Date tTo);
}
