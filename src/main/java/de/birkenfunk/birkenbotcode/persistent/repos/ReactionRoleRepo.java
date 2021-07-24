package de.birkenfunk.birkenbotcode.persistent.repos;

import de.birkenfunk.birkenbotcode.persistent.entity.ReactionRole;
import de.birkenfunk.birkenbotcode.persistent.entity.ReactionRoleID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRoleRepo extends JpaRepository<ReactionRole, ReactionRoleID> {
}
