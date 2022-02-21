package pl.daniel.kolban.tankfleetmanagement.tank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.daniel.kolban.tankfleetmanagement.exception.domain.PresidentNotFoundException;
import pl.daniel.kolban.tankfleetmanagement.exception.domain.TankNotFoundException;
import pl.daniel.kolban.tankfleetmanagement.exception.domain.WrongArgumentException;
import pl.daniel.kolban.tankfleetmanagement.user.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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

    public Tank save(Tank tank) throws WrongArgumentException {
        validateTankData(tank);
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

    private void validateTankData(Tank tank) throws WrongArgumentException {
        if (tank.getYearOfProduction() < 1900 || tank.getYearOfProduction() > LocalDateTime.now().getYear()) {
            throw new WrongArgumentException("Year of production must be between 1900 and current");
        } else if (tank.getAmmunition() < 0) {
            throw new WrongArgumentException("Cannot be negative number");
        } else if (tank.getMileage() < 0) {
            throw new WrongArgumentException("Cannot be negative number");
        } else if (tank.getSideArmor() < 0) {
            throw new WrongArgumentException("Cannot be negative number");
        } else if (tank.getFrontArmor() < 0) {
            throw new WrongArgumentException("Cannot be negative number");
        } else if (tank.getBackArmor() < 0) {
            throw new WrongArgumentException("Cannot be negative number");
        }
    }
}
