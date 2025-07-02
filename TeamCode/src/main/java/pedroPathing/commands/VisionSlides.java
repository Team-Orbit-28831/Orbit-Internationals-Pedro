package pedroPathing.commands;
import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import pedroPathing.SUBSYSTEMS.CascadeSlides;
import pedroPathing.SUBSYSTEMS.Vision;

public class VisionSlides extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final CascadeSlides cascadeSlidesE;
    private double target = 0;

    private final Vision vision;
    public VisionSlides(CascadeSlides subsystem, Vision subsystem1) {
        cascadeSlidesE = subsystem;
        vision = subsystem1;


        addRequirements(subsystem,subsystem1);
    }

    @Override
    public void initialize() {
        if (vision.getDistance() != 0) {
             target = vision.getDistance();
        } else {
            target = 700;
        }

        cascadeSlidesE.setSlideTarget((int)Math.round(target+110));



    }

    @Override
    public boolean isFinished() {
        return true;
    }
}




