package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class WinchMotor {
    private DcMotor winchMotor;

    public WinchMotor(HardwareMap hardwareMap) {
        winchMotor = hardwareMap.get(DcMotor.class, RobotConfig.WINCH);
        winchMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        winchMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        winchMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        winchMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void ensureDirection() {
        this.winchMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setSpeed(double speed) {
        double speedToSet =
                Math.max((Math.min(speed, RobotConfig.MAX_WINCH_SPEED)),
                        -RobotConfig.MAX_WINCH_SPEED);
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

    /**
     * Sets the winch position to the provided winch position and sets its power so the winch
     * will try to get to that position
     * @param winchPosition
     */
    public void setPosition(int winchPosition) {
        this.winchMotor.setTargetPosition(winchPosition);
        this.winchMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.setSpeed(1);
    }

    /**
     * Returns true if winch is within acceptable encoder error of desired position
     * @return
     */
    public boolean isWithinRange() {
        return Math.abs(winchMotor.getTargetPosition() - winchMotor.getCurrentPosition()) <= RobotConfig.ACCEPTABLE_WINCH_ERROR;
    }
}