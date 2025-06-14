package pedroPathing.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import pedroPathing.SUBSYSTEMS.CascadeSlides;

@TeleOp(name="Check Position", group="Testing")
public class CheckPosition extends LinearOpMode {

    private CascadeSlides cascadeSlides = new CascadeSlides();

    @Override
    public void runOpMode() {
        // Initialize hardware
        cascadeSlides.init(hardwareMap);

        telemetry.addLine("Ready to start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("Cascade Slide Position", cascadeSlides.getCurrentPosition());
            telemetry.update();
        }
    }
}
