package pro.sky.sockswarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.sockswarehouse.model.SocksEntity;

import java.util.Optional;

@Repository
public interface SocksRepository extends JpaRepository<SocksEntity, Integer> {

    Optional<SocksEntity> findByColorIgnoreCaseAndCottonPart(String color, Integer cottonPart);

    @Query("SELECT SUM(s.quantity) FROM SocksEntity as s  WHERE s.color = :color AND s.cottonPart < :cottonPart")
    Optional<Integer> getAmountSocksCottonLessThen(@Param("color") String color, @Param("cottonPart") Integer cottonPart);

    @Query("SELECT SUM(s.quantity) FROM SocksEntity as s  WHERE s.color = :color AND s.cottonPart = :cottonPart")
    Optional<Integer> getAmountSocksCottonEqual(@Param("color") String color, @Param("cottonPart") Integer cottonPart);

    @Query("SELECT SUM(s.quantity) FROM SocksEntity as s  WHERE s.color = :color AND s.cottonPart > :cottonPart")
    Optional<Integer> getAmountSocksCottonMoreThen(@Param("color") String color, @Param("cottonPart") Integer cottonPart);
}
