import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Распознователь речи запущен");

        // Провекряем, передан ли аргумент (путь к файлу)
        if (args.length == 0) {
            System.out.println("ОШИБКА: не указан путь к аудиофайлу");
            System.out.println("Пример использования: java SpeechRecognize test");
            System.out.println("Проект завершил работу с ошибкой");
            return;
        }
        String filePath = args[0];
        System.out.println("Путь к файлу: " + filePath);

        // Проверяем существующий файл
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("ОШИБКА: файл не найден");
            return;
        }

        System.out.println("Файл найден! Размер: " + file.length() + "байт");

        // Создаем объект для работы с аудио
        AudioFileLoader audioLoader = new AudioFileLoader(filePath);

        try {
            audioLoader.load();
            audioLoader.showFirstBytes(16);

            System.out.println("\n Информация о фале");
            System.out.println(" Имя: " + audioLoader.getFileName() + " бфйт");

            System.out.println("\n Проект успешно обработал файл!");

        } catch (IOException e) {
            System.out.println("ОШИБКА при запуске файла: " + e.getMessage());
        }
        System.out.println("Работа программы завершена!");
    }

}
