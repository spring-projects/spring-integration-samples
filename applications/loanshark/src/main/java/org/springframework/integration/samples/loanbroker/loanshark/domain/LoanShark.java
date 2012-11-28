/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.samples.loanbroker.loanshark.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
public class LoanShark {

	private String name;

	private Long counter;

	private Double averageRate;

	public String toJson() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

	public static LoanShark fromJsonToLoanShark(String json) {
		return new JSONDeserializer<LoanShark>().use(null, LoanShark.class).deserialize(json);
	}

	public static String toJsonArray(Collection<LoanShark> collection) {
		return new JSONSerializer().exclude("*.class").serialize(collection);
	}

	public static Collection<LoanShark> fromJsonArrayToLoanSharks(String json) {
		return new JSONDeserializer<List<LoanShark>>().use(null, ArrayList.class).use("values", LoanShark.class).deserialize(json);
	}

	@PersistenceContext
	transient EntityManager entityManager;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			LoanShark attached = this.entityManager.find(this.getClass(), this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		this.entityManager.flush();
	}

	@Transactional
	public LoanShark merge() {
		if (this.entityManager == null) {
			this.entityManager = entityManager();
		}
		LoanShark merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static final EntityManager entityManager() {
		EntityManager em = new LoanShark().entityManager;
		if (em == null) {
			throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		}
		return em;
	}

	public static long countLoanSharks() {
		return ((Number) entityManager().createQuery("select count(o) from LoanShark o").getSingleResult()).longValue();
	}

	@SuppressWarnings("unchecked")
	public static List<LoanShark> findAllLoanSharks() {
		return entityManager().createQuery("select o from LoanShark o").getResultList();
	}

	public static LoanShark findLoanShark(Long id) {
		if (id == null) return null;
		return entityManager().find(LoanShark.class, id);
	}

	@SuppressWarnings("unchecked")
	public static List<LoanShark> findLoanSharkEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("select o from LoanShark o").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCounter() {
		return this.counter;
	}

	public void setCounter(Long counter) {
		this.counter = counter;
	}

	public Double getAverageRate() {
		return this.averageRate;
	}

	public void setAverageRate(Double averageRate) {
		this.averageRate = averageRate;
	}

	public static Query findLoanSharksByName(String name) {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException("The name argument is required");
		}
		EntityManager em = LoanShark.entityManager();
		Query q = em.createQuery("SELECT LoanShark FROM LoanShark AS loanshark WHERE loanshark.name = :name");
		q.setParameter("name", name);
		return q;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ").append(getId()).append(", ");
		sb.append("Version: ").append(getVersion()).append(", ");
		sb.append("Name: ").append(getName()).append(", ");
		sb.append("Counter: ").append(getCounter()).append(", ");
		sb.append("AverageRate: ").append(getAverageRate());
		return sb.toString();
	}
}
