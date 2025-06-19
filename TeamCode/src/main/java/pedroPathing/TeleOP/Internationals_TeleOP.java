package pedroPathing.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import pedroPathing.SUBSYSTEMS.Drivetrain;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.commands.PivotBask;
import pedroPathing.commands.SlidesHighBask;
import pedroPathing.commands.SlidesLowBask;
import pedroPathing.commands.PivotSample;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.command.CommandScheduler;

@TeleOp(name = "Internationals TeleOp Main", group = "Linear OpMode")
public class Internationals_TeleOP extends LinearOpMode {
    private Drivetrain drivetrain;
    private CascadeSlides cascadeSlides;
    private CascadePivot cascadePivot;
    private Claw claw;



    // Claw positions
    private static final double CLAW_OPEN = 0.8;
    private static final double CLAW_CLOSED = 0.2;
    private static final double CLAW_UP = 0.9;
    private static final double CLAW_DOWN = 0.1;
    private static final double CLAW_FLAT = 0.0;
    private static final double CLAW_DIA = 0.75;

    private GamepadEx driver;
    private GamepadEx operator;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain();
        cascadeSlides = new CascadeSlides(hardwareMap, telemetry);
        cascadePivot = new CascadePivot(hardwareMap, telemetry);
        claw = new Claw();
        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);
        claw.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Slides pos", cascadeSlides.getCurrentPosition());
        telemetry.addData("Pivot pos", cascadePivot.getAveragePosition());
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // drivetrain
//            drivetrain.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            CommandScheduler.getInstance().run();

            driver.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                    new SequentialCommandGroup(
//                            new cascadePivot.setPos(0)
//                            new cascadeSlides.moveSlidesTo(200)
                            new PivotSample(cascadePivot),
                            new SlidesLowBask(cascadeSlides)
                    )

            );

            driver.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                    new SequentialCommandGroup(
//                            new cascadePivot.setPos(0)
//                            new cascadeSlides.moveSlidesTo(200)
                            new PivotBask(cascadePivot),
                            new SlidesHighBask(cascadeSlides)
                    )

            );

//            if (gamepad1.x) {
//                cascadeSlides.setPower(0.5);
//            }


            // claw

//            if (gamepad2.left_trigger > 0.1) {
//                claw.setServoPosOC(CLAW_OPEN);
//            } else if (gamepad2.right_trigger > 0.1) {
//                claw.setServoPosOC(CLAW_CLOSED);
//            }
//
//            if (Math.abs(gamepad2.right_stick_x) > 0.1) {
//                claw.setServoPosRot(gamepad2.right_stick_x);
//            }
//
//            if (gamepad2.left_bumper) {
//                claw.setServoPosUD(CLAW_UP);
//            } else if (gamepad2.right_bumper) {
//                claw.setServoPosUD(CLAW_DOWN);
//            }
//
//            if (gamepad2.a) {
//                claw.setServoPosRot(CLAW_FLAT);
//            } else if (gamepad2.b) {
//                claw.setServoPosRot(CLAW_DIA);
//            }

            // slides
//            if (gamepad2.y) {
//                cascadeSlides.setPower(0.8);
//            } else if (gamepad2.a) {
//                cascadeSlides.setPower(-0.8);
//            }

//            // slides - PIDF
//            if (gamepad2.dpad_down) {
//                cascadeSlides.moveSlidesTo(SLIDES_LOW_CHAMBER);
//            } else if (gamepad2.dpad_right) {
//                cascadeSlides.moveSlidesTo(SLIDES_HIGH_CHAMBER);
//            } else if (gamepad2.dpad_left) {
//                cascadeSlides.moveSlidesTo(SLIDES_LOW_BASKET);
//            } else if (gamepad2.dpad_up) {
//                cascadeSlides.moveSlidesTo(SLIDES_HIGH_BASKET);
//            }

//            // pivot
//            if (gamepad1.left_bumper) {
//                cascadePivot.setPower(-0.5);
//            } else if (gamepad1.right_bumper) {
//                cascadePivot.setPower(0.5);
//            } else {
//                cascadePivot.setPower(0);
//            }

            // telemetry
            telemetry.addData("Current Slides Position", cascadeSlides.getCurrentPosition());
            telemetry.addData("Current Pivot Position", cascadePivot.getAveragePosition());
            telemetry.addData("Left Stick Y", gamepad2.left_stick_y);
            telemetry.addData("Left Stick X", gamepad2.left_stick_x);
            telemetry.addData("Right Stick X", gamepad2.right_stick_x);
            telemetry.addData("button prrr", driver.getGamepadButton(GamepadKeys.Button.X).get());

            telemetry.update();
        }
    }
}