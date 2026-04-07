import java.io.File;
import java.io.IOException;
//  TE4324234121223212124!!!!
public class Main {

    public static int fileCounter = 0;

    public static void main(String[] args) {
        // Загружаем сохраненный счетчик
        fileCounter = loadCounter();

        System.out.println("Программа начала работу");
        System.out.println("Это запуск № " + (fileCounter + 1));
        // Провекряем, передан ли аргумент (путь к файлу)
        if (args.length == 0) {
            System.out.println("ОШИБКА: не указан путь к аудиофайлу");
            System.out.println("Пример использования: java SpeechRecognize test");
            System.out.println("Проект завершил работу с ошибкой");
            return;
        }
        String filePath = args[0];
        System.out.println("Обрабатываю файл: " + filePath);

        // Проверяем существующий файл
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("ОШИБКА: файл не найден");
            return;
        }

        System.out.println("Файл существует! Весит: " + file.length() + " байт");

        // Создаем объект для работы с аудио
        AudioFileLoader audioLoader = new AudioFileLoader(filePath);

        try {
            audioLoader.load();
            audioLoader.showFirstBytes(16);
            audioLoader.countByte((byte) 0x00);

            System.out.println("\nИнформация о файле");
            System.out.println("Имя: " + audioLoader.getFileName());

            System.out.println("\nПроект успешно обработал файл!");

        } catch (IOException e) {
            System.out.println("ОШИБКА при загрузке файла: " + e.getMessage());
        }
        System.out.println("Работа программы завершена!");
        fileCounter = fileCounter + 1;
        saveCounter(fileCounter);
        System.out.println("Обработано файлов за все время: " + fileCounter);
        System.out.println("Спасибо за использование! Хорошего дня!");
    }

    // Сохраняем счетчик в файл
    private static void saveCounter(int counter) {
        try {
            java.nio.file.Files.writeString(
                    java.nio.file.Paths.get("counter.txt"),
                    String.valueOf(counter)
            );
        } catch (IOException e) {
            System.out.println("Не удалось сохранить счетчик");
        }
    }

    // Загружаем счетчик из файла
    private static int loadCounter() {
        try {
            String text = java.nio.file.Files.readString(
                java.nio.file.Paths.get("counter.txt")
            );
            return Integer.parseInt(text);
        } catch (IOException e) {
            return 0; // если нет файла начинаем с 0
        }
    }

}
