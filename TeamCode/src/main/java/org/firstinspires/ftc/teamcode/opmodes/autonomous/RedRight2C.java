package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="RedRight 2C", group="Phoebe", preselectTeleOp="Phoebe: Driver Controlled")
public class RedRight2C extends Autonomous2Cones {
    @Override
    public void init() {
        super.init(telemetry, Alliance.Color.RED, Field.StartingPosition.Right);
    }
}
