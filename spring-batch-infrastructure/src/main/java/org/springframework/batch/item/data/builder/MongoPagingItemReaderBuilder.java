/*
 * Copyright 2017-2025 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.springframework.batch.item.data.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Builder for {@link MongoPagingItemReader}.
 *
 * @param <T> type of items to read.
 * @author Glenn Renfro
 * @author Mahmoud Ben Hassine
 * @author Drummond Dawson
 * @author Parikshit Dutta
 * @since 5.1
 */
public class MongoPagingItemReaderBuilder<T> {

	protected MongoOperations template;

	protected String jsonQuery;

	protected Class<? extends T> targetType;

	protected Map<String, Sort.Direction> sorts;

	protected String hint;

	protected String fields;

	protected String collection;

	protected List<Object> parameterValues = new ArrayList<>();

	protected int pageSize = 10;

	protected boolean saveState = true;

	protected String name;

	protected int maxItemCount = Integer.MAX_VALUE;

	protected int currentItemCount;

	protected Query query;

	/**
	 * Configure if the state of the
	 * {@link org.springframework.batch.item.ItemStreamSupport} should be persisted within
	 * the {@link org.springframework.batch.item.ExecutionContext} for restart purposes.
	 * @param saveState defaults to true
	 * @return The current instance of the builder.
	 */
	public MongoPagingItemReaderBuilder<T> saveState(boolean saveState) {
		this.saveState = saveState;

		return this;
	}

	/**
	 * The name used to calculate the key within the
	 * {@link org.springframework.batch.item.ExecutionContext}. Required if
	 * {@link #saveState(boolean)} is set to true.
	 * @param name name of the reader instance
	 * @return The current instance of the builder.
	 * @see org.springframework.batch.item.ItemStreamSupport#setName(String)
	 */
	public MongoPagingItemReaderBuilder<T> name(String name) {
		this.name = name;

		return this;
	}

	/**
	 * Configure the max number of items to be read.
	 * @param maxItemCount the max items to be read
	 * @return The current instance of the builder.
	 * @see org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader#setMaxItemCount(int)
	 */
	public MongoPagingItemReaderBuilder<T> maxItemCount(int maxItemCount) {
		this.maxItemCount = maxItemCount;

		return this;
	}

	/**
	 * Index for the current item. Used on restarts to indicate where to start from.
	 * @param currentItemCount current index
	 * @return this instance for method chaining
	 * @see org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader#setCurrentItemCount(int)
	 */
	public MongoPagingItemReaderBuilder<T> currentItemCount(int currentItemCount) {
		this.currentItemCount = currentItemCount;

		return this;
	}

	/**
	 * Used to perform operations against the MongoDB instance. Also handles the mapping
	 * of documents to objects.
	 * @param template the MongoOperations instance to use
	 * @see MongoOperations
	 * @return The current instance of the builder
	 * @see MongoPagingItemReader#setTemplate(MongoOperations)
	 */
	public MongoPagingItemReaderBuilder<T> template(MongoOperations template) {
		this.template = template;

		return this;
	}

	/**
	 * A JSON formatted MongoDB jsonQuery. Parameterization of the provided jsonQuery is
	 * allowed via ?&lt;index&gt; placeholders where the &lt;index&gt; indicates the index
	 * of the parameterValue to substitute.
	 * @param query JSON formatted Mongo jsonQuery
	 * @return The current instance of the builder
	 * @see MongoPagingItemReader#setQuery(String)
	 */
	public MongoPagingItemReaderBuilder<T> jsonQuery(String query) {
		this.jsonQuery = query;

		return this;
	}

	/**
	 * The type of object to be returned for each {@link MongoPagingItemReader#read()}
	 * call.
	 * @param targetType the type of object to return
	 * @return The current instance of the builder
	 * @see MongoPagingItemReader#setTargetType(Class)
	 */
	public MongoPagingItemReaderBuilder<T> targetType(Class<? extends T> targetType) {
		this.targetType = targetType;

		return this;
	}

