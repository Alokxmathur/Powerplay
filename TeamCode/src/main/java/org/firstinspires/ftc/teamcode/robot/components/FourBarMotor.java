package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class FourBarMotor {
    private DcMotor fourBarMotor;

    public FourBarMotor(HardwareMap hardwareMap) {
        fourBarMotor = hardwareMap.get(DcMotor.class, RobotConfig.FOUR_BAR);
        fourBarMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fourBarMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fourBarMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setSpeed(double speed) {
        double speedToSet =
                Math.max((Math.min(speed, RobotConfig.MAX_FOUR_BAR_SPEED)),
                        -RobotConfig.MAX_FOUR_BAR_SPEED);
        fourBarMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.fourBarMotor.setPower(speedToSet);
        if (speedToSet != 0) {
            Match.log("Set four beam speed to " + speedToSet);
        }
    }

    public void setToTopPosition() {
        this.fourBarMotor.setTargetPosition(RobotConfig.FOUR_BAR_TOP_POSITION);
        this.fourBarMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.fourBarMotor.setPower(RobotConfig.MAX_FOUR_BAR_SPEED);
    }

    public void setToBottomPosition() {
        this.fourBarMotor.setTargetPosition(RobotConfig.FOUR_BAR_INITIAL_POSITION);
        this.fourBarMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.fourBarMotor.setPower(RobotConfig.MAX_FOUR_BAR_SPEED);
    }

    public String getStatus() {
        return String.format(Locale.getDefault(),"FourBeam:%d->%d@%.2f",
                this.fourBarMotor.getCurrentPosition(),
                this.fourBarMotor.getTargetPosition(),
                this.fourBarMotor.getPower());
    }

    public void stop() {
        this.fourBarMotor.setPower(0);
    }

    public boolean isWithinRange() {
        return Math.abs(fourBarMotor.getTargetPosition() - fourBarMotor.getCurrentPosition())
                <= RobotConfig.ACCEPTABLE_FOUR_BAR_ERROR;
    }
}
