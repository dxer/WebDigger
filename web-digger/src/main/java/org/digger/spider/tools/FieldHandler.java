package org.digger.spider.tools;

import org.digger.spider.entity.OutputModel;
import org.digger.spider.entity.Response;

public class FieldHandler {

	public void handler(Response response, OutputModel model) {
		if (response != null && model != null) {
			
			
			response.setOutputModel(model);
			
		}

	}
}
