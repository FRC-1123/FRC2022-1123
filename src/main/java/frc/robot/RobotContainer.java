package frc.robot;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.DriveReverseTime;
import frc.robot.commands.DriveStraightPos;
import frc.robot.commands.GyroTurn;
import frc.robot.commands.RaiseBallsPos;
import frc.robot.commands.ShootBalls;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class RobotContainer {
  Logger logger = Logger.getLogger(frc.robot.RobotContainer.class.getName());
  private static final MecanumDriveSubsystem drive = new MecanumDriveSubsystem();
  private static final GyroSubsystem gyro = new GyroSubsystem();
  private static final BallSubsystem ballSubysytem = new BallSubsystem();
  private static final ArmLifterSubsystem lifter = new ArmLifterSubsystem();
  private static final ClimbingSubsystem climb = new ClimbingSubsystem();
  // private static final MassMoverSubsystem massMover = new MassMoverSubsystem();

  private static SendableChooser<Command> autoChooser;

  public static void initialize() {
    JoystickControlSystem.initialize(new Joystick(Constants.joystickChannel), drive, gyro, ballSubysytem, new XboxController(Constants.xBoxChannel), lifter);
    DashboardControlSystem.initialize(drive, gyro, ballSubysytem, climb, lifter);
    initializeAutonomousChooser();
  }

  private static void initializeAutonomousChooser(){
    autoChooser = new SendableChooser<>();
    SequentialCommandGroup baiscAuto = new SequentialCommandGroup(new ShootBalls(ballSubysytem, 4), new DriveReverseTime(drive, 7, 0.23), new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 0.7));
    autoChooser.setDefaultOption("basic auto", baiscAuto);
    
    // ParallelCommandGroup lastStepLeft = new ParallelCommandGroup(new DriveForwardTime(drive, 2, 0.25), new ShootBalls(ballSubysytem, 2));
    // SequentialCommandGroup complexAutoLeft = new SequentialCommandGroup(new InstantCommand(() -> gyro.calibrateGyro()), new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 1), new DriveStraightPos(drive, 45, 0.4),
    //  new GyroTurn(drive, gyro, 175), new DriveStraightPos(drive, 80, 0.4), new RaiseBallsPos(lifter, Constants.intakeLifterTopPosition, 1), new GyroTurn(drive, gyro, 202), lastStepLeft);

    ParallelCommandGroup leftStep = new ParallelCommandGroup(new DriveStraightPos(drive, 110, 0.3), new RaiseBallsPos(lifter, Constants.intakeLifterTopPosition, 1));
    SequentialCommandGroup complexAutoLeft = new SequentialCommandGroup(
      new InstantCommand(() -> gyro.calibrateGyro()),
      new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 1),
      new InstantCommand(() -> ballSubysytem.spinIntake(0.4)),
      new GyroTurn(drive, gyro, 350),//344
      new DriveStraightPos(drive, 67, 0.4),
      new GyroTurn(drive, gyro, 152),
      leftStep,
      new ShootBalls(ballSubysytem, 1),
      new DriveStraightPos(drive, -20, 0.4),
      new GyroTurn(drive, gyro, 339),
      new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 1));

    ParallelCommandGroup rightStep = new ParallelCommandGroup(new DriveStraightPos(drive, 88, 0.3), new RaiseBallsPos(lifter, Constants.intakeLifterTopPosition, 1));
    SequentialCommandGroup complexAutoRight = new SequentialCommandGroup(
      new InstantCommand(() -> gyro.calibrateGyro()),
      new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 1),
      new InstantCommand(() -> ballSubysytem.spinIntake(0.4)),
      new GyroTurn(drive, gyro, 354),
      new DriveStraightPos(drive, 65, 0.4),
      new GyroTurn(drive, gyro, 190),
      rightStep,
      new GyroTurn(drive, gyro, 135),
      new DriveStraightPos(drive, 25, 0.35),
      new ShootBalls(ballSubysytem, 1),
      new DriveStraightPos(drive, -20, 0.4),
      new GyroTurn(drive, gyro, 315),
      new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 1));

    // ParallelCommandGroup step2 = new ParallelCommandGroup(new DriveStraightPos(drive, 6, 0.3), new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 1));
    // ParallelCommandGroup step5 = new ParallelCommandGroup(new DriveStraightPos(drive, 80, 0.4), new RaiseBallsPos(lifter, Constants.intakeLifterTopPosition, 0.7));//slowed down cause unnessasry speed also keeps center of gravity lower for longer
    // ParallelCommandGroup lastStep = new ParallelCommandGroup(new DriveForwardTime(drive, 0.5, 0.25), new ShootBalls(ballSubysytem, 1));
    // SequentialCommandGroup complexAutoRight = new SequentialCommandGroup(new InstantCommand(() -> gyro.calibrateGyro()), step2, new DriveStraightPos(drive, 8, 0.4),
    //  new GyroTurn(drive, gyro, 175), step5, new GyroTurn(drive, gyro, 202), lastStep);

    //  ParallelCommandGroup step5Left = new ParallelCommandGroup(new DriveStraightPos(drive, 95, 0.6), new RaiseBallsPos(lifter, Constants.intakeLifterTopPosition, 0.7));//slowed down cause unnessasry speed also keeps center of gravity lower for longer
    // SequentialCommandGroup complexAutoLeft = new SequentialCommandGroup(new InstantCommand(() -> gyro.calibrateGyro()), step2, new DriveStraightPos(drive, 40, 0.4),
    // new GyroTurn(drive, gyro, 185), step5Left, new GyroTurn(drive, gyro, 158), lastStep);

    // ParallelCommandGroup step2Left = new ParallelCommandGroup(new DriveStraightPos(drive, 6, 0.4), new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 1));
    // ParallelCommandGroup step5Left = new ParallelCommandGroup(new DriveStraightPos(drive, 55, 0.6), new RaiseBallsPos(lifter, Constants.intakeLifterTopPosition, 0.7));//slowed down cause unnessasry speed also keeps center of gravity lower for longer
    // ParallelCommandGroup secondToLastStepLeft = new ParallelCommandGroup(new DriveStraightPos(drive, 45, 0.4));
    // ParallelCommandGroup lastStepLeft = new ParallelCommandGroup(new DriveForwardTime(drive, 1, 0.3), new ShootBalls(ballSubysytem, 1.5));
    // SequentialCommandGroup complexAutoLeft = new SequentialCommandGroup(new InstantCommand(() -> gyro.calibrateGyro()), step2Left, new DriveStraightPos(drive, 50, 0.4),
    //   new GyroTurn(drive, gyro, 180), step5Left, new GyroTurn(drive, gyro, 202), secondToLastStepLeft, lastStepLeft);

    autoChooser.addOption("Complex auto right", complexAutoRight);
    autoChooser.addOption("Complex auto left", complexAutoLeft);
    autoChooser.addOption("Drive forward", new DriveStraightPos(drive, 70, 0.3));
    SmartDashboard.putData("Autonomous Chooser", autoChooser);

  }
  public static Command getAutonomousCommand(){
    // SequentialCommandGroup auto = new SequentialCommandGroup(new ShootBalls(ballSubysytem, 4), new DriveReverseTime(drive, 7, 0.23), new RaiseBallsPos(lifter, Constants.intakeLifterDownPosition, 0.7));
    // return auto;
    return autoChooser.getSelected();
  }

  public static void setTeleTime(double time){
    DashboardControlSystem.setTeleTime(time);
  }
}