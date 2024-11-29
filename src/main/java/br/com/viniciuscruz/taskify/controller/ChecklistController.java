package br.com.viniciuscruz.taskify.controller;

import br.com.viniciuscruz.taskify.dto.ChecklistDto;
import br.com.viniciuscruz.taskify.exception.CustomExceptionResponse;
import br.com.viniciuscruz.taskify.service.ChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checklists")
@Tag(name = "Checklists", description = "Here are the endpoints to manage checklists")
public class ChecklistController {

    @Autowired
    private ChecklistService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Persist a new checklist in database.", tags = {"Checklists"}, responses =
            { @ApiResponse(description = "Success", responseCode = "200", content = { @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChecklistDto.class)
            )}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = { @Content }),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = { @Content }),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = { @Content })
            }
    )
    public ResponseEntity<ChecklistDto> create(@RequestBody ChecklistDto checklistDto) {
        ChecklistDto checklist = service.create(checklistDto);
        this.buildSelfLink(checklist);
        return new ResponseEntity<>(checklist, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a checklist by id", tags = {"Checklists"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChecklistDto.class))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = { @Content }),
            @ApiResponse(description = "Resource Not Found", responseCode = "404", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema( implementation = CustomExceptionResponse.class))
            }),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = { @Content })
    })
    public ResponseEntity<ChecklistDto> findById(@PathVariable(name = "id") long id) {
        ChecklistDto checklist = service.findById(id);
        this.buildSelfLink(checklist);
        return new ResponseEntity<>(checklist, HttpStatus.OK);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existing checklist.", tags = {"Checklists"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChecklistDto.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<ChecklistDto> update(@RequestBody ChecklistDto checklistDto) {
        ChecklistDto checklist = service.update(checklistDto);
        this.buildSelfLink(checklist);
        return new ResponseEntity<>(checklist, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a checklist by ID.", tags = {"Checklists"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Resource Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity delete(@PathVariable(name = "id") long id) {
        service.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve all checklists.", tags = {"Checklists"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChecklistDto.class))),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<PagedModel<ChecklistDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<ChecklistDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title", "id"));
        Page<ChecklistDto> checklists = service.findAll(pageable);

        PagedModel<ChecklistDto> pagedChecklists = assembler.toModel(checklists, checklistDto -> {
            buildSelfLink(checklistDto);
            return checklistDto;
        });

        return new ResponseEntity<>(pagedChecklists, HttpStatus.OK);
    }

    @GetMapping("/find/task/{taskId}")
    @Operation(
            summary = "Retrieve all checklists by task ID.",
            description = "This endpoint retrieves all checklists associated with a given task ID, supporting pagination and sorting.",
            tags = {"Checklists"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ChecklistDto.class)
                            )
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<PagedModel<ChecklistDto>> findByTaskId(
            @PathVariable(name = "taskId") long taskId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<ChecklistDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title", "id"));
        Page<ChecklistDto> checklists = service.findByTaskId(pageable, taskId);

        PagedModel<ChecklistDto> pagedChecklists = assembler.toModel(checklists, checklistDto -> {
            buildSelfLink(checklistDto);
            return checklistDto;
        });

        return new ResponseEntity<>(pagedChecklists, HttpStatus.OK);
    }

    private void buildSelfLink(ChecklistDto checklistDto) {
        checklistDto.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()
                        ).findById(checklistDto.getId())
                ).withSelfRel()
        );
    }

    private void buildCollectionLink(CollectionModel<ChecklistDto> checklists) {
        checklists.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()
                        ).findAll(0, 10, "asc", null)
                ).withRel(LinkRelation.of("COLLECTION"))
        );
    }
}
