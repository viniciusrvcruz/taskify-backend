package br.com.viniciuscruz.taskify.controller;

import br.com.viniciuscruz.taskify.dto.TaskDto;
import br.com.viniciuscruz.taskify.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        TaskDto model = service.create(taskDto);
        return new ResponseEntity<>(model, HttpStatus.OK);
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
    public ResponseEntity<List<TaskDto>> findAll(){
        var list = service.findAll();
        return new ResponseEntity<List<TaskDto>>(list, HttpStatus.OK);
    }

    @GetMapping("/find/title/{title}")
    @Operation(summary = "Find tasks by title.", tags = {"Tasks"},
            responses = {
                    @ApiResponse(description = "Tasks found successfully", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content)
            }
    )
    public ResponseEntity<List<TaskDto>> findByTitle(@PathVariable(name = "title") String title) {
        var tasks = service.findByTitle(title);
        return new ResponseEntity<List<TaskDto>>(tasks, HttpStatus.OK);
    }
}
