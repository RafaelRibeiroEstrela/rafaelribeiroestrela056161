package com.example.processoseletivoapi.repositories;

import com.example.processoseletivoapi.models.Regional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegionalRepository extends JpaRepository<Regional, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT r " +
            "FROM Regional r ")
    List<Regional> findAllLocked();
}
