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
    public ResponseEntity<List<ChecklistDto>> findAll(){
        var checklists = service.findAll();
        return new ResponseEntity<>(checklists, HttpStatus.OK);
    }
}
