package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class FourBeamMotor {
    private DcMotor FBMotor;

    public FourBeamMotor(HardwareMap hardwareMap) {
        FBMotor = hardwareMap.get(DcMotor.class, RobotConfig.FOUR_BEAM);
    }

    public void setSpeed(double speed) {
        double speedToSet =
                Math.max((Math.min(speed, RobotConfig.MAX_FOUR_BEAM_SPEED)),
                        -RobotConfig.MAX_FOUR_BEAM_SPEED);
        FBMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.FBMotor.setPower(speedToSet);
        if (speedToSet != 0) {
            Match.log("Set four beam speed to " + speedToSet);
        }
    }

    public void setToTopPosition() {
        this.FBMotor.setTargetPosition(RobotConfig.FOUR_BEAM_INITIAL_POSITION);
    }

    public void setToBottomPosition() {
        this.FBMotor.setTargetPosition(RobotConfig.FOUR_BEAM_TOP_POSITION);
    }

    public String getStatus() {
        return String.format(Locale.getDefault(),"FourBeam:%d->%d@%.2f(Offset:%d)",
                this.FBMotor.getCurrentPosition(),
                this.FBMotor.getTargetPosition(),
                this.FBMotor.getPower());
    }

    public void stop() {
        this.FBMotor.setPower(0);
    }
}
