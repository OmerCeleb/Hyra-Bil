package com.saferent1.repository;

import ch.qos.logback.core.read.ListAppender;
import com.saferent1.domain.Reservation;
import com.saferent1.domain.User;
import com.saferent1.domain.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH Car c on r.car=c.id WHERE " +
            "c.id=:carId and (r.status not in :status) and : pickUpTime BETWEEN r.pickUpTime and r.dropOfTime " +
            "or " +
            "c.id=:carId and (r.status not in :status) and : dropOfTime BETWEEN r.pickUpTime and r.dropOfTime " +
            "or " +
            "c.id=:carId and (r.status not in :status) and (r.pickUpTime BETWEEN :pickUpTime and :dropOfTime)")
    List<Reservation> checkCarStatus(@Param("carId") Long carId,
                                     @Param("pickUpTime") LocalDateTime pickUpTime,
                                     @Param("dropOfTime") LocalDateTime dropOfTime,
                                     @Param("status") ReservationStatus[] status);


    @EntityGraph(attributePaths = {"car", "car.image"})
    List<Reservation> findAll();

    @EntityGraph(attributePaths = {"car", "car.image"})
    Page<Reservation> findAll(Pageable page);

    @EntityGraph(attributePaths = {"car", "car.image", "user"})
    Optional<Reservation> findById(Long id);

    @EntityGraph(attributePaths = {"car", "car.image", "user"})
    Page<Reservation> findAllByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"car", "car.image", "user"})
    Optional<Reservation> finByIdAndUser(Long id, User user);
}
