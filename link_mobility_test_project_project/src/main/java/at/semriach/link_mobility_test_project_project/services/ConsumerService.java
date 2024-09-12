/*
    Name of the project: link_mobility_test_project_project
    Name of the package: at.semriach.link_mobility_test_project_project.services
    Name of the file: ConsumerService
    Date of the creation: 12.09.24
    Time of the creation: 12:17
    Name of the creator: manuelbodlos
*/

package at.semriach.link_mobility_test_project_project.services;

import at.semriach.link_mobility_test_project_project.pojos.Message;
import at.semriach.link_mobility_test_project_project.pojos.OutputLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService
{
    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;
    private final OutputLock outputLock;
    private final ExecutorService executorServiceA = Executors.newFixedThreadPool(1);
    private final ExecutorService executorServiceB = Executors.newFixedThreadPool(1);
    private final ExecutorService executorServiceC = Executors.newFixedThreadPool(1);
    private final List<Message> messageList = new ArrayList<>();

    public void startConsumingMessages()
    {
        executorServiceA.submit(() -> consumeMessages("recipientA"));
        executorServiceB.submit(() -> consumeMessages("recipientB"));
        executorServiceC.submit(() -> consumeMessages("recipientC"));
    }

    private void consumeMessages(String recipientPrefix)
    {
        while (true)
        {
            try
            {
                synchronized (messageList)
                {
                    Message message = null;
                    try
                    {
                        message = messageList.remove(0);
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        message = (Message) rabbitTemplate.receiveAndConvert("messageQueue");
                    }
                    if (message != null && message.getRecipient().startsWith(recipientPrefix))
                    {
                        synchronized (outputLock)
                        {
                            log.info("Consumed message in thread {}: {}", Thread.currentThread().getName(), message);
                        }
                        Thread.sleep(1000);
                    }
                    if (message != null && !message.getRecipient().startsWith(recipientPrefix))
                    {
                        messageList.add(message);
                    }
                }
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void stopConsumingMessages()
    {
        executorServiceA.shutdownNow();
        executorServiceB.shutdownNow();
        executorServiceC.shutdownNow();
    }
}
