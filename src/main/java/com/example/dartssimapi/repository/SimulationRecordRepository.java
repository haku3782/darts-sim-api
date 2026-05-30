package com.example.dartssimapi.repository;

import com.example.dartssimapi.entity.SimulationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationRecordRepository extends JpaRepository<SimulationRecord, Long> {
    // 中身は空っぽでOKです！
    // JpaRepositoryを継承するだけで、基本的なSQLが自動生成されます。
}