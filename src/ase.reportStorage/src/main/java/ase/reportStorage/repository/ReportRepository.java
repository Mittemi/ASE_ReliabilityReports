package ase.reportStorage.repository;

import ase.reportStorage.model.Report;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Michael on 21.05.2015.
 */
public interface ReportRepository extends PagingAndSortingRepository<Report, String> {

}
