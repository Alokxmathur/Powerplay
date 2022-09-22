package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="BlueLeft", group="Phoebe", preselectTeleOp="Phoebe: Driver Controlled")
public class BlueLeft extends NearWarehouseAutonomous {
    @Override
    public void init() {
        super.init(Alliance.Color.BLUE, Field.StartingPosition.Left);
    }
}