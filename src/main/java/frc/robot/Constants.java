package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final int frontLeftDriveMotorCanID = 11;
    public static final int rearLeftDriveMotorCanID = 4; //13
    public static final int frontRightDriveMotorCanID = 12;
    public static final int rearRightDriveMotorCanID = 14;

    public static final int intakeBallMotorCanID = 21;
    public static final int intakeLifterMotorCanID = 20;

    public static final int climberMotorCanID = 15;

    public static final int massMoverCanID = 17;

    public static final int joystickChannel = 0;
    public static final int xBoxChannel = 1;

    public static final double intakeLifterTopPosition = 6;
    public static final double intakeLifterDownPosition = 0.2;
}