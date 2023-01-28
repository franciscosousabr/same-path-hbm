package org.hibernate.bugs;

import static org.junit.Assert.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;
	private Path<Object> namePath;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void querySamePath() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		persistCustomers(entityManager);

		CriteriaQuery<Customer> listCriteriaQuery = getListCriteriaQuery(
				entityManager, false);
		TypedQuery<Customer> listQuery = entityManager.createQuery(listCriteriaQuery);
		List<Customer> customers = listQuery.getResultList();

		assertEquals(1, customers.size());

		CriteriaQuery<Long> countCriteriaQuery = getCountCriteriaQuery(
				entityManager, false);
		TypedQuery<Long> countQuery = entityManager.createQuery(countCriteriaQuery);
		long count = countQuery.getSingleResult();

		assertEquals(1L, count);

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	public void queryDifferentPath() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		persistCustomers(entityManager);

		CriteriaQuery<Customer> listCriteriaQuery = getListCriteriaQuery(
				entityManager, true);
		TypedQuery<Customer> listQuery = entityManager.createQuery(listCriteriaQuery);
		List<Customer> customers = listQuery.getResultList();

		assertEquals(1, customers.size());

		CriteriaQuery<Long> countCriteriaQuery = getCountCriteriaQuery(
				entityManager, true);
		TypedQuery<Long> countQuery = entityManager.createQuery(countCriteriaQuery);
		long count = countQuery.getSingleResult();

		assertEquals(1L, count);

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private static void persistCustomers(EntityManager entityManager) {
		Customer customer = new Customer(1, "Francisco");
		entityManager.persist(customer);
	}

	private CriteriaQuery<Customer> getListCriteriaQuery(EntityManager entityManager, boolean recreatePath) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
		Root<Customer> root = criteriaQuery.from(Customer.class);

		if (namePath == null || recreatePath) {
			namePath = root.get("name");
		}

		criteriaQuery.where(builder.equal(namePath, "Francisco"));
		return criteriaQuery;
	}

	private CriteriaQuery<Long> getCountCriteriaQuery(EntityManager entityManager, boolean recreatePath) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Customer> root = criteriaQuery.from(Customer.class);
		criteriaQuery.select(builder.count(root));

		if (namePath == null || recreatePath) {
			namePath = root.get("name");
		}

		criteriaQuery.where(builder.equal(namePath, "Francisco"));
		return criteriaQuery;
	}
}
