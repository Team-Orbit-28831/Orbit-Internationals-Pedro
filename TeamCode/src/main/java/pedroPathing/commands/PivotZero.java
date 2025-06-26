package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;
import pedroPathing.SUBSYSTEMS.CascadePivot;

public class PivotZero extends CommandBase {
    private final CascadePivot pivot;


    public PivotZero(CascadePivot subsystem) {
        pivot = subsystem;

        addRequirements(subsystem);
    }


    @Override
    public void initialize() {
        //turn outtake on
        pivot.pivotZero();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
