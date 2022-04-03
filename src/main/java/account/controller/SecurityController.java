package account.controller;

import account.entity.Log;
import account.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SecurityController {

    @Autowired
    LogService logService;

    @GetMapping("/security/events")
    public ResponseEntity<List<Log>> getLogs() {
        return new ResponseEntity<>(logService.getLogsOrderById(), HttpStatus.OK);
    }
}
