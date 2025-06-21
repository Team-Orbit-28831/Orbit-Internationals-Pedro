package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadePivot;

public class m_pivotup extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadePivot cascadePivotE;


    public m_pivotup(CascadePivot subsystem) {
        cascadePivotE = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        //turn outtake on
        cascadePivotE.setPower(1);
    }

    @Override
    public boolean isFinished() {
        cascadePivotE.setPower(0);
        return true;
    }


}


