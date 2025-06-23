package pedroPathing.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "servo test for starlink", group = "Linear Opmode")
public class servoteststarlink extends LinearOpMode {

    // Declare Servo objects
    private Servo servo1;
    private Servo servo2;

    @Override
    public void runOpMode() {

        // Map servos from hardware
        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");

        // Reverse the direction of servo2
        servo2.setDirection(Servo.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start
        waitForStart();

        while (opModeIsActive()) {

            // Press A -> go to position 0.5
            if (gamepad1.a) {
                servo1.setPosition(0.5);
                servo2.setPosition(0.5);
            }

            // Press B -> go to position 1.0
            if (gamepad1.b) {
                servo1.setPosition(1.0);
                servo2.setPosition(1.0);
            }

            // Display current positions
            telemetry.addData("Servo1 Position", servo1.getPosition());
            telemetry.addData("Servo2 Position", servo2.getPosition());
            telemetry.update();
        }
    }
}
