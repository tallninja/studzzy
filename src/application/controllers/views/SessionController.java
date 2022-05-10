package application.controllers.views;

import java.io.*;
import java.util.UUID;

public class SessionController {

    public static UUID userId;
    private static String sessionFile = "session.txt";

    private static FileWriter fileWriter = null;
    private static FileReader fileReader = null;
    private static BufferedReader bufferedReader = null;
    private static BufferedWriter bufferedWriter = null;


    public static void setSession(UUID userId) {
        setUserId(userId);

        try {
            fileWriter = new FileWriter(sessionFile);
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(getUserId().toString());
        } catch (Exception e) {
            System.out.printf("%s: %s", e.getClass().getName(), e.getMessage());
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                    fileWriter.close();
                }
            } catch (Exception e) {
                System.out.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }

    }

    public static UUID getSession() {

        String line;

        try {
            fileReader = new FileReader(sessionFile);
            bufferedReader = new BufferedReader(fileReader);

            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            setUserId(UUID.fromString(stringBuilder.toString()));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s: %s", e.getClass().getName(), e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                    fileReader.close();
                }
            } catch (Exception e) {
                System.out.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }

        return  getUserId();
    }

    public static void deleteSession() {
        setUserId(userId);

        try {
            fileWriter = new FileWriter(sessionFile);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("");
        } catch (Exception e) {
            System.out.printf("%s: %s", e.getClass().getName(), e.getMessage());
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                    fileWriter.close();
                }
            } catch (Exception e) {
                System.out.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }
    }

    public static UUID getUserId() {
        return userId;
    }

    public static void setUserId(UUID userId) {
        SessionController.userId = userId;
    }
}
