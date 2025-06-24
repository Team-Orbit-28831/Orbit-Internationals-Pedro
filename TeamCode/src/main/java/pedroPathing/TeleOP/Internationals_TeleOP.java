package pedroPathing.TeleOP;

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
import pedroPathing.commands.ClawClose;
//import pedroPathing.commands.ClawDiagonal;
import pedroPathing.commands.ClawDown;
import pedroPathing.commands.ClawFlat;
import pedroPathing.commands.ClawOpen;
import pedroPathing.commands.ClawPerp;
import pedroPathing.commands.ClawUp;
import pedroPathing.commands.ClawVision;
import pedroPathing.commands.CollectSub;
//import pedroPathing.commands.SampleAutoAlign;
import pedroPathing.commands.PivotBask;
import pedroPathing.commands.PivotReset;
import pedroPathing.commands.PivotSampleLong;
import pedroPathing.commands.PivotNormal;
import pedroPathing.commands.PivotSampleShort;
import pedroPathing.commands.SlideRetract;
import pedroPathing.commands.SlideSampleLong;
import pedroPathing.commands.SlidesHighBask;
import pedroPathing.commands.SlidesSampleShort;
import pedroPathing.commands.VisionSlides;
import pedroPathing.commands.m_pivotdown;
import pedroPathing.commands.m_pivotup;
import pedroPathing.commands.m_slidesdown;
import pedroPathing.commands.m_slidesup;
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
//    private DriveTrainBasic driveTrainBasic;


    private Follower follower;
    private final Pose startPose = new Pose(0, 0, 0);

    private GamepadEx driver;
    private GamepadEx operator;

    @Override
    public void runOpMode() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
//        driveTrainBasic = new DriveTrainBasic();
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
//            driveTrainBasic.drive(-gamepad2.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,1);
//            follower.setTeleOpMovementVectors(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
//            Pose currentPose = follower.getPose();
//            follower.startTeleopDrive();
//            follower.update();
            drivetrain.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,1);
            if (gamepad1.left_trigger>0.1){
                drivetrain.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,2);
            }




            CommandScheduler.getInstance().run();



//            driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
//                    new ClawDown(claw)
//            );
            vision.periodic();
            //vision.setDetectionColor(Vision.SampleColor.RED);

            // short sample collection


            driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                    new SequentialCommandGroup(
                            new ClawOpen(claw),
                            new WaitCommand(300),
                            new PivotSampleShort(cascadePivot),
                            new ClawVision(claw, vision),
                            new VisionSlides(cascadeSlides,vision),
                            new ClawOpen(claw),
//                            new ClawFlat(claw)

                            new ClawDown(claw),
                            new WaitCommand(150)



//                            new ClawDown(claw)

                    )
            );

            driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                    new SequentialCommandGroup(
                            new ClawOpen(claw),
                            new WaitCommand(300),
                            new PivotSampleShort(cascadePivot),
                            new SlidesSampleShort(cascadeSlides),
                            new ClawOpen(claw),
                            new ClawFlat(claw),
                            new ClawDown(claw)

//                            new ClawDown(claw)

                    )
            );

            operator.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON).whenPressed(
                    new ClawUp(claw)
            );

            operator.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON).whenPressed(
                    new ClawDown(claw)
            );

            // long sample collection
            driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                    new SequentialCommandGroup(
                            new ClawOpen(claw),
                            new WaitCommand(300),
                            new ClawDown(claw),
                            new PivotSampleLong(cascadePivot),
                            new SlideSampleLong(cascadeSlides),
                            new ClawFlat(claw)
                    )
            );


            // collect
            operator.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                    new SequentialCommandGroup(
                            new CollectSub(claw, cascadePivot),
                            new ClawClose(claw),
                            new WaitCommand(50)
                    )

            );

            // manual claw close
            operator.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                    new ClawClose(claw)
            );

            // manual claw open
            operator.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                    new ClawOpen(claw)
            );

            // manual claw close
            driver.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON).whenPressed(
                    new ClawClose(claw)
            );

            // manual claw open
            driver.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON).whenPressed(
                    new ClawOpen(claw)
            );


            // retract
            driver.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                    new SequentialCommandGroup(
                            new ClawClose(claw),
                            new WaitCommand(50),
                            new ClawUp(claw),
                            new SlideRetract(cascadeSlides),
//                            new PivotSampleShort(cascadePivot),
//                            new PivotNormal(cascadePivot),
                            new PivotBask(cascadePivot),
                            new ClawClose(claw)

                    )

            );

//          reset
            driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                    new SequentialCommandGroup(
                            new ClawClose(claw),
                            new ClawUp(claw),
                            new SlideRetract(cascadeSlides),
//                            new PivotSampleShort(cascadePivot),
                            new PivotReset(cascadePivot),
//                            new PivotBask(cascadePivot),
                            new ClawClose(claw)

                    )

            );

            // slides to high bask
            driver.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                    new SlidesHighBask(cascadeSlides)
            );

            // open claw
//            driver.getGamepadButton(GamepadKeys.Button.B).whenPressed(
//                    new ClawOpen(claw)
//            );

            // moves slide up and down manually
            operator.getGamepadButton(GamepadKeys.Button.X).whileHeld(
                    new m_slidesdown(cascadeSlides)
            );

            operator.getGamepadButton(GamepadKeys.Button.Y).whileHeld(
                    new m_slidesup(cascadeSlides)
            );

            // move pivot up and down manually
            operator.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whileHeld(
                    new m_pivotup(cascadePivot)
            );

            operator.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whileHeld(
                    new m_pivotdown(cascadePivot)
            );

            operator.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                    new ClawFlat(claw)
            );

            operator.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                    new ClawPerp(claw)
            );

//            operator.getGamepadButton(GamepadKeys.Button.B).whenPressed(
//                    new ClawDiagonal(claw)
//            );

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
//                claw.downClaw();
//            } else if (gamepad2.right_bumper) {
//                claw.upClaw();
//            }
////
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
            telemetry.addData("claw pos", claw.getCurrentUDPos());


            telemetry.update();
        }
    }
}