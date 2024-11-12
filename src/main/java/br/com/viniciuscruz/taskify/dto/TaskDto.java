package br.com.viniciuscruz.taskify.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDto extends RepresentationModel<TaskDto> {
    private long id;
    private String title;
    private String description;
}
