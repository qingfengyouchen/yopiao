package com.base.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class MyJpaRepositoryFactoryBean<T extends JpaRepository<Object, Serializable>>
		extends JpaRepositoryFactoryBean<T, Object, Serializable> {

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(
			EntityManager entityManager) {

		return new MyJpaRepositoryFactory(entityManager);
	}
}
