package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.drive.AdvancedMecanumDrive;

import java.util.logging.Logger;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.CalebFalcon;
import frc.robot.Constants;

public class MecanumDriveSubsystem extends SubsystemBase {
  private final Logger logger = Logger.getLogger(this.getClass().getName());
  private AdvancedMecanumDrive m_robotDrive;

  /**
   * Creates a new MecanumDriveSubsystem.
   */
  CalebFalcon frontLeft;
  CalebFalcon rearLeft;
  CalebFalcon frontRight;
  CalebFalcon rearRight;
    SendableChooser<Boolean> showValues;
  public MecanumDriveSubsystem() {
    frontLeft = new CalebFalcon(Constants.frontLeftDriveMotorCanID, false);
    rearLeft = new CalebFalcon(Constants.rearLeftDriveMotorCanID, false);
    frontRight = new CalebFalcon(Constants.frontRightDriveMotorCanID, true);
    rearRight = new CalebFalcon(Constants.rearRightDriveMotorCanID, true);

    frontLeft.setNeutralMode(NeutralMode.Brake);
    rearLeft.setNeutralMode(NeutralMode.Brake);
    frontRight.setNeutralMode(NeutralMode.Brake);
    rearRight.setNeutralMode(NeutralMode.Brake);

    m_robotDrive = new AdvancedMecanumDrive(frontLeft, rearLeft, frontRight, rearRight);
    setName("Drive Subsystem");
    showValues = new SendableChooser<Boolean>();
    showValues.setDefaultOption("false", false);
    showValues.addOption("true", true);
    SmartDashboard.putData("Mecanum Subsystem show value", showValues);
    logger.info("The mecanum drive subsystem is initialized.");
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(showValues.getSelected()){
      updateSmartDashboard();
    }
  }

  public void updateSmartDashboard(){
    SmartDashboard.putNumber("Front Left Speed", frontLeft.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Front Right Speed", frontRight.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Rear Left Speed", rearLeft.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Rear Right Speed", rearRight.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Front Left Position", frontLeft.getSelectedSensorPosition());
    SmartDashboard.putNumber("Front Right Position", frontRight.getSelectedSensorPosition());
    SmartDashboard.putNumber("Rear Left Position", rearLeft.getSelectedSensorPosition());
    SmartDashboard.putNumber("Rear Right Position", rearRight.getSelectedSensorPosition());
  }

  public double getAverageSpeed(){
    double average = (Math.abs(frontLeft.getSelectedSensorVelocity()) + 
     Math.abs(frontRight.getSelectedSensorVelocity()) + 
     Math.abs(rearRight.getSelectedSensorVelocity()) +
     Math.abs(rearLeft.getSelectedSensorVelocity()))/4;
    return average;
  }

  public double getFrontLeftDistance(){
    return frontLeft.getDistance();
  }

  public double getFrontRightDistance(){
    return frontRight.getDistance();
  }

  public double getRearRightDistance(){
    return rearRight.getDistance();
  }

  public double getRearLeftDistance(){
    return rearLeft.getDistance();
  }

  public double getAverageDistance(){
    return (getFrontLeftDistance() + getFrontRightDistance() + getRearRightDistance() + getRearLeftDistance())/4;
  }

  public void resetMotorPosition(){
    frontLeft.resetDistance();
    frontRight.resetDistance();
    rearLeft.resetDistance();
    rearRight.resetDistance();
  }

  public void driveCartesian(double yval, double xval, double zval, double throttle) {
    //logger.info(String.format("X: %s, Y: %s, Z: %s, G: %s", xval, yval, zval, gyroval));
    m_robotDrive.driveCartesian(-yval, -xval, zval, throttle);
  }

  public void fieldCartesian(double yval, double xval, double zval, double throttle, double angle) {
    //logger.info(String.format("X: %s, Y: %s, Z: %s, G: %s", xval, yval, zval, gyroval));
    m_robotDrive.driveCartesian(-yval, -xval, zval, throttle, angle);
  }

  public void pivotCartesian(double yval, double xval, double zval, double throttle) {
    //logger.info(String.format("X: %s, Y: %s, Z: %s, G: %s", xval, yval, zval, gyroval));
    m_robotDrive.pivotCartesian(-yval, -xval, zval, throttle);
  }

  public void drivePolar(double magnitude, double angle, double zRotation){
    m_robotDrive.drivePolar(magnitude, angle, zRotation);
  }
}