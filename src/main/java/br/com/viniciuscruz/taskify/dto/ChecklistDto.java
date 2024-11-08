package br.com.viniciuscruz.taskify.dto;

import br.com.viniciuscruz.taskify.model.TaskModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChecklistDto {
    private long id;
    private String title;
    private String description;

    private Boolean isChecked;
    private Date checkedAt;
    private TaskModel task;
}
