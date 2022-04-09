package account.controller;

import account.dto.LogDto;
import account.service.LogService;
import account.util.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SecurityController {

    @Autowired
    LogService logService;

    @Autowired
    MappingUtils mapper;

    @GetMapping("/security/events")
    public ResponseEntity<List<LogDto>> getLogs() {

        List<LogDto> logsDto = logService.getLogsOrderById().stream()
                .map(log -> mapper.convertEntityToDto(log, LogDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(logsDto, HttpStatus.OK);
    }
}
