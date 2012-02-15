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
package org.springframework.integration.samples.poller;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.util.Assert;


/**
 * This is a dynamically changeable {@link Trigger}. It is based on the 
 * {@link PeriodicTrigger} implementations. However, the fields of this dynamic 
 * trigger are not final and the properties can be inspected and set via 
 * explicit getters and setters.
 * 
 * @author Gunnar Hillert
 *
 */
public class DynamicPeriodicTrigger implements Trigger {
	
	private volatile long period;

	private volatile TimeUnit timeUnit;

	private volatile long initialDelay = 0;

	private volatile boolean fixedRate = false;

	/**
	 * Create a trigger with the given period in milliseconds. The underlying
	 * {@link TimeUnit} will be initialized to TimeUnit.MILLISECONDS.
	 * 
	 * @param period Must not be negative
	 */
	public DynamicPeriodicTrigger(long period) {
		this(period, TimeUnit.MILLISECONDS);
	}

	/**
	 * Create a trigger with the given period and time unit. The time unit will
	 * apply not only to the period but also to any 'initialDelay' value, if
	 * configured on this Trigger later via {@link #setInitialDelay(long)}.
	 * 
	 * @param period Must not be negative
	 * @param timeUnit Must not be null
	 * 
	 */
	public DynamicPeriodicTrigger(long period, TimeUnit timeUnit) {
		Assert.isTrue(period >= 0, "period must not be negative");
		Assert.notNull(timeUnit, "timeUnit must not be null");
		
		this.timeUnit = timeUnit;
		this.period = this.timeUnit.toMillis(period);
	}
	
	/**
	 * Specify the delay for the initial execution. It will be evaluated in
	 * terms of this trigger's {@link TimeUnit}. If no time unit was explicitly
	 * provided upon instantiation, the default is milliseconds. 
	 */
	public void setInitialDelay(long initialDelay) {
		Assert.isTrue(initialDelay >= 0, "initialDelay must not be negative");
		this.initialDelay = this.timeUnit.toMillis(initialDelay);
	}

	/**
	 * Specify whether the periodic interval should be measured between the
	 * scheduled start times rather than between actual completion times.
	 * The latter, "fixed delay" behavior, is the default.
	 */
	public void setFixedRate(boolean fixedRate) {
		this.fixedRate = fixedRate;
	}

	/**
	 * Returns the time after which a task should run again.
	 */
	public Date nextExecutionTime(TriggerContext triggerContext) {
		if (triggerContext.lastScheduledExecutionTime() == null) {
			return new Date(System.currentTimeMillis() + this.initialDelay);
		}
		else if (this.fixedRate) {
			return new Date(triggerContext.lastScheduledExecutionTime().getTime() + this.period);
		}
		return new Date(triggerContext.lastCompletionTime().getTime() + this.period);
	}

	public long getPeriod() {
		return period;
	}

	/**
	 * Specify the period of the trigger. It will be evaluated in
	 * terms of this trigger's {@link TimeUnit}. If no time unit was explicitly
	 * provided upon instantiation, the default is milliseconds. 
	 * 
	 * @param period Must not be negative
	 */
	public void setPeriod(long period) {
		Assert.isTrue(period >= 0, "period must not be negative");
		this.period = this.timeUnit.toMillis(period);
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		Assert.notNull(timeUnit, "timeUnit must not be null");
		this.timeUnit = timeUnit;
	}

	public long getInitialDelay() {
		return initialDelay;
	}

	public boolean isFixedRate() {
		return fixedRate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DynamicPeriodicTrigger)) {
			return false;
		}
		DynamicPeriodicTrigger other = (DynamicPeriodicTrigger) obj;
		return this.fixedRate == other.fixedRate
				&& this.initialDelay == other.initialDelay
				&& this.period == other.period;
	}

	@Override
	public int hashCode() {
		return (this.fixedRate ? 14 : 41) +
				(int) (38 * this.period) +
				(int) (43 * this.initialDelay); 
	}

}
