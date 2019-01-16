package com.betvictor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betvictor.resource.TextResponse;

@Repository
public interface TextRepository extends JpaRepository<TextResponse, Long>{

}
