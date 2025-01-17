/* Hibernate, Relational Persistence for Idiomatic Java
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright: Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.reactive.query.sqm.mutation.internal.temptable;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletionStage;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.query.spi.DomainQueryExecutionContext;
import org.hibernate.query.sqm.internal.DomainParameterXref;
import org.hibernate.query.sqm.mutation.internal.temptable.AfterUseAction;
import org.hibernate.query.sqm.mutation.internal.temptable.LocalTemporaryTableInsertStrategy;
import org.hibernate.query.sqm.tree.insert.SqmInsertStatement;
import org.hibernate.reactive.logging.impl.Log;
import org.hibernate.reactive.logging.impl.LoggerFactory;
import org.hibernate.reactive.query.sqm.mutation.spi.ReactiveSqmMultiTableInsertStrategy;

public class ReactiveLocalTemporaryTableInsertStrategy extends LocalTemporaryTableInsertStrategy
		implements ReactiveSqmMultiTableInsertStrategy {

	private static final Log LOG = LoggerFactory.make( Log.class, MethodHandles.lookup() );

	public ReactiveLocalTemporaryTableInsertStrategy(LocalTemporaryTableInsertStrategy insertStrategy) {
		super( insertStrategy.getTemporaryTable(), insertStrategy.getSessionFactory() );
	}

	@Override
	public CompletionStage<Integer> reactiveExecuteInsert(
			SqmInsertStatement<?> sqmInsertStatement,
			DomainParameterXref domainParameterXref,
			DomainQueryExecutionContext context) {
		return new ReactiveTableBasedInsertHandler(
				sqmInsertStatement,
				domainParameterXref,
				getTemporaryTable(),
				afterUserAction(),
				ReactiveLocalTemporaryTableInsertStrategy::throwUnexpectedCallToSessionUIDError,
				getSessionFactory()
		).reactiveExecute( context );
	}

	private static String throwUnexpectedCallToSessionUIDError(SharedSessionContractImplementor session) {
		throw new UnsupportedOperationException( "Unexpected call to access Session uid" );
	}

	private AfterUseAction afterUserAction() {
		return isDropIdTables()
				? AfterUseAction.DROP
				: getSessionFactory().getJdbcServices().getDialect().getTemporaryTableAfterUseAction();
	}
}
