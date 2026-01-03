package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
/**
 * Главный TeleOp режим для FTC робота
 * 
 * УПРАВЛЕНИЕ GAMEPAD 1 (ВОДИТЕЛЬ):
 * - Left Stick: Движение (X - вперёд/назад, Y - влево/вправо)
 * - Right Stick X: Поворот
 * - Touchpad: Переключение высокой/низкой скорости
 * - Triangle: Поворот клешни в положение по умолчанию
 * - Square: Поворот клешни на 90°
 * - A: Закрыть захват клешни
 * - B: Открыть захват клешни
 * 
 * УПРАВЛЕНИЕ GAMEPAD 2 (ОПЕРАТОР):
 * - Right Bumper: Выдвинуть телескоп
 * - Left Bumper: Втянуть телескоп
 * - Right Trigger: Выдвинуть клешню
 * - Left Trigger: Втянуть клешню
 * - DPad Up: Поднять клешню
 * - DPad Down: Опустить клешню
 * - DPad Right: Клешня в среднее положение
 * - DPad Left: Клешня в нижнее положение
 * - A: Поворот клешни на 45°
 * - B: Поворот клешни на 180°
 * - Triangle: Открыть коробку
 * - Square: Закрыть коробку
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Genius Olympiad Robot win №1 100% Goyda")
public class TeleOp extends LinearOpMode {
    
    // Подсистемы робота
    private DriveSubsystem drive;
    private ManipulatorSubsystem manipulator;
    private ClawSubsystem claw;
    private BoxSubsystem box;
    private ColorDetector colorDetector;
    
    // Управление состоянием игры
    private boolean isEndGame = false;
    
    // Debounce для кнопок
    private long lastSpeedToggleTime = 0;
    
    @Override
    public void runOpMode() {
        // Инициализация всех подсистем
        initializeSubsystems();
        
        telemetry.addLine("✓ Робот инициализирован");
        telemetry.addLine("Нажмите START для начала");
        telemetry.update();
        
        waitForStart();
        
        // Главный игровой цикл
        while (opModeIsActive()) {
            // Проверка времени для предупреждения об окончании игры
            checkEndgame();
            
            // Обработка управления от геймпадов
            handleGamepad1();
            handleGamepad2();
            
            // Автоматические действия
            handleAutoActions();
            
            // Обновление телеметрии
            updateTelemetry();
        }
    }
    
    /**
     * Инициализация всех подсистем робота
     */
    private void initializeSubsystems() {
        drive = new DriveSubsystem(hardwareMap);
        manipulator = new ManipulatorSubsystem(hardwareMap);
        claw = new ClawSubsystem(hardwareMap);
        box = new BoxSubsystem(hardwareMap);
        colorDetector = new ColorDetector(hardwareMap);
    }
    
    /**
     * Обработка управления от первого геймпада (водитель)
     */
    private void handleGamepad1() {
        // Движение робота
        double driveY = -gamepad1.left_stick_x;  // вперёд/назад
        double strafeX = gamepad1.left_stick_y;  // влево/вправо
        double rotate = gamepad1.right_stick_x;  // поворот
        drive.drive(driveY, strafeX, rotate);
        
        // Переключение скорости с debounce
        if (gamepad1.touchpad && System.currentTimeMillis() - lastSpeedToggleTime > RobotConfig.DEBOUNCE_DELAY_MS) {
            drive.toggleSpeed();
            lastSpeedToggleTime = System.currentTimeMillis();
            gamepad1.rumble(100); // Короткая вибрация для подтверждения
        }
        
        // Управление поворотом клешни
        if (gamepad1.triangle) {
            claw.rotateDefault();
        }
        if (gamepad1.square) {
            claw.rotate90();
        }
        
        // Управление захватом клешни
        if (gamepad1.a) {
            claw.closeGrip();
        }
        if (gamepad1.b) {
            claw.openGrip();
        }
    }
    
    /**
     * Обработка управления от второго геймпада (оператор)
     */
    private void handleGamepad2() {
        // Телескопический манипулятор
        if (gamepad2.right_bumper) {
            manipulator.extend();
        }
        if (gamepad2.left_bumper) {
            manipulator.retract();
        }
        
        // Выдвижение/втягивание клешни через триггеры
        float extendPower = gamepad2.right_trigger;
        float retractPower = gamepad2.left_trigger;
        
        if (extendPower > 0.1) {
            claw.extend(extendPower);
        } else if (retractPower > 0.1) {
            claw.retract(retractPower);
        } else {
            claw.stopExtending();
        }
        
        // Подъём клешни
        if (gamepad2.dpad_up) {
            claw.liftUp();
        }
        if (gamepad2.dpad_down) {
            claw.liftDown();
        }
        if (gamepad2.dpad_right) {
            claw.liftMiddle();
        }
        if (gamepad2.dpad_left) {
            claw.liftServoDown();
        }
        
        // Поворот клешни
        if (gamepad2.a) {
            claw.rotate45();
        }
        if (gamepad2.b) {
            claw.rotate180();
        }
        
        // Управление коробкой
        if (gamepad2.triangle) {
            box.open();
        }
        if (gamepad2.square) {
            box.close();
        }
    }
    
    /**
     * Автоматические действия робота
     */
    private void handleAutoActions() {
        // Автоматическое открытие захвата при достижении определённой высоты
        if (claw.shouldAutoGrip()) {
            sleep(RobotConfig.AUTO_GRIP_DELAY_MS);
            claw.openGrip();
        }
    }
    
    /**
     * Проверка времени для предупреждения об окончании игры
     */
    private void checkEndgame() {
        if (!isEndGame && getRuntime() >= RobotConfig.ENDGAME_WARNING_TIME) {
            gamepad1.rumbleBlips(3);
            gamepad2.rumbleBlips(3);
            isEndGame = true;
        }
    }
    
    /**
     * Обновление телеметрии на драйвер станцию
     */
    private void updateTelemetry() {
        // Драйв система
        telemetry.addLine("=== ДВИЖЕНИЕ ===");
        telemetry.addData("Режим скорости", drive.isHighSpeed() ? "ВЫСОКАЯ" : "НИЗКАЯ");
        telemetry.addData("Множитель", "%.2f", drive.getSpeedMultiplier());
        
        // Цветовой сенсор
        telemetry.addLine();
        telemetry.addLine("=== ЦВЕТОВОЙ СЕНСОР ===");
        telemetry.addData("Обнаружен цвет", colorDetector.isColorDetected() ? "ДА" : "НЕТ");
        telemetry.addData("Доминирующий", colorDetector.getDominantColor());
        telemetry.addData("RGB", "R:%d G:%d B:%d", 
            colorDetector.getRed(), 
            colorDetector.getGreen(), 
            colorDetector.getBlue());
        
        // Манипулятор
        telemetry.addLine();
        telemetry.addLine("=== МАНИПУЛЯТОР ===");
        telemetry.addData("Статус", manipulator.isExtended() ? "ВЫДВИНУТ" : "ВТЯНУТ");
        telemetry.addData("Позиция Л/П", "%d / %d", 
            manipulator.getLeftPosition(), 
            manipulator.getRightPosition());
        
        // Клешня
        telemetry.addLine();
        telemetry.addLine("=== КЛЕШНЯ ===");
        telemetry.addData("Подъём", claw.isLifted() ? "ПОДНЯТА" : "ОПУЩЕНА");
        telemetry.addData("Позиция подъёма", claw.getLiftingPosition());
        telemetry.addData("Захват", "%.2f", claw.getGripPosition());
        telemetry.addData("Лифт", "%.2f", claw.getLiftPosition());
        telemetry.addData("Поворот", "%.2f", claw.getRotatePosition());
        
        // Коробка
        telemetry.addLine();
        telemetry.addLine("=== КОРОБКА ===");
        telemetry.addData("Статус", box.isOpen() ? "ОТКРЫТА" : "ЗАКРЫТА");
        
        // Игровой статус
        telemetry.addLine();
        telemetry.addLine("=== ИГРА ===");
        telemetry.addData("Время", "%.1f сек", getRuntime());
        if (isEndGame) {
            telemetry.addLine("⚠ ЭНДГЕЙМ!");
        }
        
        telemetry.update();
    }
}
