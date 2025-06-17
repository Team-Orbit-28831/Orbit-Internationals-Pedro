package pedroPathing.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import pedroPathing.SUBSYSTEMS.Drivetrain;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.Claw;

@TeleOp(name = "Internationals TeleOp", group = "Linear OpMode")
public class Internationals_TeleOP extends LinearOpMode {
    private Drivetrain drivetrain;
    private CascadeSlides cascadeSlides;
    private CascadePivot cascadePivot;
    private Claw claw;

    // Slide positions
    private static final int SLIDES_LOW_CHAMBER = 0;
    private static final int SLIDES_HIGH_CHAMBER = 250;
    private static final int SLIDES_LOW_BASKET = 215;
    private static final int SLIDES_HIGH_BASKET = 700;

    // Claw positions
    private static final double CLAW_OPEN = 0.8;
    private static final double CLAW_CLOSED = 0.2;
    private static final double CLAW_UP = 0.9;
    private static final double CLAW_DOWN = 0.1;
    private static final double CLAW_FLAT = 0.0;
    private static final double CLAW_DIA = 0.75;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain();
        cascadeSlides = new CascadeSlides();
        cascadePivot = new CascadePivot();
        claw = new Claw();

        drivetrain.init(hardwareMap);
        cascadeSlides.init(hardwareMap);
        cascadePivot.init(hardwareMap);
        claw.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Slides pos", cascadeSlides.getCurrentPosition());
        telemetry.addData("Pivot pos", cascadePivot.getCurrentPosition());
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // drivetrain
            drivetrain.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

            // claw
            if (gamepad2.left_trigger > 0.1) {
                claw.setServoPosOC(CLAW_OPEN);
            } else if (gamepad2.right_trigger > 0.1) {
                claw.setServoPosOC(CLAW_CLOSED);
            }

            if (Math.abs(gamepad2.right_stick_x) > 0.1) {
                claw.setServoPosRot(gamepad2.right_stick_x);
            }

            if (gamepad2.left_bumper) {
                claw.setServoPosUD(CLAW_UP);
            } else if (gamepad2.right_bumper) {
                claw.setServoPosUD(CLAW_DOWN);
            }

            if (gamepad2.a) {
                claw.setServoPosRot(CLAW_FLAT);
            } else if (gamepad2.b) {
                claw.setServoPosRot(CLAW_DIA);
            }

            // slides
//            if (gamepad2.y) {
//                cascadeSlides.setPower(0.8);
//            } else if (gamepad2.a) {
//                cascadeSlides.setPower(-0.8);
//            }

            // slides - PIDF
            if (gamepad2.dpad_down) {
                cascadeSlides.moveSlidesTo(SLIDES_LOW_CHAMBER);
            } else if (gamepad2.dpad_right) {
                cascadeSlides.moveSlidesTo(SLIDES_HIGH_CHAMBER);
            } else if (gamepad2.dpad_left) {
                cascadeSlides.moveSlidesTo(SLIDES_LOW_BASKET);
            } else if (gamepad2.dpad_up) {
                cascadeSlides.moveSlidesTo(SLIDES_HIGH_BASKET);
            }

            // pivot
            if (gamepad1.left_bumper) {
                cascadePivot.setPower(-0.6);
            } else if (gamepad1.right_bumper) {
                cascadePivot.setPower(0.6);
            } else {
                cascadePivot.setPower(0);
            }

            // telemetry
            telemetry.addData("Current Slides Position", cascadeSlides.getCurrentPosition());
            telemetry.addData("Current Pivot Position", cascadePivot.getCurrentPosition());
            telemetry.addData("Left Stick Y", gamepad2.left_stick_y);
            telemetry.addData("Left Stick X", gamepad2.left_stick_x);
            telemetry.addData("Right Stick X", gamepad2.right_stick_x);

            telemetry.update();
        }
    }
}