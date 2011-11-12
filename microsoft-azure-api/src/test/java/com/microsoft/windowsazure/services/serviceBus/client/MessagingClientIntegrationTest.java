package com.microsoft.windowsazure.services.serviceBus.client;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.microsoft.windowsazure.services.serviceBus.IntegrationTestBase;
import com.microsoft.windowsazure.services.serviceBus.Message;
import com.microsoft.windowsazure.services.serviceBus.client.MessageReceiver;
import com.microsoft.windowsazure.services.serviceBus.client.MessageSender;
import com.microsoft.windowsazure.services.serviceBus.client.MessageTransceiver;
import com.microsoft.windowsazure.services.serviceBus.client.MessagingClient;

public class MessagingClientIntegrationTest extends IntegrationTestBase {

    private MessagingClient messaging;

    @Before
    public void createClient() throws Exception {
        messaging = createConfiguration().create(MessagingClient.class);
    }

    @Test
    public void queueWillSendAndReceiveMessages() throws Exception {
        // Arrange
        MessageTransceiver port = messaging.openQueue("TestAlpha");
        Message messageOut = new Message("queueWillSendAndReceiveMessages");

        // Act
        port.sendMessage(messageOut);
        Message messageIn = port.receiveMessage();

        // Assert
        assertNotSame(messageOut, messageIn);

        byte[] data = new byte[100];
        int size = messageIn.getBody().read(data);
        assertEquals(31, size);
        assertEquals("queueWillSendAndReceiveMessages", new String(data, 0, size));
    }

    @Test
    public void queueIsSenderAndReceiver() throws Exception {
        // Arrange
        MessageSender sender = messaging.openQueue("TestAlpha");
        MessageReceiver receiver = messaging.openQueue("TestAlpha");
        Message messageOut = new Message("queueIsSenderAndReceiver");

        // Act
        sender.sendMessage(messageOut);
        Message messageIn = receiver.receiveMessage();

        // Assert
        assertNotSame(messageOut, messageIn);

        byte[] data = new byte[100];
        int size = messageIn.getBody().read(data);
        assertEquals(24, size);
        assertEquals("queueIsSenderAndReceiver", new String(data, 0, size));
    }

    @Test
    public void clientMayBeCreatedDirectly() throws Exception {
        // Arrange

        // Act
        MessagingClient mc = new MessagingClient(createConfiguration());
        mc.openQueue("TestAlpha").sendMessage(new Message("clientMayBeCreatedDirectly"));

        // Assert
    }

}