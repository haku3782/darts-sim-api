package com.example.dartssimapi.service;

import com.example.dartssimapi.dto.SimulationDto.SimulationRequest;
import com.example.dartssimapi.dto.SimulationDto.SimulationResponse;
import com.example.dartssimapi.dto.SimulationDto.TrajectoryPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimulationService {

    // 重力加速度 (m/s^2)
    private static final double GRAVITY = 9.80665;
    // タイムステップ (秒) - 0.005秒ごとに位置を計算
    private static final double TIME_STEP = 0.005;

    public SimulationResponse runSimulation(SimulationRequest request) {
        List<TrajectoryPoint> trajectory = new ArrayList<>();

        // パラメータの取得と単位変換
        double mass = request.dartSetting().weight() / 1000.0; // グラム(g) -> キログラム(kg)
        double dragCoeff = request.dartSetting().dragCoefficient();
        double cog = request.dartSetting().centerOfGravity(); // 将来的にClaudeで姿勢変化ロジックを入れる用のプレースホルダー

        // APIからはkm/hで受け取るため、物理演算用にm/sに変換 (1 km/h = 1/3.6 m/s)
        double speedKmh = request.throwCondition().speed();
        double speedMs = speedKmh / 3.6;
        double angleRad = Math.toRadians(request.throwCondition().angle()); // 度 -> ラジアン

        double targetX = request.throwCondition().releaseDistance();
        double startY = request.throwCondition().releaseHeight();

        // 初期状態の設定 (t = 0)
        double t = 0.0;
        double x = 0.0;
        double y = startY;
        double z = 0.0; // 左右のブレ（今回は常に0）

        // 初速の分解 (水平方向の速度 vx, 垂直方向の速度 vy)
        double vx = speedMs * Math.cos(angleRad);
        double vy = speedMs * Math.sin(angleRad);
        double vz = 0.0;

        // 初期位置を記録
        trajectory.add(new TrajectoryPoint(t, x, y, z));

        // ループ計算：ダーツが的に届く（x >= targetX）か、床に落ちる（y <= 0）まで繰り返す
        while (x < targetX && y > 0) {
            t += TIME_STEP;

            // 現在の合成速度 v を計算
            double v = Math.sqrt(vx * vx + vy * vy + vz * vz);

            // 空気抵抗による加速度の計算 (F = m*a -> a = F/m)
            // 速度の逆方向に比例する抵抗力（簡易モデル）
            double axDrag = -(dragCoeff * v * vx) / mass;
            double ayDrag = -(dragCoeff * v * vy) / mass;

            // 速度の更新 (加速度を時間で積分)
            vx += axDrag * TIME_STEP;
            vy += (ayDrag - GRAVITY) * TIME_STEP; // 空気抵抗 + 重力

            // 位置の更新 (速度を時間で積分)
            x += vx * TIME_STEP;
            y += vy * TIME_STEP;

            // 算出した座標を記録
            trajectory.add(new TrajectoryPoint(
                    Math.round(t * 1000.0) / 1000.0, // 小数点以下の丸め誤差対策
                    Math.round(x * 1000.0) / 1000.0,
                    Math.round(y * 1000.0) / 1000.0,
                    z));
        }

        // 的（ターゲット距離）を通り過ぎた最後のポイントを、ちょうどボードの位置（targetX）に補正する処理
        if (trajectory.size() >= 2 && x >= targetX) {
            int lastIndex = trajectory.size() - 1;
            TrajectoryPoint p2 = trajectory.get(lastIndex); // 通り過ぎた点
            TrajectoryPoint p1 = trajectory.get(lastIndex - 1); // その1つ手前の点

            // 比例計算（線形補間）で x = targetX の瞬間の正確な y を求める
            double ratio = (targetX - p1.x()) / (p2.x() - p1.x());
            double exactY = p1.y() + (p2.y() - p1.y()) * ratio;
            double exactTime = p1.time() + (p2.time() - p1.time()) * ratio;

            trajectory.set(lastIndex, new TrajectoryPoint(
                    Math.round(exactTime * 1000.0) / 1000.0,
                    targetX,
                    Math.round(exactY * 1000.0) / 1000.0,
                    p2.z()));
        }

        return new SimulationResponse(trajectory);
    }
}