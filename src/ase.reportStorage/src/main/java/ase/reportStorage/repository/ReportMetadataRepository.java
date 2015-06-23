package ase.reportStorage.repository;

import ase.reportStorage.model.StoredReport;
import ase.reportStorage.model.StoredReportMetadata;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Michael on 23.06.2015.
 */
public interface ReportMetadataRepository extends PagingAndSortingRepository<StoredReportMetadata, String> {

    public StoredReportMetadata findById(@Param("id")String id);
}
