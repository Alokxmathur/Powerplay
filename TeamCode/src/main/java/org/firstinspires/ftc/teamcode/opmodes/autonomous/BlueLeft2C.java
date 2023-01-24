package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="BlueLeft 1C", group="Phoebe", preselectTeleOp="Phoebe: Driver Controlled")
public class BlueLeft2C extends Autonomous1Cone {
    @Override
    public void init() {
        super.init(telemetry, Alliance.Color.BLUE, Field.StartingPosition.Left);
    }
}
