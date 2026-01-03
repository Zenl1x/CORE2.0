package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Подсистема управления движением робота (Mecanum Drive)
 */
public class DriveSubsystem {
    
    private final DcMotor frontLeft;
    private final DcMotor frontRight;
    private final DcMotor backLeft;
    private final DcMotor backRight;
    
    private boolean isHighSpeed = true;
    private double speedMultiplier = RobotConfig.HIGH_SPEED;
    
    public DriveSubsystem(HardwareMap hardwareMap) {
        // Инициализация моторов
        frontLeft = hardwareMap.get(DcMotor.class, RobotConfig.FRONT_LEFT_MOTOR);
        frontRight = hardwareMap.get(DcMotor.class, RobotConfig.FRONT_RIGHT_MOTOR);
        backLeft = hardwareMap.get(DcMotor.class, RobotConfig.BACK_LEFT_MOTOR);
        backRight = hardwareMap.get(DcMotor.class, RobotConfig.BACK_RIGHT_MOTOR);
        
        // Настройка направлений
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    
    /**
     * Управление движением робота через джойстики
     * @param drive - движение вперёд/назад (обычно left_stick_x)
     * @param strafe - движение влево/вправо (обычно left_stick_y)
     * @param rotate - поворот (обычно right_stick_x)
     */
    public void drive(double drive, double strafe, double rotate) {
        // Расчёт мощности для каждого мотора (Mecanum Drive)
        double frontLeftPower = drive + strafe + rotate;
        double frontRightPower = drive - strafe - rotate;
        double backLeftPower = drive - strafe + rotate;
        double backRightPower = drive + strafe - rotate;
        
        // Нормализация (если какая-то мощность > 1.0, масштабируем все пропорционально)
        double maxPower = Math.max(
            Math.abs(frontLeftPower),
            Math.max(Math.abs(frontRightPower),
            Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)))
        );
        
        if (maxPower > 1.0) {
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower /= maxPower;
            backRightPower /= maxPower;
        }
        
        // Применение скорости и установка мощности
        frontLeft.setPower(frontLeftPower * speedMultiplier);
        frontRight.setPower(frontRightPower * speedMultiplier);
        backLeft.setPower(backLeftPower * speedMultiplier);
        backRight.setPower(backRightPower * speedMultiplier);
    }
    
    /**
     * Переключение между высокой и низкой скоростью
     */
    public void toggleSpeed() {
        isHighSpeed = !isHighSpeed;
        speedMultiplier = isHighSpeed ? RobotConfig.HIGH_SPEED : RobotConfig.LOW_SPEED;
    }
    
    /**
     * Остановка всех моторов
     */
    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
    
    // Геттеры для телеметрии
    public boolean isHighSpeed() {
        return isHighSpeed;
    }
    
    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
}
