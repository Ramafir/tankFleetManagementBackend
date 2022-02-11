package pl.daniel.kolban.tankfleetmanagement.tank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.daniel.kolban.tankfleetmanagement.user.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tanks")
public class TankResource {
    private TankService tankService;

    public TankResource(TankService tankService) {
        this.tankService = tankService;
    }

    @GetMapping("")
    public ResponseEntity<List<Tank>> getTanks() {
        List<Tank> tanks = tankService.getAllTanks();
        return new ResponseEntity<>(tanks, HttpStatus.OK);
    }

    @GetMapping("/{id}/president")
    public ResponseEntity<User> getTankPresident(@PathVariable("id") Long id) {
        User president = tankService.getPresident(id);
        return new ResponseEntity<>(president, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Tank> saveTank(@RequestBody @Valid Tank tank) {
        Tank newTank = tankService.save(tank);
        return new ResponseEntity<>(newTank, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tank> updateTank(@PathVariable Long id, @RequestBody @Valid Tank tank) {
        if(!id.equals(tank.getId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The updated object must have an id that matches the id in the resource path");
        Tank updateTank = tankService.update(tank);
        return new ResponseEntity<>(updateTank, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tank> getTankById(@PathVariable("id") Long id) {
        Tank tank = tankService.findById(id);
        return new ResponseEntity<>(tank, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tank> deleteTank(@PathVariable("id") Long id) {
        tankService.deleteTank(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
