package com.example.dartssimapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class SimulationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double initialVelocity; // 初速 (km/h)
    private double angle; // 射出角度 (度)
    private double drag; // 空気抵抗係数

    private LocalDateTime createdAt; // 実行日時

    // JPAの仕様上、引数のない空のコンストラクタが必須です
    public SimulationRecord() {
    }

    // データ保存時に使うコンストラクタ
    public SimulationRecord(double initialVelocity, double angle, double drag) {
        this.initialVelocity = initialVelocity;
        this.angle = angle;
        this.drag = drag;
        this.createdAt = LocalDateTime.now();
    }

    // --- 以下、Getter ---
    public Long getId() {
        return id;
    }

    public double getInitialVelocity() {
        return initialVelocity;
    }

    public double getAngle() {
        return angle;
    }

    public double getDrag() {
        return drag;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}