# link_mobility_test_project
Test project for link mobility following the first discussion about my application.

To start the RabbitMQ server please run "docker-compose up -d" in the directory /src/main/java/at/semriach/link_mobility_test_project_project/docker
In the directory /src/main/java/at/semriach/link_mobility_test_project_project/docker you can also find a docker volume that stores files from the RabbitMQ server so that the status of the server stays at its level if the container would be deleted

To start the test please run the class "TestApplicationTest.java" in the directory /src/main/java/at/semriach/link_mobility_test_project_project/tests

To produce messages through the endpoint please use the post endpoint with the url "localhost:8080/producerController/produceMessage" and add a body like "{
    "id":null,
    "sender":null,
    "recipient":"recipientA",
    "message":"Hello World!"
}"
Please use "recipientA", "recipientB" or "recipientC" as recipient because you wanted my consumers to consume only messages with a special character and as i understand no further (generell) consumer

To let my application produce messages continiously please remove the "// " before the statement "" in the class "StartApplicationService.java" in the directory "/src/main/java/at/semriach/link_mobility_test_project_project/services/"
