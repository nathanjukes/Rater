package Rater.Controllers;

import Rater.Models.API;
import Rater.Services.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/targets")
public class TargetController {
    private final APIService apiService;

    @Autowired
    public TargetController(APIService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(value = "/id/{id}", method = GET)
    public ResponseEntity<Optional<API>> getApi(@PathVariable UUID id) {
        return ResponseEntity.ok(apiService.getById(id));
    }

    @RequestMapping(value = "", method = GET)
    public ResponseEntity<Optional<List<API>>> getAPIs() {
        return ResponseEntity.ok(apiService.getAPIs());
    }

    @RequestMapping(value = "", method = POST)
    public ResponseEntity<String> createAPI(@RequestBody String name) {
        //apiService.createAPI(name);
        return ResponseEntity.ok("TEST");
    }
}
