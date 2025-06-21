package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;
import pedroPathing.SUBSYSTEMS.CascadePivot;

public class PivotReset extends CommandBase {
    private final CascadePivot pivot;


    public PivotReset(CascadePivot subsystem) {
        pivot = subsystem;

        addRequirements(subsystem);
    }


    @Override
    public void initialize() {
        //turn outtake on
        pivot.pivotReset();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
