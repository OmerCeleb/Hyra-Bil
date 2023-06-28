package com.saferent1.repository;

import com.saferent1.domain.ImageFile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, String> {


    @EntityGraph(attributePaths = "id")
    List<ImageFile> findAll();


}
