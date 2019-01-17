package com.betvictor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.betvictor.resource.TextResponse;

@Repository
public interface TextRepository extends JpaRepository<TextResponse, Long>{

	
	@Query("FROM TextResponse ORDER BY id DESC")
	List<TextResponse> findTop10ByOrderByIdDesc();
}
