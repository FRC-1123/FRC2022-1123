package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BallSubsystem;
import java.util.logging.Logger;


/**
 * An example command that uses an example subsystem.
 */
public class RaiseBallsPos extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  BallSubsystem balls;
  double position;
  double lifterStartPosition;
  NetworkTableEntry positionEntry;
  public RaiseBallsPos(BallSubsystem balls, NetworkTableEntry position) {
      this.balls = balls;
      // this.position = position.getDouble(0);
      this.position = 0;
      this.positionEntry = position;
      lifterStartPosition = balls.getStartPosition();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(balls);
  }

  public RaiseBallsPos(BallSubsystem balls, double position) {
    this.balls = balls;
    this.position = position * 24576;
    lifterStartPosition = balls.getStartPosition();
    addRequirements(balls);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("Got to Raise balls activate");

    if(positionEntry != null){
      logger.info("IN POSITION ENTRY");
      position = positionEntry.getDouble(2) *24576;
    }
    lifterStartPosition = balls.getStartPosition();
    runMotor();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    time++;
    runMotor();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    balls.stopLifter();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(Math.abs(balls.getLiftMotorPosition()-lifterStartPosition - position) < 500){
      return true;
    }
    return false;
  }

  private void runMotor(){
    if(balls.getLiftMotorPosition()-lifterStartPosition > position){
      if(Math.abs(balls.getLiftMotorPosition()-lifterStartPosition - position) > 32000){
        balls.driveLifter(-1);
      }
      else if(Math.abs(balls.getLiftMotorPosition()-lifterStartPosition - position) < 7000){
        balls.driveLifter(-0.15);
      }
      else{
        balls.driveLifter(-0.3);
      }
    }
    else{
      if(Math.abs(balls.getLiftMotorPosition()-lifterStartPosition - position) > 30000){
        balls.driveLifter(1);
      }
      else if(Math.abs(balls.getLiftMotorPosition()-lifterStartPosition - position) < 5000){
        balls.driveLifter(.15);
      }
      else{
        balls.driveLifter(0.3);
      }
    }
  }
  protected void setPositionGoal(double inputPosition) {
      this.position = inputPosition * 24576;
  }
}