package frc.robot;

import java.util.logging.Logger;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DriveStraightPos;
import frc.robot.commands.GyroTurn;
import frc.robot.commands.RaiseBallsPos;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RobotContainer {
  Logger logger = Logger.getLogger(frc.robot.RobotContainer.class.getName());
  private static final MecanumDriveSubsystem drive = new MecanumDriveSubsystem();
  private static final GyroSubsystem gyro = new GyroSubsystem();
  private static final BallSubsystem ballSubysytem = new BallSubsystem();
  private static final ClimbingSubsystem climb = new ClimbingSubsystem();

  public static void initialize() {
    JoystickControlSystem.initialize(new Joystick(Constants.joystickChannel), drive, gyro, ballSubysytem, new XboxController(Constants.xBoxChannel));
    DashboardControlSystem.initialize(drive, gyro, ballSubysytem, climb);
  }

  public static void testModeDisplayInitialize(){
    DashboardControlSystem.testModeDisplayInitialize(drive, gyro, ballSubysytem);
  }

  public static void testModeDisplayPeriodic(){
    DashboardControlSystem.testModeDisplayInitialize(drive, gyro, ballSubysytem);
  }

  // public static Sendable getAutonomousCommand(){
  //   gyro.calibrateGyro();
  //   if(true){
  //     ParallelCommandGroup step1 = new ParallelCommandGroup(new DriveStraightPos(drive, 6, 0.4), new RaiseBallsPos(ballSubysytem, Constants.intakeLifterDownPosition));
  //     ParallelCommandGroup step4 = new ParallelCommandGroup(new DriveStraightPos(drive, 45, 0.4), new RaiseBallsPos(ballSubysytem, Constants.intakeLifterTopPosition));
  //     return new SequentialCommandGroup(step1, new DriveStraightPos(drive, 12, 0.4), new GyroTurn(drive, gyro, 180), step4);
  //   }
  // }
}