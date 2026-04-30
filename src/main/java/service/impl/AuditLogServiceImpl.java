package service.impl;

import database.AuditLogDAO;
import database.impl.AuditLogDAOImpl;
import lombok.extern.slf4j.Slf4j;
import service.AuditLogService;

/**
 * AuditLogService の実装クラスです。
 */
@Slf4j
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogDAO auditLogDAO;

    public AuditLogServiceImpl() {
        this.auditLogDAO = new AuditLogDAOImpl();
    }

    public AuditLogServiceImpl(AuditLogDAO auditLogDAO) {
        this.auditLogDAO = auditLogDAO;
    }

    @Override
    public void log(String tableName, String recordId, String action, String oldValue, String newValue, String operatorId) {
        auditLogDAO.log(tableName, recordId, action, oldValue, newValue, operatorId);
    }

    @Override
    public void log(java.sql.Connection con, String tableName, String recordId, String action, String oldValue, String newValue, String operatorId) {
        auditLogDAO.log(con, tableName, recordId, action, oldValue, newValue, operatorId);
    }
}
