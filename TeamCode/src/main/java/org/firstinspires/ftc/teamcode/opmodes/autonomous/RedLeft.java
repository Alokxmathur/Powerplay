package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="RedLeft", group="Phoebe", preselectTeleOp="Phoebe: Driver Controlled")
public class RedLeft extends NearCarouselAutonomous {
    @Override
    public void init() {
        super.init(Alliance.Color.RED, Field.StartingPosition.Left);
    }
}
