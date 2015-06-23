package ase.reportStorage.repository;

import ase.reportStorage.model.StoredReport;
import ase.reportStorage.model.StoredReportMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Michael on 23.06.2015.
 */
public interface ReportMetadataRepository extends MongoRepository<StoredReportMetadata, String> {


    @Query("{ '_id' : ?0}")
    StoredReportMetadata findById(@Param("id")String id);

    @Query("{ 'reportId' : ?0}")
    List<StoredReportMetadata> findByReportId(@Param("reportId")String reportId);
}
