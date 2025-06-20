package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Claw;

public class SlideRetract extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadeSlides slides;


    public SlideRetract(CascadeSlides subsystem) {
        slides = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        //turn outtake on
        slides.retract();
    }

    @Override
    public boolean isFinished() {
        return true;
    }


}


