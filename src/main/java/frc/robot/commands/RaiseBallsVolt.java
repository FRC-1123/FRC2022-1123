package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BallSubsystem;

import java.util.logging.Logger;

public class RaiseBallsVolt extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  BallSubsystem balls;
  NetworkTableEntry position;
  NetworkTableEntry k_P;
  public RaiseBallsVolt(BallSubsystem balls, NetworkTableEntry position) {
      this.balls = balls;
      this.position = position;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(balls);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("got to motor Activate");
    balls.lift(position.getDouble(balls.getStartPosition()));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    time++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(time > 5){
      return true;
    }
    return false;
  }
}