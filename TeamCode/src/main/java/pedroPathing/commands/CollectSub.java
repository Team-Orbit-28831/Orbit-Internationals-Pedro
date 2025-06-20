package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadePivot;
import pedroPathing.SUBSYSTEMS.Claw;

public class CollectSub extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final Claw claw;
    private final CascadePivot pivot;


    public CollectSub(Claw subsystem1, CascadePivot subsystem2) {
        claw = subsystem1;
        pivot = subsystem2;

        addRequirements(subsystem1, subsystem2);
    }

    @Override
    public void initialize() {
        //turn outtake on
        pivot.pivotToCollect();
    }

    @Override
    public boolean isFinished() {
        return true;
    }


}


