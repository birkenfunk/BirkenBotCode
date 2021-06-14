package de.birkenfunk.birkenbotcode.persistent.repos;

import de.birkenfunk.birkenbotcode.persistent.entity.RoleToName;
import de.birkenfunk.birkenbotcode.persistent.entity.RoleToNameId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleToNameRepo extends JpaRepository<RoleToName, RoleToNameId> {
}
