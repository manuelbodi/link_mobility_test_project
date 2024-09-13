/*
    Name of the project: link_mobility_test_project_project
    Name of the package: at.semriach.link_mobility_test_project_project.services
    Name of the file: ProducerService
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

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProducerService
{
    private final RabbitTemplate rabbitTemplate;
    private final OutputLock outputLock;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    public static Long producerCounter = 0L;

    public void startProducingMessages(Long bound)
    {
        for (int i = 0; i < 5; i++)
        {
            executorService.submit(() -> produceMessages(bound));
        }
    }

    private void produceMessages(Long bound)
    {
        while (true)
        {
            try
            {
                if (!bound.equals(0L))
                {
                    synchronized (producerCounter)
                    {
                        if (producerCounter.equals(bound))
                        {
                            return;
                        }
                        producerCounter++;
                    }
                }
                Message message = new Message(UUID.randomUUID(),
                        "sender" + Thread.currentThread().getId(),
                        "recipient" + (char) ('A' + (int) (Math.random() * 3)),
                        "Hello World!");
                rabbitTemplate.convertAndSend("messageQueue", message);
                synchronized (outputLock)
                {
                    log.info("Produced message: {}", message);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void stopProducingMessages()
    {
        executorService.shutdownNow();
    }
}
