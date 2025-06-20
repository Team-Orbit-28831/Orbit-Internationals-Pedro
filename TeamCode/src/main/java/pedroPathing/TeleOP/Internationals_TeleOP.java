package pedroPathing.TeleOP;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import pedroPathing.SUBSYSTEMS.Drivetrain;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.SUBSYSTEMS.Vision;
import pedroPathing.commands.ClawDown;
import pedroPathing.commands.ClawOpen;
import pedroPathing.commands.PivotBask;
//import pedroPathing.commands.SampleAutoAlign;
import pedroPathing.commands.SlidesHighBask;
import pedroPathing.commands.SlidesLowBask;
import pedroPathing.commands.PivotSample;
import pedroPathing.commands.SlidesSample;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

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
    private Vision vision;



    private Follower follower;
    private final Pose startPose = new Pose(0, 0, 0);

    private GamepadEx driver;
    private GamepadEx operator;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain();
        cascadeSlides = new CascadeSlides(hardwareMap, telemetry);
        cascadePivot = new CascadePivot(hardwareMap, telemetry);
        vision = new Vision(hardwareMap,telemetry);
        claw = new Claw();
        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);
        claw.init(hardwareMap);
        vision.initializeCamera();

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Slides pos", cascadeSlides.getCurrentPosition());
        telemetry.addData("Pivot pos", cascadePivot.getAveragePosition());
        telemetry.update();
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);



        waitForStart();

        while (opModeIsActive()) {
            // drivetrain
//            drivetrain.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
            Pose currentPose = follower.getPose();
            follower.startTeleopDrive();
            follower.update();


            CommandScheduler.getInstance().run();


            driver.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                    new SequentialCommandGroup(
                            new ParallelCommandGroup(
                                    new ClawDown(claw),
                                    new PivotSample(cascadePivot)
                            ),
                            new ClawOpen(claw),
                            new WaitCommand(200),
                            new SlidesLowBask(cascadeSlides),
                            new WaitCommand(100)

                    )

            );

            driver.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                    new SequentialCommandGroup(
//                            new cascadePivot.setPos(0)
//                            new cascadeSlides.moveSlidesTo(200)
                            new PivotBask(cascadePivot)
//                            new SlidesHighBask(cascadeSlides)
                    )

            );
//            driver.getGamepadButton(GamepadKeys.Button.A).whenPressed(
//                    new SequentialCommandGroup(
//
//                        new SlidesSample(cascadeSlides),
//                        new SampleAutoAlign(cascadeSlides,cascadePivot,vision,follower,currentPose,claw)
//                    )
//            );

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