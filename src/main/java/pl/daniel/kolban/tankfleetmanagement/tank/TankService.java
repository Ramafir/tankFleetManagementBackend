package pl.daniel.kolban.tankfleetmanagement.tank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.daniel.kolban.tankfleetmanagement.exception.domain.*;
import pl.daniel.kolban.tankfleetmanagement.user.Role;
import pl.daniel.kolban.tankfleetmanagement.user.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Slf4j
@Transactional
@Service
public class TankService {
    private TankRepository tankRepository;

    public TankService(TankRepository tankRepository) {
        this.tankRepository = tankRepository;
    }

    public List<Tank> getAllTanks() {
        log.info("Fetching all tanks");
        return tankRepository.findAll();
    }

    public Tank findById(Long id) {
        log.info("Fetching tank by id: {}", id);
        return tankRepository.findById(id).orElseThrow(TankNotFoundException::new);
    }

    public Tank save(Tank tank) {
        log.info("Saving new tank: {}", tank.getModel());
        tank.setCreationDate(LocalDateTime.now());
        return tankRepository.save(tank);
    }

    public Tank update(Tank tank) {
        log.info("Updating tank: {}", tank.getModel());
        tank.setUpdateDate(LocalDateTime.now());
        return tankRepository.save(tank);
    }

    public void deleteTank(Long tankId) {
        log.info("Deleting tank by ID: {}", tankId);
        tankRepository.deleteById(tankId);
    }

    public User getPresident(Long tankId) {
        return tankRepository.findById(tankId)
                .map(Tank::getUser)
                .orElseThrow(PresidentNotFoundException::new);
    }
}
