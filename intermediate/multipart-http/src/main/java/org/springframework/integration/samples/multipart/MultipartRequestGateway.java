/**
 * 
 */
package org.springframework.integration.samples.multipart;

import java.util.Map;

import org.springframework.http.HttpStatus;

/**
 * @author ozhurakousky
 *
 */
public interface MultipartRequestGateway {

	public HttpStatus postMultipartRequest(Map<String, Object> multipartRequest);
}
