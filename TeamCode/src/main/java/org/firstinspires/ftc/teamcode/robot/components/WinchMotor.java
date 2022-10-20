package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class WinchMotor {
    private DcMotor motor;

    public WinchMotor(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, RobotConfig.WINCH);
    }

    public void setSpeed(double speed) {
        double speedToSet =
                Math.max((Math.min(speed, RobotConfig.MAX_WINCH_SPEED)),
                        -RobotConfig.MAX_WINCH_SPEED);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motor.setPower(speedToSet);
        if (speedToSet != 0) {
            Match.log("Set carousel speed to " + speedToSet);
        }
    }

    public void stop() {
        this.motor.setPower(0);
    }

    public String getStatus() {
        return String.format(Locale.getDefault(), "C:%d->%d@%.2f",
                this.motor.getCurrentPosition(),
                this.motor.getTargetPosition(),
                this.motor.getPower());
    }
}