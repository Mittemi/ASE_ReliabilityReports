package ase.notify.repository;

import ase.notify.model.StoredNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Michael on 23.06.2015.
 */
public interface NotificationRepository extends MongoRepository<StoredNotification, String> {

    @Query("{'email' : ?0}")
    //{'email' : -1}
    List<StoredNotification> findByEmail(@Param("email")String email);
}
