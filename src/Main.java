import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

// Импортируем классы Vosk
import org.vosk.Model;
import org.vosk.Recognizer;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("=== РАСПОЗНАВАНИЕ РЕЧИ (Vosk) ===\n");

        System.out.println("=== Укажите путь к аудиофайлу ===");
        System.out.println("ПРИМЕР Linux: /home/io/Рабочий стол/Франциск-Асизский.wav");
        System.out.println("ПРИМЕР Windows: C:\\Users\\Admin\\Desktop\\Франциск-Асизский.wav");
        System.out.println("ВАЖНО: Формат: WAV, 16 кГц, моно, 16 бит");

        // Создаем сканер
        Scanner sc = new Scanner(System.in);
        String wavFilePath = sc.nextLine();
        File audioFile = new File(wavFilePath);

        if (!audioFile.exists()) {
            System.out.println("ОШИБКА: файл не найден - " + wavFilePath);
            return;
        }
        System.out.println("\n=== Укажите файл вывода данных в формате txt ===");
        System.out.println("ПРИМЕР Linux: /home/io/Рабочий стол/stenogramma.txt");
        System.out.println("ПРИМЕР Windows: C:\\Users\\Admin\\Desktop\\stenogramma.txt");
        System.out.println("ВАЖНО: Если файл уже создан он будет перезаписан!");
        String textFilePath = sc.nextLine();

        File file = new File(textFilePath);
        // Извлекаем информацию о папке (C:/NewFolder/SubFolder)
        File dir = file.getParentFile();

        // Проверяем существует ли такой путь
        if (dir != null && !dir.exists()) {
            System.out.println("ОШИБКА: путь к текстовому файлу указан неверно");
            return;
        }


        System.out.println("Аудиофайл: " + audioFile.getName());
        System.out.println("Размер: " + audioFile.length() + " байт");

        // Определяем путь к модели в зависимости от ОС
        String modelPath;
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows: модель в той же папке, что и EXE
            modelPath = "vosk-model-ru-0.42";
        } else {
            // Linux: системный путь
            modelPath = "/usr/local/share/speechrecognizer/vosk-model-ru-0.42";
        }

        System.out.println("ОС: " + os);
        System.out.println("Путь к модели: " + modelPath);
        // StringBuilder для сбора всей стенограммы
        StringBuilder fullTranscript = new StringBuilder();

        try {
            // Загружаем модель
            System.out.println("\nЗагрузка модели распознавания...");
            Model model = new Model(modelPath);
            System.out.println("Модель загружена!");

            // Создаем распознаватель
            Recognizer recognizer = new Recognizer(model, 16000);
            System.out.println("Распознаватель готов!");

            // Открываем аудиофайл
            FileInputStream audioStream = new FileInputStream(audioFile);

            // Буфер для чтения аудио
            byte[] buffer = new byte[4096];
            int bytesRead;  // ← исправлено byteRead → bytesRead

            System.out.println("Распознавание речи...\n");

            // Читаем аудио и распознаем
            while ((bytesRead = audioStream.read(buffer)) >= 0) {
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    String result = recognizer.getResult();
                    String text = extractText(result);
                    if (!text.isEmpty()) {
                        fullTranscript.append(text).append(" ");
                    }
                }
            }

            String finalResult = recognizer.getFinalResult();
            String finalText = extractText(finalResult);

            fullTranscript.append("\n=== ИТОГОВАЯ СТЕНОГРАММА ===\n");
            fullTranscript.append(finalText);

            System.out.println("\n=== ИТОГОВАЯ СТЕНОГРАММА ===");
            System.out.println(finalText);
            System.out.println("============================");

            // Сохраняем полную стенограмму
            Files.writeString(Paths.get(textFilePath), fullTranscript.toString());
            System.out.println("\nПолная стенограмма сохранена в: " + textFilePath);

            audioStream.close();
            recognizer.close();
            model.close();

            System.out.println("\nРаспознавание завершено!");

        } catch (IOException e) {
            System.out.println("ОШИБКА: " + e.getMessage());  // ← исправлено ystem → System
            e.printStackTrace();
        }
    }

    // Извлекает текст из JSON-ответа Vosk
    private static String extractText(String json) {
        int start = json.indexOf("\"text\" : \"");
        if (start == -1) return "";

        start += 10;
        int end = json.indexOf("\"", start);

        if (end == -1) return "";
        return json.substring(start, end);
    }
}