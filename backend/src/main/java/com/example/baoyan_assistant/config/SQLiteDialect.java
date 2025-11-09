package com.example.baoyan_assistant.config;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitOffsetLimitHandler;

/**
 * SQLite 数据库方言类
 * 用于支持 Spring Data JPA 与 SQLite 数据库的交互
 * 兼容 Hibernate 6.x (Spring Boot 3.x)
 * 
 * 注意：Hibernate 6 的 Dialect API 发生了重大变化
 * 这个实现提供了 SQLite 所需的基本功能
 */
public class SQLiteDialect extends Dialect {
    
    private static final LimitHandler LIMIT_HANDLER = new LimitOffsetLimitHandler();
    
    public SQLiteDialect() {
        // Hibernate 6 的构造函数
        super();
    }

    @Override
    public LimitHandler getLimitHandler() {
        return LIMIT_HANDLER;
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitString(String sql, boolean hasOffset) {
        if (hasOffset) {
            return sql + " limit ? offset ?";
        } else {
            return sql + " limit ?";
        }
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

    @Override
    public boolean supportsCascadeDelete() {
        return false;
    }

    @Override
    public boolean supportsLockTimeouts() {
        return false;
    }

    @Override
    public String getForUpdateString() {
        return "";
    }

    @Override
    public String getForUpdateString(String aliases) {
        return "";
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public String getDropForeignKeyString() {
        return "";
    }

    @Override
    public String getAddForeignKeyConstraintString(
            String constraintName,
            String[] foreignKey,
            String referencedTable,
            String[] primaryKey,
            boolean referencesPrimaryKey) {
        return "";
    }

    @Override
    public String getAddPrimaryKeyConstraintString(String constraintName) {
        return "";
    }

    @Override
    public String getSelectGUIDString() {
        return "select hex(randomblob(16))";
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new SQLiteIdentityColumnSupport();
    }

    /**
     * SQLite 自增列支持类
     */
    public static class SQLiteIdentityColumnSupport implements IdentityColumnSupport {
        @Override
        public boolean supportsIdentityColumns() {
            return true;
        }

        @Override
        public String getIdentitySelectString(String table, String column, int type) {
            return "select last_insert_rowid()";
        }

        @Override
        public String getIdentityColumnString(int type) {
            return "integer";
        }

        @Override
        public String getIdentityInsertString() {
            return "null";
        }

        @Override
        public boolean supportsInsertSelectIdentity() {
            return false;
        }

        @Override
        public String getIdentityInsertString(String table, String column, String values) {
            return getIdentityInsertString();
        }
    }
}

