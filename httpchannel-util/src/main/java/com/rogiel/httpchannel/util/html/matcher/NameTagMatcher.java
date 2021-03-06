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
package com.rogiel.httpchannel.util.html.matcher;

import org.htmlparser.Tag;

import com.rogiel.httpchannel.util.html.PageElement.TagMatcher;

/**
 * An {@link TagMatcher} that always returns the tag name
 * 
 * @author <a href="http://www.rogiel.com">Rogiel</a>
 */
public class NameTagMatcher<T extends Tag> implements TagMatcher<T> {
	@Override
	public String content(T tag) {
		return tag.getAttribute("name");
	}

}
