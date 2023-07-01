package com.saferent1.repository;

import com.saferent1.domain.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /// !11 JPQL
    @Query("SELECT count (*) from Car c join c.image img where img.id=:id")
    Integer findCarCountByImageId(@Param("id") String id);

    @EntityGraph(attributePaths = {"image"})
        // har gjorts EAGER
    List<Car> findAll();

    @EntityGraph(attributePaths = {"image"})
        // har gjorts EAGER
    Page<Car> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"image"})
    Optional<Car> findCarById(Long id);


    @Query("SELECT c from Car c join c.image im where im.id=:id")
    List<Car> findCarsByImageId(@Param("id") String id);


}
