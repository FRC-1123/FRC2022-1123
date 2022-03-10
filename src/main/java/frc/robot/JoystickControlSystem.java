package frc.robot;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
// import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.RaiseBallsPos;
import frc.robot.commands.RaiseBallsToggle;
import frc.robot.commands.RaiseHooksPos;
// import frc.robot.commands.*;
import frc.robot.subsystems.*;

public class JoystickControlSystem {
  public static Joystick driverJoystick;
  private static XboxController xboxController;

  public static void initialize(Joystick driverJoystickInput, MecanumDriveSubsystem driveSubsystemInput, GyroSubsystem Gyro, BallSubsystem ballSubsystem, XboxController xboxControllerInput, ArmLifterSubsystem lifter) {
    Logger logger = Logger.getLogger(frc.robot.JoystickControlSystem.class.getName());

    driverJoystick = driverJoystickInput;
    xboxController = xboxControllerInput;
    MecanumDriveSubsystem driveSubsystem = driveSubsystemInput;

    logger.info("Mecanum drive subsystem defaulting to driveCartesian.");
    RunCommand driveCommand = new RunCommand(() -> driveSubsystem.driveCartesian(-driverJoystick.getX(),
    driverJoystick.getY(), driverJoystick.getZ()/2, (1 - driverJoystick.getThrottle()) / 2), driveSubsystem);
    driveCommand.setName("Joystick drive");
    driveSubsystem.setDefaultCommand(driveCommand);
    // ballSubsystem.setDefaultCommand(new RunCommand(() -> ballSubsystem.spinIntake(
    //   Math.abs(driverJoystick.getY()*((1 - driverJoystick.getThrottle()) / 4))+0.1), ballSubsystem));
    
    ballSubsystem.setDefaultCommand(new RunCommand(() -> {
      if((lifter.getLiftMotorPosition() - lifter.getStartPosition()) / 24576 < Constants.intakeLifterDownPosition + 1)
        ballSubsystem.spinIntake(0.4);
      else
        ballSubsystem.spinIntake(0);
      }, ballSubsystem));

    // logger.info("Driver button 10 bound to pivotCartesian");
    // JoystickButton driveModeButton = new JoystickButton(driverJoystick, 10);
    // driveModeButton.whenHeld(new RunCommand(() -> driveSubsystem.pivotCartesian(-driverJoystick.getX(),
    //     driverJoystick.getY(), driverJoystick.getZ(), (1 - driverJoystick.getThrottle()) / 2), driveSubsystem));

    // JoystickButton fieldOrientedButton = new JoystickButton(driverJoystick, 3);
    // fieldOrientedButton.whenHeld(new RunCommand(() -> driveSubsystem.fieldCartesian(-driverJoystick.getX(),
    //   driverJoystick.getY(), driverJoystick.getZ(), (1 - driverJoystick.getThrottle()) / 2, Gyro.getAngle()), driveSubsystem));
    
    RunCommand shootBallCommand = new RunCommand(() -> ballSubsystem.spinIntake(-1), ballSubsystem);
    JoystickButton shootBall = new JoystickButton(driverJoystick, 1);
    shootBall.whenHeld(shootBallCommand);
    
    RaiseBallsToggle controllerRaiseBallsToggle = new RaiseBallsToggle(lifter, Constants.intakeLifterTopPosition, 0.7);
    JoystickButton toggleBallPosition = new JoystickButton(driverJoystick, 2);
    toggleBallPosition.whenPressed(controllerRaiseBallsToggle);
    JoystickButton xboxToggleBallPosition = new JoystickButton(xboxController, 1);
    xboxToggleBallPosition.whenPressed(controllerRaiseBallsToggle);

    JoystickButton putBallsDown = new JoystickButton(driverJoystick, 3);
    putBallsDown.whenPressed(new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 0.7));
    // JoystickButton xboxPutBallsDown = new JoystickButton(xboxController, 3);
    // xboxPutBallsDown.whenPressed(new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 0.6));

    JoystickButton putBallsUp = new JoystickButton(driverJoystick, 4);
    putBallsUp.whenPressed(new RaiseBallsPos(lifter, Constants.intakeLifterTopPosition, 0.7));
    JoystickButton xboxPutBallsUp = new JoystickButton(xboxController, 2);
    xboxPutBallsUp.whenPressed(new RaiseBallsPos(lifter, Constants.intakeLifterTopPosition, 0.7));

    RaiseBallsPos armClimbPosition = new RaiseBallsPos(lifter, -6.5, 0.4);
    JoystickButton climb1 = new JoystickButton(driverJoystick, 5);
      climb1.whenPressed(armClimbPosition);

      JoystickButton climb2 = new JoystickButton(driverJoystick, 6);
      climb2.whenPressed(armClimbPosition);

      JoystickButton climb3 = new JoystickButton(driverJoystick, 7);
      climb3.whenPressed(armClimbPosition);

    RaiseBallsPos armDrivePosition = new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition + 2, 0.4);

    JoystickButton drivePosition1 = new JoystickButton(driverJoystick, 11);
    drivePosition1.whenPressed(armDrivePosition);
    JoystickButton drivePosition2 = new JoystickButton(driverJoystick, 12);
    drivePosition2.whenPressed(armDrivePosition);
    JoystickButton drivePosition3 = new JoystickButton(driverJoystick, 13);
    drivePosition3.whenPressed(armDrivePosition);
  }

  public static double getThrottle() {
    return driverJoystick.getThrottle();
  }
}

// climb.whileHeld(new StartEndCommand(() -> lifter.driveLifter(-0.35), () -> lifter.driveLifter(0), lifter));