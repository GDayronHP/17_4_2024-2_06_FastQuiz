package com.secure.fastquiz.repositories;

import com.secure.fastquiz.models.PublicacionCuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionCuestionarioRepository extends JpaRepository<PublicacionCuestionario, Long> {
}