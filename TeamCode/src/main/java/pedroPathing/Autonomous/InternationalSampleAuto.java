package pedroPathing.Autonomous;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.commands.ClawClose;
import pedroPathing.commands.ClawDown;
import pedroPathing.commands.ClawFlat;
import pedroPathing.commands.ClawOpen;
import pedroPathing.commands.ClawPerp;
import pedroPathing.commands.ClawUp;
import pedroPathing.commands.CollectSub;
import pedroPathing.commands.PivotBask;
import pedroPathing.commands.PivotNormal;
import pedroPathing.commands.SlideRetract;
import pedroPathing.commands.SlidesHighBask;
import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "International Sample Auto", group = "Autonomous")
public class InternationalSampleAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private CascadeSlides cascadeSlides;

    private CascadePivot cascadePivot;

    private Claw claw;

    private SequentialCommandGroup dropSample, collect1S, collect2S, collect3S, dropClaw;


    private int pathState;

    // Starting pose
    private final Pose startPose = new Pose(9.173, convertOffset(110.942), Math.toRadians(0));

    // Generated paths
    private PathChain line1, line2, line3, line4, line5, line6;

    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        cascadePivot = new CascadePivot(hardwareMap, telemetry);
        cascadeSlides = new CascadeSlides(hardwareMap, telemetry);
        claw = new Claw(hardwareMap);

        Constants.setConstants(FConstants.class, LConstants.class);

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startPose);

        claw.closeClaw();
        buildPaths();
        pathState = 0;
    }

    public void buildPaths() {
        // Line 1: Move to rotation point
        line1 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(9.173, convertOffset(110.942), Point.CARTESIAN),
                        new Point(11.596153846153847, convertOffset(127.38461538461539), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-35))
                .build();

        // Line 2: Move to first sample
        line2 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(11.596153846153847, convertOffset(127.38461538461539), Point.CARTESIAN),
                        new Point(26.827, convertOffset(118.115), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(0))
                .build();

        // Line 3: Return to rotation point
        line3 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(26.827, convertOffset(118.115), Point.CARTESIAN),
                        new Point(11.596153846153847, convertOffset(127.38461538461539), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-35))
                .build();

        // Line 4: Move to second sample
        line4 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(11.596153846153847, convertOffset(127.38461538461539), Point.CARTESIAN),
                        new Point(26.654, convertOffset(127.246), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(0))
                .build();

        // Line 5: Return to rotation point
        line5 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(26.654, convertOffset(127.246), Point.CARTESIAN),
                        new Point(11.596153846153847, convertOffset(127.38461538461539), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-35))
                .build();

        // Line 6: Move to third sample
        line6 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(11.596153846153847, convertOffset(127.38461538461539), Point.CARTESIAN),
                        new Point(26.827, convertOffset(125.38461538461539), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(62))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Follow first path to rotation point
                follower.followPath(line1);
                SequentialCommandGroup command1 = new SequentialCommandGroup(
                        new ClawDown(claw),
                        new ClawClose(claw),
                        new PivotBask(cascadePivot),
                        new SlidesHighBask(cascadeSlides)
                );
                CommandScheduler.getInstance().schedule(command1);
                setPathState(1);
                break;

            case 1:
                // Wait for first path to complete, then follow line2 (to first sample)
                if (!follower.isBusy()) {
                    // Schedule commands to execute while moving
                    SequentialCommandGroup command2 = new SequentialCommandGroup(
                            new PivotBask(cascadePivot),
                            new SlidesHighBask(cascadeSlides),
                            new ClawUp(claw),
                            new WaitCommand(500),
                            new ClawOpen(claw),
                            new WaitCommand(500),
                            new ClawDown(claw),
                            new PivotNormal(cascadePivot),
                            new InstantCommand(() -> {
                                cascadeSlides.setSlideTarget(300);
                            })
                    );
                    CommandScheduler.getInstance().schedule(command2);

                    if (pathTimer.getElapsedTimeSeconds() > 3) {
                        follower.followPath(line2, true);
                        setPathState(2);
                    }
                }
                break;

            case 2:
                // Wait for line2 to complete, then follow line3 (return to rotation point)
                if (!follower.isBusy()) {
                    SequentialCommandGroup command3 = new SequentialCommandGroup(
                            new CollectSub(claw, cascadePivot),
                            new WaitCommand(100)
                    );
                    CommandScheduler.getInstance().schedule(command3);

                    if (pathTimer.getElapsedTimeSeconds() > 3) {
                        claw.closeClaw();
                        follower.followPath(line3);
                        setPathState(3);
                    }
                }
                break;

            case 3:
                // Wait for line3 to complete, then follow line4 (to second sample)
                if (!follower.isBusy()) {
                    SequentialCommandGroup command4 = new SequentialCommandGroup(
                            new PivotBask(cascadePivot),
                            new SlidesHighBask(cascadeSlides),
                            new ClawUp(claw),
                            new WaitCommand(300),
                            new ClawOpen(claw),
                            new WaitCommand(500),
                            new ClawDown(claw),
                            new PivotNormal(cascadePivot),
                            new InstantCommand(() -> {
                                cascadeSlides.setSlideTarget(300);
                            })
                    );
                    CommandScheduler.getInstance().schedule(command4);
                    if (pathTimer.getElapsedTimeSeconds() > 3) {
                        follower.followPath(line4, true);
                        setPathState(4);
                    }
                }
                break;

            case 4:
                // Wait for line4 to complete, then follow line5 (return to rotation point)
                if (!follower.isBusy()) {
                    SequentialCommandGroup command3 = new SequentialCommandGroup(
                            new CollectSub(claw, cascadePivot),
                            new WaitCommand(100)
                    );
                    CommandScheduler.getInstance().schedule(command3);

                    if (pathTimer.getElapsedTimeSeconds() > 3) {
                        claw.closeClaw();
                        follower.followPath(line5);
                        setPathState(5);
                    }
                }
                break;

            case 5:
                // Wait for line5 to complete, then follow line6 (to third sample)
                if (!follower.isBusy()) {
                    SequentialCommandGroup command4 = new SequentialCommandGroup(
                            new PivotBask(cascadePivot),
                            new SlidesHighBask(cascadeSlides),
                            new ClawUp(claw),
                            new WaitCommand(300),
                            new ClawOpen(claw),
                            new WaitCommand(500),
                            new ClawDown(claw),
                            new PivotNormal(cascadePivot),
                            new InstantCommand(() -> {
                                cascadeSlides.setSlideTarget(460);
                            })
                    );
                    CommandScheduler.getInstance().schedule(command4);
                    if (pathTimer.getElapsedTimeSeconds() > 4) {
                        follower.followPath(line6, true);
                        setPathState(6);
                    }
                }
                break;

            case 6:
                if (!follower.isBusy()) {
                    SequentialCommandGroup command3 = new SequentialCommandGroup(
//                            new ClawPerp(claw),
                            new CollectSub(claw, cascadePivot),
                            new WaitCommand(100)
                    );
                    CommandScheduler.getInstance().schedule(command3);


                    if (pathTimer.getElapsedTimeSeconds() > 3.5) {
                        claw.closeClaw();
                        follower.followPath(line5);
                        setPathState(7);
                    }
                }
                break;
            case 7:
                if (!follower.isBusy()) {
                    SequentialCommandGroup command4 = new SequentialCommandGroup(

                            new PivotBask(cascadePivot),
                            new SlidesHighBask(cascadeSlides),
                            new ClawUp(claw),
                            new ClawFlat(claw),
                            new WaitCommand(300),
                            new ClawOpen(claw),
                            new WaitCommand(500),
                            new ClawDown(claw),
                            new PivotNormal(cascadePivot),
                            new InstantCommand(() -> {
                                cascadeSlides.setSlideTarget(0);
                            })
                    );
                    CommandScheduler.getInstance().schedule(command4);
                    if (pathTimer.getElapsedTimeSeconds() > 4) {
                        claw.upClaw();

                        claw.openClaw();
                        break;
                    }
                }
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
        CommandScheduler.getInstance().run();

        // Telemetry
        telemetry.addData("Path State", pathState);
        telemetry.addData("Pose", follower.getPose());
        telemetry.addData("Is Busy", follower.isBusy());
        telemetry.addData("Path Timer", pathTimer.getElapsedTimeSeconds());
        telemetry.addData("Opmode Timer", opmodeTimer.getElapsedTimeSeconds());

        telemetry.update();
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    public double convertOffset(double y) {
        return (y / 24) * 16;
    }

    @Override
    public void stop() {
        // Clean shutdown
    }
}