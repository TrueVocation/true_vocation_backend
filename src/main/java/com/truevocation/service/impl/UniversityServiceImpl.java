package com.truevocation.service.impl;

import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.truevocation.domain.University;
import com.truevocation.repository.UniversityRepository;
import com.truevocation.service.SpecialtyService;
import com.truevocation.service.UniversityService;
import com.truevocation.service.dto.UniversityDTO;
import com.truevocation.service.dto.UniversityFilterDto;
import com.truevocation.service.mapper.UniversityMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link University}.
 */
@Service
@Transactional
public class UniversityServiceImpl implements UniversityService {

    private final Logger log = LoggerFactory.getLogger(UniversityServiceImpl.class);

    private final UniversityRepository universityRepository;

    private final UniversityMapper universityMapper;

    private final SpecialtyService specialtyService;

    @Value("${file.university.viewPath}")
    private String viewPath;

    @Value("${file.university.uploadPath}")
    private String uploadPath;

    @Value("${file.university.viewPathPicture}")
    private String viewPathPicture;

    @Value("${file.university.uploadPathPicture}")
    private String uploadPathPicture;

    @Value("${file.university.defaultPicture}")
    private String defaultPicture;

    public UniversityServiceImpl(UniversityRepository universityRepository, UniversityMapper universityMapper, SpecialtyService specialtyService) {
        this.universityRepository = universityRepository;
        this.universityMapper = universityMapper;
        this.specialtyService = specialtyService;
    }

    @Override
    public UniversityDTO save(UniversityDTO universityDTO) {
        log.debug("Request to save University : {}", universityDTO);
        University university = universityMapper.toEntity(universityDTO);
        university = universityRepository.save(university);
        return universityMapper.toDto(university);
    }

