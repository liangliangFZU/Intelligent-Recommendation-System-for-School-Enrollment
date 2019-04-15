package com.example.login.dao;

import com.example.login.domain.Category;
import com.example.login.domain.RecInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecInfoRepository extends JpaRepository<RecInfo,Long> {
    public List<RecInfo> findByCategory(String category);
}
