package ase.reportStorage.repository;

import ase.reportStorage.model.StoredReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Michael on 21.05.2015.
 */
public interface ReportRepository extends MongoRepository<StoredReport, String> {

    @Query("{ '_id' : ?0}")
    StoredReport findById(@Param("id")String id);
}
