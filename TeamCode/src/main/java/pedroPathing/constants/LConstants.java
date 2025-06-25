package pedroPathing.constants;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class LConstants {
    static {
        TwoWheelConstants.forwardTicksToInches = .0019;
        TwoWheelConstants.strafeTicksToInches = 0.002977600645084359
        ;
        TwoWheelConstants.forwardY = 2.3;
        TwoWheelConstants.strafeX = -2.8;
        TwoWheelConstants.forwardEncoder_HardwareMapName = "front_right_drive";
        TwoWheelConstants.strafeEncoder_HardwareMapName = "front_left_drive";
        TwoWheelConstants.forwardEncoderDirection = Encoder.FORWARD;
        TwoWheelConstants.strafeEncoderDirection = Encoder.FORWARD;
        TwoWheelConstants.IMU_HardwareMapName = "imu";
        TwoWheelConstants.IMU_Orientation = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP);
    }
}
