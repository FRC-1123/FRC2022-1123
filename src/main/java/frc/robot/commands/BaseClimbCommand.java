package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.BallSubsystem;
import frc.robot.subsystems.ClimbingSubsystem;
import frc.robot.subsystems.MecanumDriveSubsystem;

// import java.util.logging.Logger;


/**
 * An example command that uses an example subsystem.
 */
public class BaseClimbCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  ClimbingSubsystem climb;
  MecanumDriveSubsystem drive;
  BallSubsystem balls;
  double driveSpeed;
  double massMoverSpeed;
  double driveDistance;
  double massMoverDistance;
  double hookDistance;
  double intakeLifterDistance;
  DriveStraightPos driveCommand;
  MoveMassMoversPos moveMassCommand;
  RaiseBallsPos intakeLiftCommand;
  RaiseHooksPos raiseHooksCommand;
  /**
   * @param driveDistance how far to drive in inches
   * @param massMoverDistance position of the mass mover from starting position in degrees
   * @param hookDistance how far from the start to release winches
   * @param intakeLifterDistance where to put intake relative to the lowest point in inches
   * @param driveSpeed how fast to move in percent
   * @param massMoverSpeed how fast to move in percent
   */
  public BaseClimbCommand(ClimbingSubsystem climb, MecanumDriveSubsystem drive, BallSubsystem balls,
   double driveDistance, double massMoverDistance, double hookDistance, double intakeLifterDistance,
   NetworkTableEntry driveSpeed, NetworkTableEntry massMoverSpeed) {
    this.climb = climb;
    this.drive = drive;
    this.balls = balls;
    this.driveSpeed = driveSpeed.getDouble(0);
    this.massMoverSpeed = massMoverSpeed.getDouble(0);
    this.driveDistance = driveDistance;
    this.massMoverDistance = massMoverDistance;
    this.hookDistance = hookDistance;
    this.intakeLifterDistance = intakeLifterDistance;
    driveCommand = new DriveStraightPos(drive, driveDistance, driveSpeed);
    moveMassCommand = new MoveMassMoversPos(climb, massMoverDistance, massMoverSpeed);
    intakeLiftCommand = new RaiseBallsPos(balls, intakeLifterDistance, 1);
    raiseHooksCommand = new RaiseHooksPos(climb, hookDistance);
  }

  public BaseClimbCommand(ClimbingSubsystem climb, MecanumDriveSubsystem drive, BallSubsystem balls,
   double driveDistance, double massMoverDistance, double hookDistance, double intakeLifterDistance,
   double driveSpeed, double massMoverSpeed) {
    this.climb = climb;
    this.drive = drive;
    this.balls = balls;
    this.driveSpeed = driveSpeed;
    this.massMoverSpeed = massMoverSpeed;
    this.driveDistance = driveDistance;
    this.massMoverDistance = massMoverDistance;
    this.hookDistance = hookDistance;
    this.intakeLifterDistance = intakeLifterDistance;
    driveCommand = new DriveStraightPos(drive, driveDistance, driveSpeed);
    moveMassCommand = new MoveMassMoversPos(climb, massMoverDistance, massMoverSpeed);
    intakeLiftCommand = new RaiseBallsPos(balls, intakeLifterDistance, 1);
    raiseHooksCommand = new RaiseHooksPos(climb, hookDistance);
 }

  public BaseClimbCommand(ClimbingSubsystem climb, BallSubsystem balls, double massMoverDistance,
   double hookDistance, double intakeLifterDistance, double massMoverSpeed) {
    this.climb = climb;
    this.balls = balls;
    this.massMoverSpeed = massMoverSpeed;
    this.massMoverDistance = massMoverDistance;
    this.hookDistance = hookDistance;
    this.intakeLifterDistance = intakeLifterDistance;
    moveMassCommand = new MoveMassMoversPos(climb, massMoverDistance, massMoverSpeed);
    intakeLiftCommand = new RaiseBallsPos(balls, intakeLifterDistance, 1);
    raiseHooksCommand = new RaiseHooksPos(climb, hookDistance);
  }

  /**
 * 
 * @param massMoverDistance position of the mass mover from starting position in degrees
 * @param hookDistance how far from the start to release winches
 * @param intakeLifterDistance where to put intake relative to the lowest point in inches
 * @param massMoverSpeed how fast to move in percent
 */
public BaseClimbCommand(ClimbingSubsystem climb, BallSubsystem balls, double massMoverDistance,
 double hookDistance, double intakeLifterDistance, NetworkTableEntry massMoverSpeed) {
  this.climb = climb;
  this.balls = balls;
  this.massMoverSpeed = massMoverSpeed.getDouble(0.1);
  this.massMoverDistance = massMoverDistance;
  this.hookDistance = hookDistance;
  this.intakeLifterDistance = intakeLifterDistance;
  moveMassCommand = new MoveMassMoversPos(climb, massMoverDistance, massMoverSpeed);
  intakeLiftCommand = new RaiseBallsPos(balls, intakeLifterDistance, 1);
  raiseHooksCommand = new RaiseHooksPos(climb, hookDistance);
}

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(driveCommand != null){
      CommandScheduler.getInstance().schedule(driveCommand, moveMassCommand, intakeLiftCommand,
       raiseHooksCommand);
    }
    else{
      CommandScheduler.getInstance().schedule(moveMassCommand, intakeLiftCommand, raiseHooksCommand);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(driveCommand != null){
      if(driveCommand.isFinished() && moveMassCommand.isFinished() && intakeLiftCommand.isFinished() &&
       raiseHooksCommand.isFinished()){
        return true;
      }
    }
    else{
      if(moveMassCommand.isFinished() && intakeLiftCommand.isFinished() && raiseHooksCommand.isFinished()){
        return true;
      }
    }
    return false;
  }
}