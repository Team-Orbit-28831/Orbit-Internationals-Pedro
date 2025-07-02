package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.Claw;
import pedroPathing.SUBSYSTEMS.Vision;

public class ClawVision extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final Claw claw;
    private final Vision vision;

    public ClawVision(Claw subsystem, Vision subsystem1) {
        claw = subsystem;
        vision = subsystem1;

        addRequirements(subsystem,subsystem1);
    }

    @Override
    public void initialize() {
        //turn outtake on
        double valuelol = vision.getOrientationFlag();

        if (valuelol == 1){
            claw.perpPos();
        }
        if (valuelol == 0){
            claw.flatPos();
        }
        else{
            claw.perpPos();
        }

        

    }

    @Override
    public boolean isFinished() {
        return true;
    }


}


