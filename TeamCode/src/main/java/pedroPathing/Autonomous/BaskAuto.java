package pedroPathing.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.commands.PivotBask;
import pedroPathing.commands.PivotNormal;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

import pedroPathing.commands.SlidesHighBask;

import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.SUBSYSTEMS.Drivetrain;
import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.CascadeSlides;

@Autonomous(name = "Basket Auto - Blue", group = "Autonomous")
public class BaskAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer;
    private int pathState;

    private CascadePivot pivot;
    private CascadeSlides slides;
    private Claw claw;

    // Starting pose
    private final Pose startPose = new Pose(9.757, 84.983, Math.toRadians(0));

    // Paths
    private PathChain pathToSample, pathToBasket, pathToNextSample, etc;

    @Override
    public void init() {
        pathTimer = new Timer();
        actionTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);

        pivot = new CascadePivot(hardwareMap, telemetry);
        slides = new CascadeSlides(hardwareMap, telemetry);
        claw = new Claw();

        claw.init(hardwareMap);

        buildPaths();
        pathState = 0;
    }

    public void buildPaths() {
        pathToSample = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(9.757, 84.983, Point.CARTESIAN),
                        new Point(10.203, 125.519, Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(280))
                .build();

        pathToBasket = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(10.203, 125.519, Point.CARTESIAN),
                        new Point(33.112, 23.679, Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(280), Math.toRadians(180))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Start - preload claw and set arm position
                claw.closeClaw();
                slides.retract();
                setPathState(1);
                break;

            case 1: // Wait for initial actions to complete
                if (claw.getCurrentCommand().isFinished() && slides.getCurrentCommand().isFinished()) {
                    setPathState(2);
                }
                break;

            case 2: // Move to first sample
                follower.followPath(pathToSample);
                setPathState(3);
                break;

            case 3: // Wait for path completion and prepare intake
                if (!follower.isBusy()) {
                    new SlidesHighBask(slides);
                    new PivotBask(pivot);
                    setPathState(4);
                }
                break;

            case 4: // Wait for mechanisms to get in position
                if (slides.getCurrentCommand().isFinished() && slides.getCurrentCommand().isFinished()) {
                    setPathState(5);
                }
                break;

            case 5: // Intake sample
                claw.openClaw();
                actionTimer.resetTimer();
                setPathState(6);
                break;

            case 6: // Wait for intake completion
                if (actionTimer.getElapsedTimeSeconds() > 1.0) { // Adjust timing as needed
                    claw.closeClaw();
                    slides.retract();

                }
                break;

            case 7: // Move to basket
                follower.followPath(pathToBasket);
                new PivotBask(pivot);
                setPathState(8);
                break;

            case 8: // Wait for path and arm movement
                if (!follower.isBusy() && slides.getCurrentCommand().isFinished()) {
                    setPathState(9);
                }
                break;

            case 9: // Score in basket
                claw.openClaw();
                actionTimer.resetTimer();
                setPathState(10);
                break;

            case 10: // Wait for scoring completion
                if (actionTimer.getElapsedTimeSeconds() > 0.5) {
                    claw.closeClaw();
                    new PivotNormal(pivot);
                setPathState(11);
                }
                break;

            case 11: // Continue to next sample or end
                setPathState(-1); // End autonomous
                break;
            default:
                // Autonomous complete
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
        telemetry.addData("Switching to state", pState);
    }

    @Override
    public void loop() {
        // Update Pedro Pathing
        follower.update();

        // Run autonomous state machine
        autonomousPathUpdate();

        // Telemetry
        telemetry.addData("Path State", pathState);
        telemetry.addData("Pose", follower.getPose());
        telemetry.addData("Is Busy", follower.isBusy());
        telemetry.addData("Path Timer", pathTimer.getElapsedTimeSeconds());

        // Add subsystem telemetry
        telemetry.addData("Pivot Position", pivot.getAveragePosition());
        telemetry.addData("Slides Position", slides.getCurrentPosition());
        telemetry.addData("Claw State", claw.getCurrentOCPos());
        telemetry.addData("Claw Pivot State", claw.getCurrentUDPos());
        telemetry.addData("Claw Rotation state", claw.getCurrentROTPos());

        telemetry.update();
    }

    @Override
    public void stop() {
        // Stop all subsystems when autonomous ends
        pivot.stop();
        slides.stop();
//        claw.stop();
    }
}