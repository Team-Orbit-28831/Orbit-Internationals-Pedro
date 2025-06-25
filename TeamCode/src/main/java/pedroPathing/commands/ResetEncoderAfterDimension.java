//package pedroPathing.commands;
//import com.arcrobotics.ftclib.command.CommandBase;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//
//import pedroPathing.SUBSYSTEMS.CascadePivot;
//
//public class ResetEncoderAfterDimension extends CommandBase {
//    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
//    private final CascadePivot cascadePivotE;
//
//
//    public ResetEncoderAfterDimension(CascadePivot subsystem) {
//        cascadePivotE = subsystem;
//        pivotMotorLeft = cascadePivotE.getClass(DcMotorEx.class, "pivotLeft");
//        pivotMotorRight = hardwareMap.get(DcMotorEx.class, "pivotRight");
//
//        pivotMotorLeft.setDirection(DcMotorEx.Direction.REVERSE);
//        pivotMotorRight.setDirection(DcMotorEx.Direction.REVERSE);
//
//        pivotMotorLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//        pivotMotorRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
//
//        addRequirements(subsystem);
//    }
//
//    @Override
//    public void initialize() {
//        //turn outtake on
//        cascadePivotE.setPivotTarget(600);
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
