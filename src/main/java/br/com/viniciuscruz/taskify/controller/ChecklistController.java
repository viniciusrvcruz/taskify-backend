package br.com.viniciuscruz.taskify.controller;

import br.com.viniciuscruz.taskify.dto.ChecklistDto;
import br.com.viniciuscruz.taskify.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checklists")
public class ChecklistController {

    @Autowired
    private ChecklistService service;

    @PostMapping
    public ResponseEntity<ChecklistDto> create(@RequestBody ChecklistDto checklistDto) {
        ChecklistDto checklist = service.create(checklistDto);
        return new ResponseEntity<>(checklist, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDto> findById(@PathVariable(name = "id") long id) {
        ChecklistDto checklist = service.findById(id);
        return new ResponseEntity<>(checklist, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ChecklistDto> update(@RequestBody ChecklistDto checklistDto) {
        ChecklistDto checklist = service.update(checklistDto);
        return new ResponseEntity<>(checklist, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") long id) {
        service.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<ChecklistDto>> findAll(){
        var checklists = service.findAll();
        return new ResponseEntity<>(checklists, HttpStatus.OK);
    }
}
