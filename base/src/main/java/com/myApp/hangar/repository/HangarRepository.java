package com.myApp.hangar.repository;

import com.myApp.hangar.model.Hangar;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HangarRepository extends JpaRepository<Hangar, Long> {

    @Query("SELECT h FROM  Hangar h WHERE h.name = ?1 and h.address = ?2")
    Hangar findHangarByNameAndAddress(String name, String address);

    @Query(value = "SELECT h.id, h.name FROM Hangar h", nativeQuery = true)
    List<Object> getBasicHangars();

}
