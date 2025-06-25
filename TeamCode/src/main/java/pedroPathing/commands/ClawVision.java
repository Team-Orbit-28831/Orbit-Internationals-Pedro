//package pedroPathing.commands;
//import com.arcrobotics.ftclib.command.CommandBase;
//
//import pedroPathing.SUBSYSTEMS.Claw;
//import pedroPathing.SUBSYSTEMS.Vision;
//
//public class ClawVision extends CommandBase {
//    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
//    private final Claw claw;
//    private final Vision vision;
//
//    public ClawVision(Claw subsystem, Vision subsystem1) {
//        claw = subsystem;
//        vision = subsystem1;
//
//        addRequirements(subsystem,subsystem1);
//    }
//
//    @Override
//    public void initialize() {
//        //turn outtake on
//        double angle = vision.getTurnServoDegree();
//
//        angle = Math.max(0, Math.min(angle, 360));
//        double perpangle = (angle + 90) % 360;
//        double servoPos = perpangle / 360.0;
//
//        claw.setServoPosRot(servoPos);
//
//    }
//
//    @Override
//    public boolean isFinished() {
//        return true;
//    }
//
//
//}
//
//
