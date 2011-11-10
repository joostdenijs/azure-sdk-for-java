package com.microsoft.azure.services.serviceBus;

import com.microsoft.azure.ServiceException;

public class Util {
	public static Iterable<Queue> iterateQueues(ServiceBusService service)
			throws ServiceException {
		//TODO: iterate over link rel=next pagination
		return service.getQueueList().getQueues();
	}
}