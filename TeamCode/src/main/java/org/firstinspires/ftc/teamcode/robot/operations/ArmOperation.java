package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.Arm;

import java.util.Date;
import java.util.Locale;

/**
 * This class implements oll of the operations related to managing the arm.
 * The types of operations permitted are:
 *  Open - opens the claw to release cone
 *  Close - closes the claw to grip cone
 *  Ground - hold cone at ground junction level
 *  Low - hold cone at low junction level
 *  Mid - hold cone at mid junction level
 *  High - hold cone at high junction level
 *  Pickup - get claw to level to pickup upright cone on the ground
 *  Stack5 - get claw to level to pickup the top (5'th) cone in the stack
 *  Stack4 - get claw to level to pickup the 4'th cone in the stack
 *  Stack3 - get claw to level to pickup the 3'rd cone in the stack
 *  Stack2 - get claw to level to pickup the 2'nd cone in the stack
 *  Stack1 - get claw to level to pickup the bottom cone in the stack - same as ground
 */
public class ArmOperation extends Operation {

    public enum Type {
        Release, Open, Close, Ground, Low, Mid, High, ObjectFinder, Pickup, InterimPickup, InterimDeposit, Stack5, Stack4, Stack3, Stack2, Stack1
    }
    Arm arm;
    Type type;

    public ArmOperation(Arm arm, Type type, String title) {
        this.arm = arm;
        this.type = type;
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "Arm: --%s",
                this.title);
    }

    public boolean isComplete() {
        if (type == Type.Open || type == Type.Close) {
            return (new Date().getTime() - this.getStartTime().getTime() > RobotConfig.SERVO_REQUIRED_TIME);
        }
        else {
            return arm.isWithinRange();
        }
    }

    @Override
    public void startOperation() {
        switch (this.type) {
            case ObjectFinder:
            case Release:
            case Pickup:
            case InterimPickup:
            case Ground:
            case Low:
            case Mid:
            case High:
            case Stack1:
            case Stack2:
            case Stack3:
            case Stack4:
            case Stack5: {
                arm.setPositions(type);
                break;
            }
            case Open: {
                arm.openClaw();
                break;
            }
            case Close:
            {
                arm.closeClaw();
                break;
            }
        }
    }

    @Override
    public void abortOperation() {
        arm.stop();
    }
}
