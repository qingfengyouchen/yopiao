package com.base.jpa;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by micheal on 15/7/1.
 */
public class MyJpaRepositoryFactory extends JpaRepositoryFactory {

    /*public MyJpaRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected MyConcreteJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata, EntityManager em) {
        JpaEntityInformation<Object, Serializable> entityMetadata = mock(JpaEntityInformation.class);
        when(entityMetadata.getJavaType()).thenReturn((Class<Object>) metadata.getDomainType());
        return new MyConcreteJpaRepository<Object, Serializable>(entityMetadata, em);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        //return MyJpaRepository.class;
        if(MyJpaRepository.class.isAssignableFrom(metadata.getRepositoryInterface())){
            return MyConcreteJpaRepository.class;
        }
        return super.getRepositoryBaseClass(metadata);
    }*/

    private final EntityManager em;

    public MyJpaRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.em=entityManager;
    }

    @Override
    @SuppressWarnings({"rawtypes","unchecked"})
    protected Object getTargetRepository(RepositoryMetadata metadata) {
        if(MyJpaRepository.class.isAssignableFrom(metadata.getRepositoryInterface())){
            JpaEntityInformation<?,Serializable> entityInformation = getEntityInformation(metadata.getDomainType());
            MyConcreteJpaRepository<?,Serializable> repository = new MyConcreteJpaRepository(entityInformation, em);
            return repository;
        }
        return super.getTargetRepository(metadata);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if(MyJpaRepository.class.isAssignableFrom(metadata.getRepositoryInterface())){
            return MyConcreteJpaRepository.class;
        }
        return super.getRepositoryBaseClass(metadata);
    }

    @Override
    protected <T, ID extends Serializable> MyConcreteJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata, EntityManager entityManager) {
        JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());
        return new MyConcreteJpaRepository(entityInformation, entityManager); // custom implementation
    }


}
