package frc.robot.drive;

import java.util.StringJoiner;
// import java.util.logging.Logger;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

/**
 * A class for driving Mecanum drive platforms.
 *
 * <p>Mecanum drives are rectangular with one wheel on each corner. Each wheel has rollers toed in
 * 45 degrees toward the front or back. When looking at the wheels from the top, the roller axles
 * should form an X across the robot. Each drive() function provides different inverse kinematic
 * relations for a Mecanum drive robot.
 *
 * <p>Drive base diagram:
 * <pre>
 * \\_______/
 * \\ |   | /
 *   |   |
 * /_|___|_\\
 * /       \\
 * </pre>
 *
 * <p>Each drive() function provides different inverse kinematic relations for a Mecanum drive
 * robot. Motor outputs for the right side are negated, so motor direction inversion by the user is
 * usually unnecessary.
 *
 * <p>This library uses the NED axes convention (North-East-Down as external reference in the world
 * frame): http://www.nuclearprojects.com/ins/images/axis_big.png.
 *
 * <p>The positive X axis points ahead, the positive Y axis points right, and the positive Z axis
 * points down. Rotations follow the right-hand rule, so clockwise rotation around the Z axis is
 * positive.
 *
 * <p>Inputs smaller then {@value edu.wpi.first.wpilibj.drive.RobotDriveBase#kDefaultDeadband} will
 * be set to 0, and larger values will be scaled so that the full range is still used. This
 * deadband value can be changed with {@link #setDeadband}.
 *
 * <p>RobotDrive porting guide:
 * <br>In MecanumDrive, the right side speed controllers are automatically inverted, while in
 * RobotDrive, no speed controllers are automatically inverted.
 * <br>{@link #driveCartesian(double, double, double, double)} is equivalent to
 * {@link edu.wpi.first.wpilibj.RobotDrive#mecanumDrive_Cartesian(double, double, double, double)}
 * if a deadband of 0 is used, and the ySpeed and gyroAngle values are inverted compared to
 * RobotDrive (eg driveCartesian(xSpeed, -ySpeed, zRotation, -gyroAngle).
 * <br>{@link #drivePolar(double, double, double)} is equivalent to
 * {@link edu.wpi.first.wpilibj.RobotDrive#mecanumDrive_Polar(double, double, double)} if a
 * deadband of 0 is used.
 */
public class AdvancedMecanumDrive extends RobotDriveBase implements Sendable, AutoCloseable {
  // private final Logger logger = Logger.getLogger(this.getClass().getName());
  private static int instances;

  private final MotorController m_frontLeftMotor;
  private final MotorController m_rearLeftMotor;
  private final MotorController m_frontRightMotor;
  private final MotorController m_rearRightMotor;

  private double m_rightSideInvertMultiplier = -1.0;
  private boolean m_reported;

