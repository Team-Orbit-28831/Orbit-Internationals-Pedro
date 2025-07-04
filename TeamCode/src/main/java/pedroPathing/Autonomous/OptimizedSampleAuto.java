package pedroPathing.Autonomous;

import static java.lang.Thread.sleep;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
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
import pedroPathing.SUBSYSTEMS.Vision;
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

@Autonomous(name = "Optimized International Sample Auto", group = "Autonomous")
public class OptimizedSampleAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private CascadeSlides cascadeSlides;
    private CascadePivot cascadePivot;
    private Claw claw;
    private Vision vision;

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
                        new Point(13, convertOffset(127.38461538461539), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-35))
                .build();

        // Line 2: Move to first sample
        line2 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(13, convertOffset(127.38461538461539), Point.CARTESIAN),
                        new Point(26.827, convertOffset(117.5), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(10))
                .build();

        // Line 3: Return to rotation point
        line3 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(26.827, convertOffset(117.5), Point.CARTESIAN),
                        new Point(13, convertOffset(127.38461538461539), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(10), Math.toRadians(-35))
                .build();

        // Line 4: Move to second sample
        line4 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(13, convertOffset(127.38461538461539), Point.CARTESIAN),
                        new Point(26.654, convertOffset(126.846), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(2))
                .build();

        // Line 5: Return to rotation point
        line5 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(26.654, convertOffset(126.846), Point.CARTESIAN),
                        new Point(13, convertOffset(127.38461538461539), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-35))
                .build();

        // Line 6: Move to third sample
        line6 = follower.pathBuilder()
                .addPath(new BezierLine(
                        new Point(13, convertOffset(127.38461538461539), Point.CARTESIAN),
                        new Point(18.865, convertOffset(131.712), Point.CARTESIAN)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(-35), Math.toRadians(25))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Start initial setup while moving
                follower.followPath(line1);
                ParallelCommandGroup initialSetup = new ParallelCommandGroup(
                        new InstantCommand(() -> claw.downClaw()),
                        new PivotBask(cascadePivot),
                        new SlidesHighBask(cascadeSlides)
                );
                CommandScheduler.getInstance().schedule(initialSetup);
                setPathState(1);
                break;

            case 1:
                // Prepare for first sample drop while moving
                if (!follower.isBusy()) {
                    // Start preparing for drop immediately
                    ParallelCommandGroup prepDrop1 = new ParallelCommandGroup(
                            new InstantCommand(() -> claw.upClaw()),
                            new PivotBask(cascadePivot),
                            new SlidesHighBask(cascadeSlides)
                    );
                    CommandScheduler.getInstance().schedule(prepDrop1);

                    // Reduced wait time before next path
                    if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                        follower.followPath(line2, true);
                        setPathState(2);
                    }
                }
                break;

            case 2:
                // Drop first sample and prepare for collection
                if (!follower.isBusy()) {
                    SequentialCommandGroup dropAndPrep1 = new SequentialCommandGroup(
                            new InstantCommand(() -> claw.openClaw()),
                            new WaitCommand(100), // Reduced from 500ms
                            new ParallelCommandGroup(
                                    new InstantCommand(() -> claw.downClaw()),
                                    new PivotNormal(cascadePivot),
                                    new InstantCommand(() -> cascadeSlides.setSlideTarget(300))
                            )
                    );
                    CommandScheduler.getInstance().schedule(dropAndPrep1);

                    if (pathTimer.getElapsedTimeSeconds() > 0.3) {
                        follower.followPath(line3);
                        setPathState(3);
                    }
                }
                break;

            case 3:
                // Collect first sample
                if (!follower.isBusy()) {
                    SequentialCommandGroup collect1 = new SequentialCommandGroup(
                            new CollectSub(claw, cascadePivot),
                            new WaitCommand(50) // Reduced wait
                    );
                    CommandScheduler.getInstance().schedule(collect1);

                    if (pathTimer.getElapsedTimeSeconds() > 0.3) {
                        claw.closeClaw();
                        // Prepare for next drop while moving
                        ParallelCommandGroup prepDrop2 = new ParallelCommandGroup(
                                new PivotBask(cascadePivot),
                                new SlidesHighBask(cascadeSlides)
                        );
                        CommandScheduler.getInstance().schedule(prepDrop2);

                        follower.followPath(line4, true);
                        setPathState(4);
                    }
                }
                break;

            case 4:
                // Drop second sample
                if (!follower.isBusy()) {
                    SequentialCommandGroup dropAndPrep2 = new SequentialCommandGroup(
                            new InstantCommand(() -> claw.upClaw()),
                            new WaitCommand(100), // Reduced wait
                            new InstantCommand(() -> claw.openClaw()),
                            new WaitCommand(100), // Reduced from 500ms
                            new ParallelCommandGroup(
                                    new InstantCommand(() -> claw.downClaw()),
                                    new PivotNormal(cascadePivot),
                                    new InstantCommand(() -> cascadeSlides.setSlideTarget(310))
                            )
                    );
                    CommandScheduler.getInstance().schedule(dropAndPrep2);

                    if (pathTimer.getElapsedTimeSeconds() > 0.3) {
                        follower.followPath(line5);
                        setPathState(5);
                    }
                }
                break;

            case 5:
                // Collect second sample
                if (!follower.isBusy()) {
                    SequentialCommandGroup collect2 = new SequentialCommandGroup(
                            new CollectSub(claw, cascadePivot),
                            new WaitCommand(50) // Reduced wait
                    );
                    CommandScheduler.getInstance().schedule(collect2);

                    if (pathTimer.getElapsedTimeSeconds() > 0.3) {
                        claw.closeClaw();
                        // Prepare for final drop while moving
                        ParallelCommandGroup prepDrop3 = new ParallelCommandGroup(
                                new PivotBask(cascadePivot),
                                new SlidesHighBask(cascadeSlides)
                        );
                        CommandScheduler.getInstance().schedule(prepDrop3);

                        follower.followPath(line6, true);
                        setPathState(6);
                    }
                }
                break;

            case 6:
                // Drop third sample
                if (!follower.isBusy()) {
                    SequentialCommandGroup dropAndPrep3 = new SequentialCommandGroup(
                            new InstantCommand(() -> claw.upClaw()),
                            new WaitCommand(100), // Reduced wait
                            new InstantCommand(() -> claw.openClaw()),
                            new WaitCommand(100), // Reduced from 500ms
                            new ParallelCommandGroup(
                                    new InstantCommand(() -> claw.downClaw()),
                                    new PivotNormal(cascadePivot),
                                    new InstantCommand(() -> cascadeSlides.setSlideTarget(580))
                            )
                    );
                    CommandScheduler.getInstance().schedule(dropAndPrep3);

                    if (pathTimer.getElapsedTimeSeconds() > 0.3) {
                        follower.followPath(line5);
                        setPathState(7);
                    }
                }
                break;

            case 7:
                // Collect third sample
                if (!follower.isBusy()) {
                    SequentialCommandGroup collect3 = new SequentialCommandGroup(
                            new CollectSub(claw, cascadePivot),
                            new WaitCommand(50) // Reduced wait
                    );
                    CommandScheduler.getInstance().schedule(collect3);

                    if (pathTimer.getElapsedTimeSeconds() > 0.3) {
                        claw.closeClaw();
                        // Start final drop preparation immediately
                        ParallelCommandGroup finalPrep = new ParallelCommandGroup(
                                new PivotBask(cascadePivot),
                                new SlidesHighBask(cascadeSlides)
                        );
                        CommandScheduler.getInstance().schedule(finalPrep);
                        setPathState(8);
                    }
                }
                break;

            case 8:
                // Final drop sequence - optimized
                SequentialCommandGroup finalDrop = new SequentialCommandGroup(
                        new InstantCommand(() -> claw.upClaw()),
                        new ClawFlat(claw),
                        new WaitCommand(100), // Reduced from 300ms
                        new InstantCommand(() -> claw.openClaw()),
                        new WaitCommand(100), // Reduced from 500ms
                        new ParallelCommandGroup(
                                new InstantCommand(() -> claw.downClaw()),
                                new PivotNormal(cascadePivot),
                                new InstantCommand(() -> cascadeSlides.setSlideTarget(0))
                        )
                );
                CommandScheduler.getInstance().schedule(finalDrop);

                // Final cleanup - execute immediately
                SequentialCommandGroup cleanup = new SequentialCommandGroup(
                        new WaitCommand(200),
                        new ClawUp(claw),
                        new ClawOpen(claw)
                );
                CommandScheduler.getInstance().schedule(cleanup);
                setPathState(9);
                break;

            case 9:
                // Autonomous complete - no more actions needed
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