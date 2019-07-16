package com.company.repository;

import com.company.model.Product_Hangar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Product_HangarRepository extends JpaRepository<Product_Hangar, Long> {

    @Query("SELECT p_h FROM Product_Hangar p_h WHERE p_h.hangar =?1")
    List<Product_Hangar> findAllByHangar(long hangar);

    @Query("SELECT p_h FROM Product_Hangar p_h WHERE p_h.product =?1")
    List<Product_Hangar> findAllByProduct(long product);

    /*@Query("SELECT p_h FROM Product_Hangar p_h WHERE p_h.product =?1 AND p_h.hangar =?2")
    boolean existsByProductAndHangar(long product, long hangar);*/

    @Query("SELECT p_h FROM Product_Hangar p_h WHERE p_h.product =?1 AND p_h.hangar =?2")
    Product_Hangar findByProductAndHangar(long product, long hangar);

}
