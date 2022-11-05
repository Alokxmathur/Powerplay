package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class VirtualFourBar {
    DcMotor virtualFourBarMotor;
    Servo endServo;
    boolean isInVerticalPosition;
    Object synchronizer = new Object();

    public VirtualFourBar(HardwareMap hardwareMap) {
        virtualFourBarMotor = hardwareMap.get(DcMotor.class, RobotConfig.FOUR_BAR);
        virtualFourBarMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        virtualFourBarMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        virtualFourBarMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        endServo = hardwareMap.get(Servo.class, RobotConfig.ENDSERVO);
        assumeInitialPosition();
    }

    public void setEndServoPosition(double position) {
        this.endServo.setPosition(position);
    }

    public void assumeInitialPosition() {
        this.endServo.setPosition(RobotConfig.ENDSERVO_INITIAL_POSITION);
    }

    public void stayVertical(){
        double x = this.virtualFourBarMotor.getCurrentPosition();
        double y = -x * 1/288;
        setEndServoPosition(y);
    }

    public boolean isVertical() {
        synchronized (synchronizer) {
            return this.isInVerticalPosition;
        }
    }

    public void setSpeed(double speed) {
        double speedToSet =
                Math.max((Math.min(speed, RobotConfig.MAX_VIRTUAL_FOUR_BAR_SPEED)),
                        -RobotConfig.MAX_VIRTUAL_FOUR_BAR_SPEED);
        virtualFourBarMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.virtualFourBarMotor.setPower(speedToSet);
        if (speedToSet != 0) {
            Match.log("Set four beam speed to " + speedToSet);
        }
    }

    private void setMotorPosition(int position) {
        this.virtualFourBarMotor.setTargetPosition(position);
        this.virtualFourBarMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.virtualFourBarMotor.setPower(RobotConfig.MAX_FOUR_BAR_SPEED);
    }

    public void setToPickUpPosition() {
        this.setMotorPosition(RobotConfig.VIRTUAL_FOUR_BAR_INITIAL_POSITION);
        this.stayVertical();
    }

    public void setToDropOffPosition() {
        this.setMotorPosition(RobotConfig.VIRTUAL_FOUR_BAR_DROP_POSITION);
        this.stayVertical();
    }

    public void raise() {
        this.setMotorPosition(this.virtualFourBarMotor.getCurrentPosition() + RobotConfig.FOUR_BAR_INCREMENT);
        this.stayVertical();
    }

    public void lower() {
        this.setMotorPosition(this.virtualFourBarMotor.getCurrentPosition() - RobotConfig.FOUR_BAR_INCREMENT);
        this.stayVertical();
    }

    public String getStatus() {
        return String.format(Locale.getDefault(),"FourBeam:%d->%d@%.2f",
                this.virtualFourBarMotor.getCurrentPosition(),
                this.virtualFourBarMotor.getTargetPosition(),
                this.virtualFourBarMotor.getPower());
    }

    public void stop() {
        this.virtualFourBarMotor.setPower(0);
    }

    public boolean isWithinRange() {
        return Math.abs(virtualFourBarMotor.getTargetPosition() - virtualFourBarMotor.getCurrentPosition())
                <= RobotConfig.ACCEPTABLE_FOUR_BAR_ERROR;
    }



}