	/**
	 * {@link List} of values to be substituted in for each of the parameters in the
	 * query.
	 * @param parameterValues values
	 * @return The current instance of the builder
	 * @see MongoPagingItemReader#setParameterValues(List)
	 */
	public MongoPagingItemReaderBuilder<T> parameterValues(List<Object> parameterValues) {
		this.parameterValues = parameterValues;

		return this;
	}

	/**
	 * Values to be substituted in for each of the parameters in the query.
	 * @param parameterValues values
	 * @return The current instance of the builder
	 * @see MongoPagingItemReader#setParameterValues(List)
	 */
	public MongoPagingItemReaderBuilder<T> parameterValues(Object... parameterValues) {
		return parameterValues(Arrays.asList(parameterValues));
	}

	/**
	 * JSON defining the fields to be returned from the matching documents by MongoDB.
	 * @param fields JSON string that identifies the fields to sort by.
	 * @return The current instance of the builder
	 * @see MongoPagingItemReader#setFields(String)
	 */
	public MongoPagingItemReaderBuilder<T> fields(String fields) {
		this.fields = fields;

		return this;
	}

	/**
	 * {@link Map} of property
	 * names/{@link org.springframework.data.domain.Sort.Direction} values to sort the
	 * input by.
	 * @param sorts map of properties and direction to sort each.
	 * @return The current instance of the builder
	 * @see MongoPagingItemReader#setSort(Map)
	 */
	public MongoPagingItemReaderBuilder<T> sorts(Map<String, Sort.Direction> sorts) {
		this.sorts = sorts;

		return this;
	}

	/**
	 * Establish an optional collection that can be queried.
	 * @param collection Mongo collection to be queried.
	 * @return The current instance of the builder
	 * @see MongoPagingItemReader#setCollection(String)
	 */
	public MongoPagingItemReaderBuilder<T> collection(String collection) {
		this.collection = collection;

		return this;
	}

	/**
	 * JSON String telling MongoDB what index to use.
	 * @param hint string indicating what index to use.
	 * @return The current instance of the builder
	 * @see MongoPagingItemReader#setHint(String)
	 */
	public MongoPagingItemReaderBuilder<T> hint(String hint) {
		this.hint = hint;

		return this;
	}

	/**
	 * The number of items to be read with each page.
	 * @param pageSize the number of items
	 * @return this instance for method chaining
	 * @see MongoPagingItemReader#setPageSize(int)
	 */
	public MongoPagingItemReaderBuilder<T> pageSize(int pageSize) {
		this.pageSize = pageSize;

		return this;
	}

	/**
	 * Provide a Spring Data Mongo {@link Query}. This will take precedence over a JSON
	 * configured query.
	 * @param query Query to execute
	 * @return this instance for method chaining
	 * @see MongoPagingItemReader#setQuery(Query)
	 */
	public MongoPagingItemReaderBuilder<T> query(Query query) {
		this.query = query;

		return this;
	}

	public MongoPagingItemReader<T> build() {
		Assert.notNull(this.template, "template is required.");
		if (this.saveState) {
			Assert.hasText(this.name, "A name is required when saveState is set to true");
		}
		Assert.notNull(this.targetType, "targetType is required.");
		Assert.state(StringUtils.hasText(this.jsonQuery) || this.query != null, "A query is required");

		if (StringUtils.hasText(this.jsonQuery) || this.query != null) {
			Assert.notNull(this.sorts, "sorts map is required.");
		}

		MongoPagingItemReader<T> reader = new MongoPagingItemReader<>();
		reader.setTemplate(this.template);
		reader.setTargetType(this.targetType);
		reader.setQuery(this.jsonQuery);
		reader.setSort(this.sorts);
		reader.setHint(this.hint);
		reader.setFields(this.fields);
		reader.setCollection(this.collection);
		reader.setParameterValues(this.parameterValues);
		reader.setQuery(this.query);

		reader.setPageSize(this.pageSize);
		reader.setName(this.name);
		reader.setSaveState(this.saveState);
		reader.setCurrentItemCount(this.currentItemCount);
		reader.setMaxItemCount(this.maxItemCount);

		return reader;
	}

}
