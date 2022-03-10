package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmLifterSubsystem;
import java.util.logging.Logger;


/**
 * An example command that uses an example subsystem.
 */
public class RaiseBallsPos extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  ArmLifterSubsystem lifter;
  double position;
  double maxSpeed;
  double lifterStartPosition;
  NetworkTableEntry positionEntry;
  /**
   * @param position position for arm to go to in inches. anything positive goes above starting configuration
   * @param maxSpeed max speed motor runs at.
   */
  public RaiseBallsPos(ArmLifterSubsystem lifter, NetworkTableEntry position, double maxSpeed) {
      this.lifter = lifter;
      // this.position = position.getDouble(0);
      this.position = 0;
      this.positionEntry = position;
      this.maxSpeed = maxSpeed;
      lifterStartPosition = lifter.getStartPosition();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(lifter);
  }

  /**
   * @param position position for arm to go to in inches. anything positive goes above starting configuration
   * @param maxSpeed max speed motor runs at.
   */
  public RaiseBallsPos(ArmLifterSubsystem lifter, double position, double maxSpeed) {
    this.lifter = lifter;
    this.position = position * 24576;
    this.maxSpeed = maxSpeed;
    lifterStartPosition = lifter.getStartPosition();
    addRequirements(lifter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("Got to Raise balls activate");

    if(positionEntry != null){
      position = positionEntry.getDouble(2) * 24576;
    }
    lifterStartPosition = lifter.getStartPosition();
    runMotor();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    time++;
    motorPosition = lifter.getLiftMotorPosition();
    runMotor();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    lifter.stopLifter();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(Math.abs(motorPosition-lifterStartPosition - position) < 500){
      return true;
    }
    return false;
  }
  double motorPosition;

  private void runMotor(){
    if(motorPosition-lifterStartPosition > position){
      if(Math.abs(motorPosition-lifterStartPosition - position) > 32000){
        lifter.driveLifter(-maxSpeed);
      }
      else if(Math.abs(motorPosition-lifterStartPosition - position) > 25000){
        lifter.driveLifter(-maxSpeed/2 - 0.05);
      }
      else if(Math.abs(motorPosition-lifterStartPosition - position) < 7000){
        lifter.driveLifter(-0.15);
      }
      else{
        lifter.driveLifter(-0.2);
      }
    }
    else{
      if(Math.abs(motorPosition-lifterStartPosition - position) > 32000){
        lifter.driveLifter(maxSpeed);
      }
      else if(Math.abs(motorPosition-lifterStartPosition - position) > 25000){
        lifter.driveLifter(maxSpeed/2 + 0.05);
      }
      else if(Math.abs(motorPosition-lifterStartPosition - position) < 5000){
        lifter.driveLifter(.15);
      }
      else{
        lifter.driveLifter(0.2);
      }
    }
  }
  protected void setPositionGoal(double inputPosition) {
      this.position = inputPosition * 24576;
  }
}