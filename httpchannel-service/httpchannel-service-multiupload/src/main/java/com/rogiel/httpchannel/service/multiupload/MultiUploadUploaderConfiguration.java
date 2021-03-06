/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.rogiel.httpchannel.service.multiupload;

import java.util.EnumSet;

import com.rogiel.httpchannel.service.AbstractUploaderConfiguration;
import com.rogiel.httpchannel.service.Uploader.DescriptionableUploaderConfiguration;
import com.rogiel.httpchannel.service.Uploader.UploaderConfiguration;
import com.rogiel.httpchannel.service.multiupload.MultiUploadService.UploaderImpl;

/**
 * Describes an configuration for an {@link UploaderImpl}
 * 
 * @author <a href="http://www.rogiel.com">Rogiel</a>
 */
public class MultiUploadUploaderConfiguration extends
		AbstractUploaderConfiguration implements UploaderConfiguration,
		DescriptionableUploaderConfiguration {
	/**
	 * The upload description
	 */
	private String description = DescriptionableUploaderConfiguration.DEFAULT_DESCRIPTION;
	/**
	 * The services in which Multiupload should mirror the uploaded file
	 */
	private EnumSet<MultiUploadMirrorService> uploadServices = EnumSet
			.allOf(MultiUploadMirrorService.class);
	/**
	 * The service authentication
	 */
	private final String[][] serviceAuthentication = new String[MultiUploadMirrorService
			.values().length][];

	/**
	 * An enumeration containing all supported services for Multiupload
	 * 
	 * @author <a href="http://www.rogiel.com">Rogiel</a>
	 */
	public enum MultiUploadMirrorService {
		MEGAUPLOAD(1), UPLOADKING(16), DEPOSIT_FILES(7), HOTFILE(9), TWO_SHARED(
				11), UPLOAD_HERE(17), ZSHARE(6), FILE_SONIC(15), FILE_SERVE(18), PUT_LOCKER(
				19), ORON(20), FILE_FACTORY(21), FREAK_SHARED(23);

		/**
		 * The internal multiupload id
		 */
		public final int id;

		private MultiUploadMirrorService(int id) {
			this.id = id;
		}
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public MultiUploadUploaderConfiguration description(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Adds this service as an desired mirror
	 * 
	 * @param service
	 *            the service
	 */
	public MultiUploadUploaderConfiguration uploadService(
			MultiUploadMirrorService... services) {
		for (final MultiUploadMirrorService service : services) {
			serviceAuthentication[service.ordinal()] = null;
			uploadServices.add(service);
		}
		return this;
	}

	/**
	 * Adds this service as an desired mirror
	 * 
	 * @param service
	 *            the service
	 * @param username
	 *            the service username
	 * @param password
	 *            the service password
	 */
	public MultiUploadUploaderConfiguration uploadService(
			MultiUploadMirrorService service, String username, String password) {
		if (username != null && password != null) {
			serviceAuthentication[service.ordinal()] = new String[] { username,
					password };
		}
		return uploadService(service);
	}

	/**
	 * Checks if the service is on the desired mirror list
	 * 
	 * @param service
	 *            the service
	 * @return <code>true</code> if the service is on the list
	 */
	public boolean containsUploadService(MultiUploadMirrorService service) {
		return uploadServices.contains(service);
	}

	/**
	 * Removes this service from the mirror list
	 * 
	 * @param service
	 *            the service
	 */
	public MultiUploadUploaderConfiguration removeUploadService(
			MultiUploadMirrorService service) {
		uploadServices.remove(service);
		return this;
	}

	/**
	 * Removes all services from the mirror list
	 * 
	 * @return
	 */
	public MultiUploadUploaderConfiguration clearUploadServices() {
		uploadServices.clear();
		return this;
	}

	/**
	 * @return the list of services of which MultiUpload should try to make
	 *         mirrors
	 */
	public EnumSet<MultiUploadMirrorService> uploadServices() {
		return uploadServices;
	}

	/**
	 * @return the configured authentication for the given service
	 */
	protected String[] getAuthenticationForService(
			MultiUploadMirrorService service) {
		return serviceAuthentication[service.ordinal()];
	}
}
