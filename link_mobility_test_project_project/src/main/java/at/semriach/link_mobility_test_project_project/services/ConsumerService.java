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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumerService
{
    private final RabbitTemplate rabbitTemplate;
    private final OutputLock outputLock;
    private final ExecutorService executorServiceA = Executors.newFixedThreadPool(1);
    private final ExecutorService executorServiceB = Executors.newFixedThreadPool(1);
    private final ExecutorService executorServiceC = Executors.newFixedThreadPool(1);
    private static final List<Message> consumableMessageList = new ArrayList<>();
    public static final List<Message> consumedMessageList = new ArrayList<>();
    public static Long consumerCounter = 0L;

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
                synchronized (consumableMessageList)
                {
                    Message message = null;
                    try
                    {
                        message = consumableMessageList.remove(0);
                    } catch (IndexOutOfBoundsException e)
                    {
                        message = (Message) rabbitTemplate.receiveAndConvert("messageQueue");
                    }
                    if (message != null && message.getRecipient().startsWith(recipientPrefix))
                    {
                        consumerCounter++;
                        consumedMessageList.add(message);
                        synchronized (outputLock)
                        {
                            log.info("Consumed message in thread {}: {}", Thread.currentThread().getName(), message);
                        }
                        Thread.sleep(1000);
                    }
                    if (message != null && !message.getRecipient().startsWith(recipientPrefix))
                    {
                        consumableMessageList.add(message);
                    }
                }
            } catch (InterruptedException e)
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