  /**
   * Construct a MecanumDrive.
   *
   * <p>If a motor needs to be inverted, do so before passing it in.
   */
  public AdvancedMecanumDrive(MotorController frontLeftMotor, MotorController rearLeftMotor,
            MotorController frontRightMotor, MotorController rearRightMotor) {
    verify(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    m_frontLeftMotor = frontLeftMotor;
    m_rearLeftMotor = rearLeftMotor;
    m_frontRightMotor = frontRightMotor;
    m_rearRightMotor = rearRightMotor;
    SendableRegistry.addChild(this, m_frontLeftMotor);
    SendableRegistry.addChild(this, m_rearLeftMotor);
    SendableRegistry.addChild(this, m_frontRightMotor);
    SendableRegistry.addChild(this, m_rearRightMotor);
    instances++;
    SendableRegistry.addLW(this, "MecanumDrive", instances);
  }

  @Override
  public void close() {
    SendableRegistry.remove(this);
  }

  /**
   * Verifies that all motors are nonnull, throwing a NullPointerException if any of them are.
   * The exception's error message will specify all null motors, e.g. {@code
   * NullPointerException("frontLeftMotor, rearRightMotor")}, to give as much information as
   * possible to the programmer.
   *
   * @throws NullPointerException if any of the given motors are null
   */
  @SuppressWarnings({"PMD.AvoidThrowingNullPointerException", "PMD.CyclomaticComplexity"})
  private void verify(MotorController frontLeft, MotorController rearLeft,
          MotorController frontRight, MotorController rearRightMotor) {
    if (frontLeft != null && rearLeft != null && frontRight != null && rearRightMotor != null) {
      return;
    }
    StringJoiner joiner = new StringJoiner(", ");
    if (frontLeft == null) {
      joiner.add("frontLeftMotor");
    }
    if (rearLeft == null) {
      joiner.add("rearLeftMotor");
    }
    if (frontRight == null) {
      joiner.add("frontRightMotor");
    }
    if (rearRightMotor == null) {
      joiner.add("rearRightMotor");
    }
    throw new NullPointerException(joiner.toString());
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's speed is independent
   * from its angle or rotation rate.
   *
   * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Right is positive.
   * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *                  positive.
   * @param maxThrottle The robot's maximum speed of drive motors [0.0...1.0].
   */
  @SuppressWarnings("ParameterName")
  public void driveCartesian(double ySpeed, double xSpeed, double zRotation, double maxThrottle) {
    driveCartesian(ySpeed, xSpeed, zRotation, maxThrottle, 0.0);
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured clockwise from the positive X axis. The robot's speed is independent
   * from its angle or rotation rate.
   *
   * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Right is positive.
   * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is positive.
   * @param maxThrottle The robot's maximum speed of drive motors [0.0...1.0].
   * @param gyroAngle The current angle reading from the gyro in degrees around the Z axis. Use
   *                  this to implement field-oriented controls.
   */
  @SuppressWarnings("ParameterName")
  public void driveCartesian(double ySpeed, double xSpeed, double zRotation, double maxThrottle, double gyroAngle) {
    // System.out.println("drive cartesian");
    if (!m_reported) {
      HAL.report(tResourceType.kResourceType_RobotDrive,
                 tInstances.kRobotDrive2_MecanumCartesian, 4);
      m_reported = true;
    }

    ySpeed = MathUtil.clamp(ySpeed, -1.0, 1.0);
    ySpeed = MathUtil.applyDeadband(ySpeed, 0.2);
    xSpeed = MathUtil.clamp(xSpeed, -1.0, 1.0);
    xSpeed = MathUtil.applyDeadband(xSpeed, 0.2);
    zRotation = MathUtil.applyDeadband(zRotation, 0.2);

    // Compensate for gyro angle.
    Vector2d input = new Vector2d(ySpeed, xSpeed);
    input.rotate(-gyroAngle);

    double[] wheelSpeeds = new double[4];
    // if (xSpeed == 0 && ySpeed == 0) {
    //   wheelSpeeds[MotorType.kFrontLeft.value] = zRotation;
    //   wheelSpeeds[MotorType.kFrontRight.value] = -zRotation;
    //   wheelSpeeds[MotorType.kRearLeft.value] = zRotation;
    //   wheelSpeeds[MotorType.kRearRight.value] = -zRotation;
    // }
    // else {
      wheelSpeeds[MotorType.kFrontLeft.value] = input.x + input.y + zRotation;
      wheelSpeeds[MotorType.kFrontRight.value] = -input.x + input.y - zRotation;
      wheelSpeeds[MotorType.kRearLeft.value] = -input.x + input.y + zRotation;
      wheelSpeeds[MotorType.kRearRight.value] = input.x + input.y - zRotation; 
    // }
    // System.out.println("before set wheel speed");
    normalize(wheelSpeeds);
    m_frontLeftMotor.set(wheelSpeeds[MotorType.kFrontLeft.value] * maxThrottle);
    m_frontRightMotor.set(wheelSpeeds[MotorType.kFrontRight.value] * maxThrottle
        * m_rightSideInvertMultiplier);
    m_rearLeftMotor.set(wheelSpeeds[MotorType.kRearLeft.value] * maxThrottle);
    m_rearRightMotor.set(wheelSpeeds[MotorType.kRearRight.value] * maxThrottle
        * m_rightSideInvertMultiplier);

    feed();
  }

  /**
   * Drive method for Mecanum platform.
   *
   * <p>Angles are measured counter-clockwise from straight ahead. The speed at which the robot
   * drives (translation) is independent from its angle or rotation rate.
   *
   * @param magnitude The robot's speed at a given angle [-1.0..1.0]. Forward is positive.
   * @param angle     The angle around the Z axis at which the robot drives in degrees [-180..180].
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *                  positive.
   */
  @SuppressWarnings("ParameterName")
  public void drivePolar(double magnitude, double angle, double zRotation) {
    if (!m_reported) {
      HAL.report(tResourceType.kResourceType_RobotDrive, tInstances.kRobotDrive2_MecanumPolar, 4);
      m_reported = true;
    }
    driveCartesian(magnitude * Math.sin(angle * (Math.PI / 180.0)),
                   magnitude * Math.cos(angle * (Math.PI / 180.0)), zRotation, 0.0);
  }

/**
   * Swivel method for Mecanum platform. Rotates around each wheel and horizonal axis
   *
   * <p>Angles are measured counter-clockwise from straight ahead. The speed at which the robot
   * swivels (rotation) is independent from its angle or magnitude.
   *
   * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Right is positive.
   * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is positive.
   */
  @SuppressWarnings("ParameterName")
  public void pivotCartesian(double ySpeed, double xSpeed, double zRotation, double maxThrottle) {
    if (!m_reported) {
      HAL.report(tResourceType.kResourceType_RobotDrive, tInstances.kRobotDrive2_MecanumPolar, 4);
      m_reported = true;
    }

    // Initialize Wheel Power to Zero
    double[] wheelSpeeds = new double[4];
    wheelSpeeds[MotorType.kFrontLeft.value] = 0;
    wheelSpeeds[MotorType.kFrontRight.value] = 0;
    wheelSpeeds[MotorType.kRearLeft.value] = 0;
    wheelSpeeds[MotorType.kRearRight.value] = 0;
    
    // Get Rid of Minor Z Rotations
    zRotation = MathUtil.clamp(zRotation, -1.0, 1.0);
    zRotation = MathUtil.applyDeadband(zRotation, 0.2); //m_deadband

    // Determine Which Wheels Get Power
    double piOverEight = Math.PI/8;
    double magnitude = Math.sqrt(Math.pow(ySpeed,2) + Math.pow(xSpeed,2));
    double theta = -Math.atan2(ySpeed, xSpeed); // Manage Theta
    if (theta < 0) { theta += 2*Math.PI; } // 0 <= Theta <= 2pi
    theta += Math.PI/2;
    if (theta > 2*Math.PI) { theta -= 2*Math.PI; }

    if (magnitude >= 0.5) { //Magnitude must be over 0.5
      if (theta >= 15*piOverEight || theta <= piOverEight) {
        // Pivot along right axis
        wheelSpeeds[MotorType.kFrontLeft.value] = zRotation;
        wheelSpeeds[MotorType.kRearLeft.value] = zRotation;
        if (zRotation > 0)
          wheelSpeeds[MotorType.kRearRight.value] = 0.2*zRotation; // To Compensate
        else
          wheelSpeeds[MotorType.kFrontRight.value] = 0.2*zRotation; 
      }
      else if (theta > piOverEight && theta < 3*piOverEight) {
        // Pivot along front right wheel
        wheelSpeeds[MotorType.kFrontLeft.value] = zRotation;
        wheelSpeeds[MotorType.kRearLeft.value] = zRotation;
        if (zRotation > 0)
          wheelSpeeds[MotorType.kRearRight.value] = 0.5*zRotation; // To Compensate
      }
      else if (theta >= 3*piOverEight && theta <= 5*piOverEight) {
        // Pivot along front axis
        wheelSpeeds[MotorType.kRearLeft.value] = zRotation;
        wheelSpeeds[MotorType.kRearRight.value] = -zRotation;
      }
      else if (theta > 5*piOverEight && theta < 7*piOverEight) {
        // Pivot along front left wheel
        wheelSpeeds[MotorType.kFrontRight.value] = -zRotation;
        wheelSpeeds[MotorType.kRearRight.value] = -zRotation;
        if (zRotation < 0)
          wheelSpeeds[MotorType.kRearLeft.value] = -0.5*zRotation; // To Compensate
      }
      else if (theta >= 7*piOverEight && theta <= 9*piOverEight) {
        // Pivot along left axis
        wheelSpeeds[MotorType.kFrontRight.value] = -zRotation;
        wheelSpeeds[MotorType.kRearRight.value] = -zRotation;
        if (zRotation > 0)
          wheelSpeeds[MotorType.kFrontLeft.value] = -0.2*zRotation; // To Compensate
        else
          wheelSpeeds[MotorType.kRearLeft.value] = -0.2*zRotation;
      }
      else if (theta > 9*piOverEight && theta < 11*piOverEight) {
        // Pivot along left rear wheel
        wheelSpeeds[MotorType.kFrontRight.value] = -zRotation;
        wheelSpeeds[MotorType.kRearRight.value] = -zRotation;
        if (zRotation > 0)
          wheelSpeeds[MotorType.kFrontLeft.value] = -0.5*zRotation; // To Compensate
      }
      else if (theta >= 11*piOverEight && theta <= 13*piOverEight) {
        // Pivot along rear axis
        wheelSpeeds[MotorType.kFrontLeft.value] = zRotation;
        wheelSpeeds[MotorType.kFrontRight.value] = -zRotation;
      }
      else if (theta > 13*piOverEight && theta < 15*piOverEight) {
        // Pivot along right rear wheel
        wheelSpeeds[MotorType.kFrontLeft.value] = zRotation;
        wheelSpeeds[MotorType.kRearLeft.value] = zRotation;
        if (zRotation < 0)
          wheelSpeeds[MotorType.kFrontRight.value] = 0.5*zRotation; // To Compensate
      }
    }
    // Power The Motors
    normalize(wheelSpeeds);
    m_frontLeftMotor.set(wheelSpeeds[MotorType.kFrontLeft.value] * maxThrottle);
    m_frontRightMotor.set(wheelSpeeds[MotorType.kFrontRight.value] * maxThrottle
        * m_rightSideInvertMultiplier);
    m_rearLeftMotor.set(wheelSpeeds[MotorType.kRearLeft.value] * maxThrottle);
    m_rearRightMotor.set(wheelSpeeds[MotorType.kRearRight.value] * maxThrottle
        * m_rightSideInvertMultiplier);
    feed();
  }

  /**
   * Gets if the power sent to the right side of the drivetrain is multipled by -1.
   *
   * @return true if the right side is inverted
   */
  public boolean isRightSideInverted() {
    return m_rightSideInvertMultiplier == -1.0;
  }

  /**
   * Sets if the power sent to the right side of the drivetrain should be multipled by -1.
   *
   * @param rightSideInverted true if right side power should be multipled by -1
   */
  public void setRightSideInverted(boolean rightSideInverted) {
    m_rightSideInvertMultiplier = rightSideInverted ? -1.0 : 1.0;
  }

  @Override
  public void stopMotor() {
    m_frontLeftMotor.stopMotor();
    m_frontRightMotor.stopMotor();
    m_rearLeftMotor.stopMotor();
    m_rearRightMotor.stopMotor();
    feed();
  }

  @Override
  public String getDescription() {
    return "MecanumDrive";
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    // builder.setSmartDashboardType("MecanumDrive");
    // builder.setActuator(true);
    // builder.setSafeState(this::stopMotor);
    // builder.addDoubleProperty("Front Left Motor Speed",
    //     m_frontLeftMotor::get,
    //     m_frontLeftMotor::set);
    // builder.addDoubleProperty("Front Right Motor Speed",
    //     () -> m_frontRightMotor.get() * m_rightSideInvertMultiplier,
    //     value -> m_frontRightMotor.set(value * m_rightSideInvertMultiplier));
    // builder.addDoubleProperty("Rear Left Motor Speed",
    //     m_rearLeftMotor::get,
    //     m_rearLeftMotor::set);
    // builder.addDoubleProperty("Rear Right Motor Speed",
    //     () -> m_rearRightMotor.get() * m_rightSideInvertMultiplier,
    //     value -> m_rearRightMotor.set(value * m_rightSideInvertMultiplier));
  }
}