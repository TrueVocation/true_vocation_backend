package com.truevocation.service;

import com.truevocation.service.dto.AptitudeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface AptitudeService {
    AptitudeDTO save(AptitudeDTO aptitudeDTO);

    Optional<AptitudeDTO> partialUpdate(AptitudeDTO aptitudeDTO);

    Page<AptitudeDTO> findAll(Pageable pageable);

    Optional<AptitudeDTO> findOne(Long id);

    void delete(Long id);
}
