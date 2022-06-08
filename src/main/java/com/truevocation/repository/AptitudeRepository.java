package com.truevocation.repository;

import com.truevocation.domain.Aptitude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
public interface AptitudeRepository extends JpaRepository<Aptitude, Long> {

    Aptitude findAptitudeByCode(Integer code);
}
