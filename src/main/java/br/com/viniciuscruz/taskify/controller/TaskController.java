package br.com.viniciuscruz.taskify.controller;

import br.com.viniciuscruz.taskify.dto.TaskDto;
import br.com.viniciuscruz.taskify.service.TaskService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Endpoints for managing tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    @PostMapping
    @Operation(summary = "Create a new task.", tags = {"Tasks"},
            responses = {
                    @ApiResponse(description = "Task created successfully", responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content)
            }
    )
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto) {
        TaskDto task = service.create(taskDto);
        this.buildSelfLink(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a task by ID.", tags = {"Tasks"},
            responses = {
                    @ApiResponse(description = "Task retrieved successfully", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content)
            }
    )
    public ResponseEntity<TaskDto> findById(@PathVariable(name = "id") long id) {
        TaskDto task = service.findById(id);
        this.buildSelfLink(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update an existing task.", tags = {"Tasks"},
            responses = {
                    @ApiResponse(description = "Task updated successfully", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content)
            }
    )
    public ResponseEntity<TaskDto> update(@RequestBody TaskDto taskDto) {
        TaskDto task = service.update(taskDto);
        this.buildSelfLink(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task by ID.", tags = {"Tasks"},
            responses = {
                    @ApiResponse(description = "Task deleted successfully", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content)
            }
    )
    public ResponseEntity delete(@PathVariable(name = "id") long id) {
        service.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(summary = "Retrieve all tasks.", tags = {"Tasks"},
            responses = {
                    @ApiResponse(description = "Tasks retrieved successfully", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<PagedModel<TaskDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<TaskDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        Page<TaskDto> tasks = service.findAll(pageable);

        PagedModel<TaskDto> pagedTasks = assembler.toModel(tasks, taskDto -> {
            buildSelfLink(taskDto);
            return taskDto;
        });

        return new ResponseEntity<>(pagedTasks, HttpStatus.OK);
    }

    @GetMapping("/find/title/{title}")
    @Operation(summary = "Find tasks by title.", tags = {"Tasks"},
            responses = {
                    @ApiResponse(description = "Tasks found successfully", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content)
            }
    )
    public ResponseEntity<CollectionModel<TaskDto>> findByTitle(
            @PathVariable(name = "title") String title,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<TaskDto> assembler
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"));
        Page<TaskDto> tasks = service.findByTitle(title, pageable);

        PagedModel<TaskDto> pagedTasks = assembler.toModel(tasks, taskDto -> {
            buildSelfLink(taskDto);
            return taskDto;
        });

        return new ResponseEntity<>(pagedTasks, HttpStatus.OK);
    }

    private void buildSelfLink(TaskDto taskDto) {
        taskDto.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()
                        ).findById(taskDto.getId())
                ).withSelfRel()
        );
    }

    private void buildCollectionLink(CollectionModel<TaskDto> tasks) {
        tasks.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()
                        ).findAll(0, 10, "asc", null)
                ).withRel(LinkRelation.of("COLLECTION"))
        );
    }
}
