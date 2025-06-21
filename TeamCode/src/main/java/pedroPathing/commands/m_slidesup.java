package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Claw;

public class m_slidesup extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadeSlides slides;


    public m_slidesup(CascadeSlides subsystem) {
        slides = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        //turn outtake on
        slides.setPower(0.25);
    }

    @Override
    public boolean isFinished() {
        slides.setPower(0);
        return true;
    }


}


