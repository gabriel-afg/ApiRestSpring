package com.apiRest.springbootApi.remedios;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RemedioRepository extends JpaRepository<Remedio, Long> {
}