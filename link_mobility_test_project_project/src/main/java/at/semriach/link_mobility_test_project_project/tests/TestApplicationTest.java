/*
    Name of the project: link_mobility_test_project_project
    Name of the package: at.semriach.link_mobility_test_project_project.tests
    Name of the file: TestApplicationTest
    Date of the creation: 13.09.24
    Time of the creation: 10:30
    Name of the creator: manuelbodlos
*/

package at.semriach.link_mobility_test_project_project.tests;

import at.semriach.link_mobility_test_project_project.services.ConsumerService;
import at.semriach.link_mobility_test_project_project.services.ProducerService;
import at.semriach.link_mobility_test_project_project.services.StartApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestApplicationTest
{
    @MockBean
    private final StartApplicationService startApplicationService;
    private final ProducerService producerService;
    private final ConsumerService consumerService;
    private final RabbitAdmin rabbitAdmin;
    private final Queue messageQueue;
    private final Long bound = 10L;

    @Test
    public void testApplication() throws InterruptedException
    {
        doNothing().when(startApplicationService).init();
        rabbitAdmin.deleteQueue(messageQueue.getName());
        rabbitAdmin.declareQueue(messageQueue);
        producerService.startProducingMessages(bound);
        consumerService.startConsumingMessages();
        while (ProducerService.producerCounter < bound)
        {
            Thread.sleep(1000);
        }
        while (rabbitAdmin.getQueueProperties(messageQueue.getName()).get("QUEUE_MESSAGE_COUNT") != null && (Integer) rabbitAdmin.getQueueProperties(messageQueue.getName()).get("QUEUE_MESSAGE_COUNT") > 0)
        {
            Thread.sleep(1000);
        }
        Assertions.assertEquals(ProducerService.producerCounter, ConsumerService.consumerCounter);
    }
}
