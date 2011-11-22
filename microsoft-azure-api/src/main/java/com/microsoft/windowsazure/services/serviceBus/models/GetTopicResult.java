package com.microsoft.windowsazure.services.serviceBus.models;

/**
 * Represents the result of a <code>getTopic</code> operation.
 */
public class GetTopicResult {

	private TopicInfo value;

	/**
	 * Creates an instance of the <code>GetTopicResult</code> class.
	 * 
	 * @param value
	 *            A {@link TopicInfo} object assigned as the value of the result.
	 */
	public GetTopicResult(TopicInfo value) {
		this.setValue(value);
	}

	/**
	 * Specfies the value of the result.
	 * 
	 * @param value
	 *            A {@link TopicInfo} object assigned as the value of the result.
	 */
	public void setValue(TopicInfo value) {
		this.value = value;
	}

	/**
	 * Returns the value of the result.
	 * 
	 * @return A {@link TopicInfo} object that represents the value of the result.
	 */
	public TopicInfo getValue() {
		return value;
	}

}
