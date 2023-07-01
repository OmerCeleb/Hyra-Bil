package com.saferent1.repository;

import com.saferent1.domain.Car;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /// !11 JPQL
    @Query("SELECT count (*) from Car c join c.image img where img.id=:id")
    Integer findCarCountByImageId(@Param("id") String id);

    @EntityGraph(attributePaths = {"image"}) // har gjorts EAGER
    List<Car> findAll();

}
