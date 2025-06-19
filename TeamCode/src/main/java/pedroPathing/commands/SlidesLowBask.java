package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;

import pedroPathing.SUBSYSTEMS.CascadeSlides;

public class SlidesLowBask extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadeSlides cascadeSlidesE;

    public SlidesLowBask(CascadeSlides subsystem) {
        cascadeSlidesE = subsystem;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        cascadeSlidesE.slideLowBasket();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}




