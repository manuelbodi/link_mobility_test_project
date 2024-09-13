/*
    Name of the project: link_mobility_test_project_project
    Name of the package: at.semriach.link_mobility_test_project_project.controller
    Name of the file: ProducerController
    Date of the creation: 13.09.24
    Time of the creation: 12:36
    Name of the creator: manuelbodlos
*/

package at.semriach.link_mobility_test_project_project.controller;

import at.semriach.link_mobility_test_project_project.pojos.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/producerController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProducerController
{
    private final RabbitTemplate rabbitTemplate;

    @RequestMapping(method = RequestMethod.POST, path = "/produceMessage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendMessage(@RequestBody(required = true) Message message)
    {
        message.setId(UUID.randomUUID());
        message.setSender("producerController");
        rabbitTemplate.convertAndSend("messageQueue", message);
        log.info("Produced message: {}", message);
        return ResponseEntity.ok().build();
    }
}
