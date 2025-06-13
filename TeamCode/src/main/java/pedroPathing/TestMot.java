package pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Test", group = "Linear OpMode")
public class TestMot extends LinearOpMode {

    DcMotor one;
    DcMotor two;

    @Override
    public void runOpMode() {
        one = hardwareMap.get(DcMotorEx.class, "one");
        two = hardwareMap.get(DcMotorEx.class, "two");

        one.setDirection(DcMotorEx.Direction.REVERSE);
        two.setDirection(DcMotorEx.Direction.REVERSE); // Adjust if needed

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad2.a) {
                one.setPower(1.0);
                two.setPower(-1.0);
            } else if (gamepad2.b) {
                one.setPower(-1.0);
                two.setPower(1.0);
            }
            else {
                one.setPower(0.0);
                two.setPower(0.0);
            }
        }
    }
}
