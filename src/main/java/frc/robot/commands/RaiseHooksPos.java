package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimbingSubsystem;
// import java.util.logging.Logger;


/**
 * An example command that uses an example subsystem.
 */
public class RaiseHooksPos extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // private final Logger logger = Logger.getLogger(this.getClass().getName());
  int time = 0;
  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  ClimbingSubsystem climb;
  double position;
  NetworkTableEntry positionEntry;
  public RaiseHooksPos(ClimbingSubsystem climb, NetworkTableEntry position) {
      this.climb = climb;
      this.position = position.getDouble(0);
      this.positionEntry = position;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climb);
    }

    public RaiseHooksPos(ClimbingSubsystem climb, double position) {
      this.climb = climb;
      this.position = position;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climb);
    }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(positionEntry != null){
      position = positionEntry.getDouble(0);
    }
    position = position * 43481.95;
    move();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    move();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climb.stopWinch();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    double delta = climb.getWinchPosition()-climb.getWinchStartPosition() - position;
    if(Math.abs(delta) < 4000){
      return true;
    }
    return false;
  }

  private void move(){
    double delta = climb.getWinchPosition()-climb.getWinchStartPosition() - position;
    if(delta > 0){
      if(Math.abs(delta) > 40000)
        climb.runWinch(1);
      else if(Math.abs(delta) < 7000)
        climb.runWinch(0.3);
      else 
        climb.runWinch(0.7);
    }
    else {
      if(Math.abs(delta) > 40000)
        climb.runWinch(-1);
      else if(Math.abs(delta) < 7000)
        climb.runWinch(-0.3);
      else
        climb.runWinch(-0.7);
    }
  }
}