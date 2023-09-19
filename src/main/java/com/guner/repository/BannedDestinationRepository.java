package com.guner.repository;

import com.guner.entity.BannedDestination;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannedDestinationRepository extends CrudRepository<BannedDestination, Long> {

    List<BannedDestination> findByGsmNo(String gsmNo);

    void deleteByGsmNo(String gsmNo);

    boolean existsByGsmNo(String gsmNo);

}