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
package com.captchatrader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.omg.CORBA.Any;

import com.captchatrader.exception.ApplicationKeyDisabledException;
import com.captchatrader.exception.CaptchaTraderException;
import com.captchatrader.exception.ConnectionLimitException;
import com.captchatrader.exception.DailyLimitException;
import com.captchatrader.exception.ImageTooLargeException;
import com.captchatrader.exception.IncorrectRespondsException;
import com.captchatrader.exception.InsuficientCreditsException;
import com.captchatrader.exception.InternalErrorException;
import com.captchatrader.exception.InvalidApplicationKeyException;
import com.captchatrader.exception.InvalidParametersException;
import com.captchatrader.exception.InvalidTicketException;
import com.captchatrader.exception.InvalidURLException;
import com.captchatrader.exception.InvalidUserException;
import com.captchatrader.exception.NotAnImageException;
import com.captchatrader.exception.SubmissionErrorException;
import com.captchatrader.exception.UserNotValidatedException;

/**
 * @author <a href="http://www.rogiel.com">Rogiel</a>
 * 
 */
public class CaptchaTrader {
	/**
	 * The {@link HttpClient} instance
	 */
	private final HttpClient client = new DefaultHttpClient();
	private final JSONParser json = new JSONParser();

	private final URI apiURI;
	private final String applicationKey;
	private final String username;
	private final String password;

	/**
	 * Creates a new API instance with the given application key
	 * 
	 * @param applicationKey
	 *            the key
	 */
	public CaptchaTrader(URI apiURI, String applicationKey, String username,
			String password) {
		this.apiURI = apiURI;
		this.applicationKey = applicationKey;
		this.username = username;
		this.password = password;
	}

	/**
	 * Creates a new API instance with the given application key
	 * 
	 * @param applicationKey
	 *            the key
	 * @throws MalformedURIException
	 */
	public CaptchaTrader(String applicationKey, String username, String password) {
		this(URI.create("http://api.captchatrader.com/"), applicationKey,
				username, password);
	}

	/**
	 * Submit a CAPTCHA already hosted on an existing website.
	 * 
	 * @param uri
	 *            The URI of the CAPTCHA image.
	 * @return The decoded CAPTCHA.
	 * @throws Any
	 *             exceptions sent by the server.
	 */
	public ResolvedCaptcha submit(URI uri) throws CaptchaTraderException,
			IOException {
		final URI requestUri = apiURI.resolve("submit");
		final HttpPost request = new HttpPost(requestUri);
		final MultipartEntity entity = new MultipartEntity();

		entity.addPart("api_key", new StringBody(applicationKey));
		entity.addPart("username", new StringBody(username));
		entity.addPart("password", new StringBody(password));
		entity.addPart("value", new StringBody(uri.toString()));

		request.setEntity(entity);
		final List<Object> response = validate(execute(request));

		return new ResolvedCaptcha(this, ((Long) response.get(0)).toString(),
				(String) response.get(1));
	}

	/**
	 * Responds if an CAPTCHA wes correctly answered or not
	 * 
	 * @param captcha
	 *            the CAPTCHA object
	 * @param state
	 *            <code>true</code> if the CAPTCHA was correctly resolved
	 * @throws CaptchaTraderException
	 *             any of the possible errors
	 */
	public void respond(ResolvedCaptcha captcha, boolean state)
			throws CaptchaTraderException, IOException {
		final URI requestUri = apiURI.resolve("respond");
		final HttpPost request = new HttpPost(requestUri);
		final MultipartEntity entity = new MultipartEntity();

		entity.addPart("is_correct", new StringBody(state ? "1" : "0"));
		entity.addPart("password", new StringBody(password));
		entity.addPart("ticket", new StringBody(captcha.getID()));
		entity.addPart("username", new StringBody(username));

		request.setEntity(entity);
		validate(execute(request));
	}

	/**
	 * Checks the amount of resolutions available on a users account
	 * 
	 * @return The decoded CAPTCHA.
	 * @throws Any
	 *             exceptions sent by the server.
	 */
	public int getCredits() throws CaptchaTraderException, IOException {
		return ((Number) validate(
				execute(new HttpGet(apiURI.resolve("get_credits/username:"
						+ username + "/password:" + password + "/")))).get(1))
				.intValue();
	}

	private List<Object> validate(List<Object> response)
			throws CaptchaTraderException {
		if ((long) response.get(0) == -1) {
			throw translateException((String) response.get(1));
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	private List<Object> execute(HttpUriRequest request) throws IOException {
		final HttpResponse response = client.execute(request);
		try {
			return (List<Object>) json.parse(new InputStreamReader(response
					.getEntity().getContent()));
		} catch (IllegalStateException | ParseException e) {
			throw new IOException(e);
		}
	}

	private CaptchaTraderException translateException(String error) {
		switch (error) {
		case "API KEY DISABLED":
			return new ApplicationKeyDisabledException();
		case " CONNECTION LIMIT":
			return new ConnectionLimitException();
		case "DAILY LIMIT":
			return new DailyLimitException();
		case "IMAGE TOO LARGE":
			return new ImageTooLargeException();
		case "INSUFFICIENT CREDITS":
			return new InsuficientCreditsException();
		case "INTERNAL ERROR":
			return new InternalErrorException();
		case "INVALID API KEY":
			return new InvalidApplicationKeyException();
		case "INVALID PARAMETERS":
			return new InvalidParametersException();
		case "INVALID URI":
			return new InvalidURLException();
		case "INVALID USER":
			return new InvalidUserException();
		case "USER NOT VALIDATED":
			return new UserNotValidatedException();
		case "NOT AN IMAGE":
			return new NotAnImageException();
		case "SUBMISSION ERROR":
			return new SubmissionErrorException();
		case "INCORRECT REPORTS":
			return new IncorrectRespondsException();
		case "INVALID TICKET":
			return new InvalidTicketException();
		default:
			return new CaptchaTraderException(error);
		}

	}
}
