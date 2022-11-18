package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
@Disabled
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="RedRight 0C", group="Phoebe", preselectTeleOp="Phoebe: Driver Controlled")
public class NoConeRedRight extends Autonomous0C {
    @Override
    public void init() {
        super.init(telemetry, Alliance.Color.RED, Field.StartingPosition.Right);
    }
}
