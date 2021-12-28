package com.allsaints.music.entity.listener;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configurable
public class AuditListener {
	
	private static final String OP_TYPE_INSERT = "insert: {}";
	private static final String OP_TYPE_UPDATE = "update: {}";
	private static final String OP_TYPE_DELETE = "delete: {}";
	

	@PostPersist
	public void logForPersist(Object target) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                	super.afterCommit();
                	if(log.isDebugEnabled()) log.info(OP_TYPE_INSERT, target);
                }
            });
        }
        else {
        	if(log.isDebugEnabled()) log.info(OP_TYPE_INSERT, target);
        }
	}
	
	@PostUpdate
	public void logForUpdate(Object target) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                	super.afterCommit();
                	if(log.isDebugEnabled()) log.info(OP_TYPE_UPDATE, target);
                }
            });
        }
        else {
        	 log.info(OP_TYPE_UPDATE, target);
        }
	}
	
	@PostRemove
	public void logForRemove(Object target) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                	super.afterCommit();
                	if(log.isDebugEnabled()) log.info(OP_TYPE_DELETE, target);
                }
            });
        }
        else {
        	if(log.isDebugEnabled()) log.info(OP_TYPE_DELETE, target);
        }
	}
	
}
