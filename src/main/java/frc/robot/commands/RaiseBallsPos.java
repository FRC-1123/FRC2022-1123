package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
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
    this.position = position * 20480;
    this.maxSpeed = maxSpeed;
    lifterStartPosition = lifter.getStartPosition();
    addRequirements(lifter);
  }
  double startTime;
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("Got to Raise balls activate");
    startTime = Timer.getFPGATimestamp();
    if(positionEntry != null){
      position = positionEntry.getDouble(2) * 20480;
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
    logger.info("time taken = " + (Timer.getFPGATimestamp() - startTime));
    lifter.stopLifter();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(Math.abs(motorPosition-lifterStartPosition - position) < 250){
      logger.info("End position " + lifter.getLiftMotorPosition());
      return true;
    }
    return false;
  }
  double motorPosition;

  private void runMotor(){
    if(motorPosition-lifterStartPosition > position){
      if(Math.abs(motorPosition-lifterStartPosition - position) > 35000){ // going down overshoot 2000
        lifter.driveLifter(-maxSpeed);
      }
      // else if(Math.abs(motorPosition-lifterStartPosition - position) > 23000){
      //   lifter.driveLifter(-maxSpeed/2 - 0.05);
      // }
      else if(Math.abs(motorPosition-lifterStartPosition - position) < 7000){
        lifter.driveLifter(-0.23);
      }
      else{
        lifter.driveLifter(-0.3);
      }
    }
    else{
      if(Math.abs(motorPosition-lifterStartPosition - position) > 30000){ //going up tick 2000
        lifter.driveLifter(maxSpeed);
      }
      // else if(Math.abs(motorPosition-lifterStartPosition - position) > 23000){
      //   lifter.driveLifter(maxSpeed/2 + 0.05);
      // }
      else if(Math.abs(motorPosition-lifterStartPosition - position) < 4000){
        lifter.driveLifter(.23);
      }
      else{
        lifter.driveLifter(0.3);
      }
    }
  }
  
  protected void setPositionGoal(double inputPosition) {
      this.position = inputPosition * 24576;
  }
}