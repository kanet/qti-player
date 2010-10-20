package com.qtitools.player.client.module;

import com.qtitools.player.client.model.variables.response.Response;

public interface ResponseSocket {

	/** Get access to response */
	public Response getResponse(String id);
}
