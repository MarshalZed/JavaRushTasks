package task3008.client;

import task3008.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BotClient extends Client {

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }

    @Override
    protected String getUserName() {
        return "date_bot_" + (int) (Math.random() * 100);
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    public class BotSocketThread extends Client.SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);

            if (message.contains(": ")){
                HashMap<String, String> botTimeRequestWords = new HashMap<>();
                botTimeRequestWords.put("дата", "d.MM.YYYY");
                botTimeRequestWords.put("день", "d");
                botTimeRequestWords.put("месяц", "MMMM");
                botTimeRequestWords.put("год", "YYYY");
                botTimeRequestWords.put("время", "H:mm:ss");
                botTimeRequestWords.put("час", "H");
                botTimeRequestWords.put("минуты", "m");
                botTimeRequestWords.put("секунды", "s");

                if (botTimeRequestWords.containsKey(message.split(": ")[1])) {
                    StringBuilder answer = new StringBuilder("Информация для ").append(message.split(": ")[0]).append(": ");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(botTimeRequestWords.get(message.split(": ")[1]));
                    Date date = Calendar.getInstance().getTime();
                    answer.append(simpleDateFormat.format(date));
                    sendTextMessage(answer.toString());
                }
            }

        }
    }
}