package com.zhang.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import org.springframework.transaction.support.ResourceHolderSupport;


public class ConnectionHolder extends ResourceHolderSupport {
    public static final String SAVEPOINT_NAME_PREFIX = "SAVEPOINT_";

    private ConnectionHandle connectionHandle;

    private Connection currentConnection;
    private boolean transactionActive;

    private Boolean savepointsSupported;
    private int savepointCounter;

    public ConnectionHolder(ConnectionHandle connectionHandle) {
        this.transactionActive = false;
        this.savepointCounter = 0;
        this.connectionHandle = connectionHandle;
    }

    public ConnectionHolder(Connection connection) {
        this.transactionActive = false;
        this.savepointCounter = 0;
        this.connectionHandle = new SimpleConnectionHandle(connection);
    }

    public ConnectionHolder(Connection connection, boolean transactionActive) {
        this(connection);
        this.transactionActive = transactionActive;
    }


    public ConnectionHandle getConnectionHandle() {
        return this.connectionHandle;
    }

    protected boolean hasConnection() {
        return this.connectionHandle != null;
    }

    protected void setTransactionActive(boolean transactionActive) {
        this.transactionActive = transactionActive;
    }

    protected boolean isTransactionActive() {
        return this.transactionActive;
    }

    protected void setConnection(Connection connection) {
        if (this.currentConnection != null) {
            if (this.connectionHandle != null) {
                this.connectionHandle.releaseConnection(this.currentConnection);
            }

            this.currentConnection = null;
        }

        if (connection != null) {
            this.connectionHandle = new SimpleConnectionHandle(connection);
        } else {
            this.connectionHandle = null;
        }

    }

    public Connection getConnection() {
        if (this.currentConnection == null) {
            this.currentConnection = this.connectionHandle.getConnection();
        }

        return this.currentConnection;
    }

    public boolean supportsSavepoints() throws SQLException {
        if (this.savepointsSupported == null) {
            this.savepointsSupported = this.getConnection().getMetaData().supportsSavepoints();
        }

        return this.savepointsSupported;
    }

    public Savepoint createSavepoint() throws SQLException {
        ++this.savepointCounter;
        return this.getConnection().setSavepoint("SAVEPOINT_" + this.savepointCounter);
    }

    public void released() {
        super.released();
        if (!this.isOpen() && this.currentConnection != null) {
            if (this.connectionHandle != null) {
                this.connectionHandle.releaseConnection(this.currentConnection);
            }

            this.currentConnection = null;
        }

    }

    public void clear() {
        super.clear();
        this.transactionActive = false;
        this.savepointsSupported = null;
        this.savepointCounter = 0;
    }
}
