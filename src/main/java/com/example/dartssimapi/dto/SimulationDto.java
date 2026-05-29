package com.example.dartssimapi.dto;

import java.util.List;

/**
 * ダーツシミュレーションに関するリクエスト/レスポンスのデータ構造を1つにまとめたクラス
 */
public class SimulationDto {

    public enum GameRule {
        SOFT,
        HARD
    }

    public record DartSetting(
        double weight,
        double centerOfGravity,
        double dragCoefficient
    ) {}

    public record ThrowCondition(
        double speed,
        double angle,
        double releaseHeight,
        double releaseDistance
    ) {}

    public record SimulationRequest(
        GameRule gameRule,
        DartSetting dartSetting,
        ThrowCondition throwCondition
    ) {}

    public record TrajectoryPoint(
        double time,
        double x,
        double y,
        double z
    ) {}

    public record SimulationResponse(
        List<TrajectoryPoint> trajectory
    ) {}
}