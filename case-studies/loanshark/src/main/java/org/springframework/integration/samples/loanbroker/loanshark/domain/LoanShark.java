package org.springframework.integration.samples.loanbroker.loanshark.domain;

import javax.persistence.Entity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.entity.RooEntity;

@Entity
@RooJavaBean
@RooToString
@RooEntity(finders = { "findLoanSharksByName" })
public class LoanShark {

    private String name;

    private Long counter;

    private Double averageRate;
}
