package org.hibernate.reactive.boot.impl;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.reactive.jpa.impl.ReactivePersisterClassResolverInitiator;
import org.hibernate.reactive.service.initiator.ReactiveConnectionProviderInitiator;
import org.hibernate.reactive.service.initiator.DummyConnectionProviderInitiator;
import org.hibernate.reactive.service.initiator.DialectFromUrlJdbcEnvironmentInitiator;
import org.hibernate.reactive.service.initiator.ReactiveQueryTranslatorFactoryInitiator;
import org.hibernate.reactive.service.initiator.ReactiveTransactionCoordinatorBuilderInitiator;
import org.hibernate.service.spi.ServiceContributor;

/**
 * Contributes our
 * {@link org.hibernate.service.spi.ServiceInitiator service initiators}
 * to Hibernate.
 */
public class ReactiveServiceRegistryInitializer implements ServiceContributor {

	@Override
	public void contribute(StandardServiceRegistryBuilder serviceRegistryBuilder) {
		serviceRegistryBuilder.addInitiator( DummyConnectionProviderInitiator.INSTANCE );
		serviceRegistryBuilder.addInitiator( DialectFromUrlJdbcEnvironmentInitiator.INSTANCE );
		serviceRegistryBuilder.addInitiator( ReactiveConnectionProviderInitiator.INSTANCE );
		serviceRegistryBuilder.addInitiator( ReactiveTransactionCoordinatorBuilderInitiator.INSTANCE );
		serviceRegistryBuilder.addInitiator( ReactivePersisterClassResolverInitiator.INSTANCE );
		serviceRegistryBuilder.addInitiator( ReactiveQueryTranslatorFactoryInitiator.INSTANCE );
	}
}