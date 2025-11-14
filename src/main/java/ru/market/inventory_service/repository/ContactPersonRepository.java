package ru.market.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.inventory_service.model.entity.ContactPerson;

@Repository
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Integer> {
}
