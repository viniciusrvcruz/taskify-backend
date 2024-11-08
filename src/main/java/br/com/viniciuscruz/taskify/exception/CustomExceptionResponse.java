package br.com.viniciuscruz.taskify.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;
}
