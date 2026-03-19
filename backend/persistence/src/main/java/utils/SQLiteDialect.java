package utils;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;

public class SQLiteDialect extends Dialect {
    public SQLiteDialect() {
        super();
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new SQLiteIdentityColumnSupport();
    }

    @Override
    public boolean hasAlterTable() {
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
    public String getAddForeignKeyConstraintString(String cn,
                                                   String[] fk, String t, String[] pk, boolean rf) {
        return "";
    }

    @Override
    public String getAddPrimaryKeyConstraintString(String constraintName) {
        return "";
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

}
