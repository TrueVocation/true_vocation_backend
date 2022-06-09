package com.truevocation.web.rest;

import com.truevocation.repository.QuestionRepository;
import com.truevocation.service.QuestionService;
import com.truevocation.service.dto.QuestionDTO;
import com.truevocation.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;


/**
 * REST controller for managing {@link com.truevocation.domain.Question}.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "question";

    private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionService questionService;

    private final QuestionRepository questionRepository;

    public QuestionResource(QuestionService questionService, QuestionRepository questionRepository) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
    }

    /**
     * {@code POST  /questions} : Create a new question.
     *
     * @param questionDTO the questionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionDTO, or with status {@code 400 (Bad Request)} if the question has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questions")
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to save Question : {}", questionDTO);
        if (questionDTO.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionDTO result = questionService.save(questionDTO);
        return ResponseEntity
            .created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questions/:id} : Updates an existing question.
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questions/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Question : {}, {}", id, questionDTO);
        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionDTO result = questionService.save(questionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /questions/:id} : Partial updates given fields of an existing question, field will ignore if it is null
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionDTO> partialUpdateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Question partially : {}, {}", id, questionDTO);
        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionDTO> result = questionService.partialUpdate(questionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /questions} : get all the questions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("answeruser-is-null".equals(filter)) {
            log.debug("REST request to get all Questions where answerUser is null");
            return new ResponseEntity<>(questionService.findAllWhereAnswerUserIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Questions");
        Page<QuestionDTO> page;
        if (eagerload) {
            page = questionService.findAllWithEagerRelationships(pageable);
        } else {
            page = questionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @GetMapping("/test-questions/{testId}")
    public ResponseEntity<Page<QuestionDTO>> getAllQuestionsByTestId(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload,
        @PathVariable Long testId
    ) {
        Page<QuestionDTO> page;
        if (eagerload) {
            page = questionService.findAllWithEagerRelationships(pageable,testId);
        } else {
            page = questionService.findAll(pageable);
        }
//        int totalCount = questionService.findAllCountByTestId(testId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HEADER_X_TOTAL_COUNT, String.valueOf(totalCount));
//        StringBuilder link = new StringBuilder();
//        if (page.getNumber() < (page.getTotalPages() - 1)) {
//            link.append(page.getNumber()+1);
//            headers.add(HttpHeaders.LINK, link.toString());
//        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        headers.add("pagination", String.valueOf(page));
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/test-questions-list/{id}")
    public ResponseEntity<List<QuestionDTO>> getAllQuestionsListByTestId(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(questionService.findAllByProfTestId(id));
    }

    /**
     * {@code GET  /questions/:id} : get the "id" question.
     *
     * @param id the id of the questionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questions/{id}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);
        Optional<QuestionDTO> questionDTO = questionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionDTO);
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" question.
     *
     * @param id the id of the questionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);
        questionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