    @Override
    public Optional<UniversityDTO> partialUpdate(UniversityDTO universityDTO) {
        log.debug("Request to partially update University : {}", universityDTO);

        return universityRepository
            .findById(universityDTO.getId())
            .map(existingUniversity -> {
                universityMapper.partialUpdate(existingUniversity, universityDTO);

                return existingUniversity;
            })
            .map(universityRepository::save)
            .map(universityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniversityDTO> findAll(Pageable pageable) {
        Page<UniversityDTO> universityDTOS = universityRepository.findAll(pageable).map(universityMapper::toDto);
        for (UniversityDTO u : universityDTOS) {
            if (!u.getFaculties().isEmpty()) {
                u.setSpecialityCount(universityRepository.countAllSpecialityByUniversity(u.getId()));
                u.setAveragePrice(universityRepository.getAveragePrice(u.getId()));
            }
        }

        return universityDTOS;
    }

    public Page<UniversityDTO> findAllWithEagerRelationships(Pageable pageable) {
        Page<UniversityDTO> universityDTOS = universityRepository.findAllWithEagerRelationships(pageable).map(universityMapper::toDto);
        for (UniversityDTO u : universityDTOS) {
            u.setSpecialityCount(universityRepository.countAllSpecialityByUniversity(u.getId()));
            u.setAveragePrice(universityRepository.getAveragePrice(u.getId()));
        }

        return universityDTOS;
    }

    @Override
    public List<UniversityDTO> findAllByFilter(UniversityFilterDto universityFilterDto) {
        List<UniversityDTO> universityDTOS = universityRepository.findAll().stream().map(universityMapper::toDto).collect(Collectors.toList());
        universityDTOS = universityDTOS.stream().filter(university -> {
                if(Objects.isNull(universityFilterDto.getCityId()) || Objects.isNull(university.getCity())){
                    return true;
                }
                return Objects.equals(universityFilterDto.getCityId(), university.getCity().getId());
            }).collect(Collectors.toList());
        universityDTOS = universityDTOS.stream().filter(university -> {
                if(CollectionUtils.isEmpty(universityFilterDto.getStatuses())){
                    return true;
                }if(Objects.isNull(universityFilterDto.getStatuses().get(0)) && Objects.isNull(universityFilterDto.getStatuses().get(1))){
                    return true;
            }
                return universityFilterDto.getStatuses().contains(university.getStatus());
            }).collect(Collectors.toList());

        if(universityFilterDto.getMilitary()) {
            universityDTOS = universityDTOS.stream().filter(university -> Objects.equals(universityFilterDto.getMilitary(), university.getMilitary())).collect(Collectors.toList());
        }
        if(universityFilterDto.getDormitory()){
            universityDTOS = universityDTOS.stream().filter(university -> Objects.equals(universityFilterDto.getDormitory(), university.getDormitory())).collect(Collectors.toList());
        }
        universityDTOS = universityDTOS.stream().filter(university -> {
                int averagePrice = universityRepository.getAveragePrice(university.getId());
                if(CollectionUtils.isEmpty(universityFilterDto.getAveragePriceList())){
                    return true;
                }
                for(UniversityFilterDto.Price price : universityFilterDto.getAveragePriceList()){
                    if(!Objects.isNull(price) && averagePrice > price.getFrom() && averagePrice < price.getTo()){
                        return true;
                    }
                }
                return true;
            }).collect(Collectors.toList());
        universityDTOS = universityDTOS.stream().filter(university -> university.getName().contains(universityFilterDto.getSearch()))
            .collect(Collectors.toList());
        for (UniversityDTO u : universityDTOS) {
            u.setSpecialityCount(universityRepository.countAllSpecialityByUniversity(u.getId()));
            u.setAveragePrice(universityRepository.getAveragePrice(u.getId()));
        }

        return universityDTOS;
    }

    @Override
    public Page<UniversityDTO> findAllBySpeciality(Pageable pageable, Long id) {
        return universityRepository.findAllBySpecialityId(pageable, id)
            .map(universityMapper::toDto)
            .map(universityDTO -> {
                    universityDTO.setAveragePrice(universityRepository.getAveragePrice(universityDTO.getId()));
                    return universityDTO;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UniversityDTO> findOne(Long id) {
        log.debug("Request to get University : {}", id);
        return universityRepository.findOneWithEagerRelationships(id).map(universityMapper::toDto)
            .map(universityDTO -> {
                    universityDTO.setAveragePrice(universityRepository.getAveragePrice(universityDTO.getId()));
                    return universityDTO;
                });
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete University : {}", id);
        universityRepository.deleteById(id);
    }

    @Override
    public UniversityDTO saveLogo(MultipartFile file, Long universityId, boolean isLogo) {
        if (Objects.equals(file.getContentType(), "image/jpeg") || Objects.equals(file.getContentType(), "image/png")) {
            String extension;
            if ("image/jpeg".equals(file.getContentType())) {
                extension = ".jpg";
            } else {
                extension = ".png";
            }
            try {
                UniversityDTO universityDTO = this.findOne(universityId).orElse(null);
                if (Objects.isNull(universityDTO)) {
                    throw new EntityNotFoundException("University entity not found");
                }

                String picName = UUID.randomUUID().toString();

                byte[] bytes = file.getBytes();
                if(isLogo){
                    Path path = Paths.get(uploadPath + picName + extension);
                    Files.write(path, bytes);
                    universityDTO.setLogo(path.getFileName().toString());
                }else {
                    Path path = Paths.get(uploadPathPicture + picName + extension);
                    Files.write(path, bytes);
                    universityDTO.setPicture(path.getFileName().toString());
                }
                universityDTO = this.save(universityDTO);
                return universityDTO;
            } catch (Exception e) {
                log.error("Error when save picture");
                throw new InternalServerErrorException("Error when save picture");
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getLogoByUrl(String url) throws IOException {
        University university = universityRepository.findByLogoEquals(url);
        String extension = "";
        if (university.getLogo() != null && (university.getLogo().contains(".jpg") || university.getLogo().contains(".png"))) {
            String[] pictureSplit = university.getLogo().split("\\.");
            url = pictureSplit[0];
            extension = pictureSplit[1];
        }
        String pictureURL = viewPath + defaultPicture;
        if (url != null && !url.equals("null")) {
            pictureURL = viewPath + url + "." + extension;
        }

        InputStream in;
        try {
            in = new FileInputStream(pictureURL);
        } catch (Exception e) {
            in = new FileInputStream(viewPath + defaultPicture);
            extension = "png";
        }
        String contentType = defineContentType(extension);
        byte[] content = Base64.getEncoder().encode(IOUtils.toByteArray(in));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        return ResponseEntity.ok().headers(headers).body(content);
    }

    @Override
    public ResponseEntity<byte[]> getPictures(Long id) throws IOException {
        University university = universityRepository.getById(id);
        String extension = "";
        String url = "";
        if (university.getPicture() != null && (university.getPicture().contains(".jpg") || university.getPicture().contains(".png"))) {
            String[] pictureSplit = university.getPicture().split("\\.");
            url = pictureSplit[0];
            extension = pictureSplit[1];
        }
        String pictureURL = viewPath + defaultPicture;
        if (url != null && !url.equals("null")) {
            pictureURL = viewPathPicture + url + "." + extension;
        }

        InputStream in;
        try {
            in = new FileInputStream(pictureURL);
        } catch (Exception e) {
            in = new FileInputStream(viewPath + defaultPicture);
            extension = "png";
        }
        String contentType = defineContentType(extension);
        byte[] content = Base64.getEncoder().encode(IOUtils.toByteArray(in));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        return ResponseEntity.ok().headers(headers).body(content);
    }

    public String defineContentType(String extension) {
        if (extension.equals("jpg")) {
            return "image/jpeg";
        } else {
            return "image/png";
        }
    }
}
