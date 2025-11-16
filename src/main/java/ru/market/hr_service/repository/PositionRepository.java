package ru.market.hr_service.repository;

import lombok.NonNull;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.hr_service.model.entity.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
    @NonNull
    @Named("positionById")
    Position getReferenceById(@NonNull Integer id);
}
