package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SilverTitansDriveConstants;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;

public class BabyBot {
    DcMotor leftFrontDrive, rightFrontDrive, leftRearDrive, rightRearDrive;
    DcMotor turret;

    Telemetry telemetry;

    /**
     * Our baby bot with four mecanum wheels, a turret and a linear slide
     * @param hardwareMap
     * @param telemetry
     */
    public BabyBot(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.leftFrontDrive = hardwareMap.get(DcMotor.class, RobotConfig.LEFT_FRONT_DRIVE);
        this.rightFrontDrive = hardwareMap.get(DcMotor.class, RobotConfig.RIGHT_FRONT_DRIVE);
        this.leftRearDrive = hardwareMap.get(DcMotor.class, RobotConfig.LEFT_REAR_DRIVE);
        this.rightRearDrive = hardwareMap.get(DcMotor.class, RobotConfig.RIGHT_REAR_DRIVE);

        this.leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        this.rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rightFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        this.leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.leftRearDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        this.rightRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.rightRearDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        this.turret = hardwareMap.get(DcMotor.class, RobotConfig.TURRET_MOTOR);
    }

    /**
     * Drive in the specified direction at the specified speed while rotating at the specified rotation
     * Direction is relative to the robot
     * @param direction - direction to drive
     * @param speed - speed at which to drive
     * @param rotation - how much to rotate while driving
     */
    public void drive(double direction, double speed, double rotation) {
        double sin = Math.sin(direction + Math.PI / 4.0);
        double cos = Math.cos(direction + Math.PI / 4.0);
        double max = Math.max(Math.abs(sin), Math.abs(cos));
        sin /= max;
        cos /= max;

        double v1 = speed * sin + rotation;
        double v2 = speed * cos - rotation;
        double v3 = speed * cos + rotation;
        double v4 = speed * sin - rotation;

        // Ensure that none of the values go over 1.0. If none of the provided values are
        // over 1.0, just scale by 1.0 and keep all values.
        double scale = DriveTrain.max(1.0, v1, v2, v3, v4);
        if (scale > 1) {
            v1 /= scale;
            v2 /= scale;
            v3 /= scale;
            v4 /= scale;
        }

        this.leftFrontDrive.setPower(v1);
        this.rightFrontDrive.setPower(v2);
        this.leftRearDrive.setPower(v3);
        this.rightRearDrive.setPower(v4);
    }

    /**
     * A method for the baby bot to move forward the specified number of mms
     * @param mms - mms to move, provide negative values to go backwards
     */
    public void move(double mms) {
        int encoderChange = SilverTitansDriveConstants.mmToEncoderTicks(mms);
        this.leftRearDrive.setTargetPosition(this.leftRearDrive.getCurrentPosition() + encoderChange);
        this.rightFrontDrive.setTargetPosition(this.rightFrontDrive.getCurrentPosition() + encoderChange);
        this.leftFrontDrive.setTargetPosition(this.leftFrontDrive.getCurrentPosition() + encoderChange);
        this.rightRearDrive.setTargetPosition(this.rightRearDrive.getCurrentPosition() + encoderChange);

        this.leftRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.rightRearDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        this.leftRearDrive.setPower(.35);
        this.rightFrontDrive.setPower(.35);
        this.leftFrontDrive.setPower(.35);
        this.rightRearDrive.setPower(.35);
    }

    public void setTurretSpeed(double speed) {
        this.turret.setPower(speed);
    }

    /**
     * Display the provided string under the Display title on the driver station
     * @param displayThis
     */
    public void display(String displayThis) {
        this.telemetry.addData("Display", displayThis);
    }
}
