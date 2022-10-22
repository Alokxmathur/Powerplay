package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class WinchMotor {
    private DcMotor winchMotor;

    public WinchMotor(HardwareMap hardwareMap) {
        winchMotor = hardwareMap.get(DcMotor.class, RobotConfig.WINCH);
    }

    public void setSpeed(double speed) {
        double speedToSet =
                Math.max((Math.min(speed, RobotConfig.MAX_WINCH_SPEED)),
                        -RobotConfig.MAX_WINCH_SPEED);
        winchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.winchMotor.setPower(speedToSet);
        if (speedToSet != 0) {
            Match.log("Set winch speed to " + speedToSet);
        }
    }

    public void stop() {
        this.winchMotor.setPower(0);
    }

    public String getStatus() {
        return String.format(Locale.getDefault(), "C:%d->%d@%.2f",
                this.winchMotor.getCurrentPosition(),
                this.winchMotor.getTargetPosition(),
                this.winchMotor.getPower());
    }
}