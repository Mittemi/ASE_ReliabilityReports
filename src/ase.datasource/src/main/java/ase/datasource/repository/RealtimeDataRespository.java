package ase.datasource.repository;

import ase.shared.model.simulation.RealtimeData;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Michael on 20.06.2015.
 */
public interface RealtimeDataRespository extends PagingAndSortingRepository<RealtimeData, String> {

    List<RealtimeData> findByStation_Name(@Param("name") String name);
}
