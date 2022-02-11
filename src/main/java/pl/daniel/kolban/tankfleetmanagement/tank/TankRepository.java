package pl.daniel.kolban.tankfleetmanagement.tank;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TankRepository extends JpaRepository<Tank, Long> {
}
