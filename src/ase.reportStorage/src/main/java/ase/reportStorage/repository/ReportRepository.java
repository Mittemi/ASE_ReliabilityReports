package ase.reportStorage.repository;

import ase.reportStorage.model.StoredReport;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Michael on 21.05.2015.
 */
public interface ReportRepository extends PagingAndSortingRepository<StoredReport, String> {

    public StoredReport findById(@Param("id")String id);
}
