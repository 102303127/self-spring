package com.zhang.jdbc.support;

import com.zhang.jdbc.JdbcException;
import org.springframework.lang.Nullable;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.*;


/**
 * @author zhang
 * @date 2024/8/7
 * @Description
 */
public class JdbcUtils {


    public static void closeConnection( Connection con) {
        if (con != null) {
            try {
                con.close();
            }
            catch (Throwable ex) {
                throw new JdbcException(ex.getMessage());
            }
        }
    }


    public static void closeStatement( Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (Throwable ex) {
                throw new JdbcException(ex.getMessage());
            }
        }
    }

    /**
     * Close the given JDBC ResultSet and ignore any thrown exception.
     * This is useful for typical finally blocks in manual JDBC code.
     * @param rs the JDBC ResultSet to close (may be {@code null})
     */
    public static void closeResultSet( ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (Throwable ex) {
                throw new JdbcException(ex.getMessage());
            }
        }
    }

    public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        String name = resultSetMetaData.getColumnLabel(columnIndex);
        if (!StringUtils.hasLength(name)) {
            name = resultSetMetaData.getColumnName(columnIndex);
        }

        return name;
    }


    public static Object getResultSetValue(ResultSet rs, int index, @Nullable Class<?> requiredType) throws SQLException {
        if (requiredType == null) {
            return getResultSetValue(rs, index);
        } else if (String.class == requiredType) {
            return rs.getString(index);
        } else {
            Object value;
            if (Boolean.TYPE != requiredType && Boolean.class != requiredType) {
                if (Byte.TYPE != requiredType && Byte.class != requiredType) {
                    if (Short.TYPE != requiredType && Short.class != requiredType) {
                        if (Integer.TYPE != requiredType && Integer.class != requiredType) {
                            if (Long.TYPE != requiredType && Long.class != requiredType) {
                                if (Float.TYPE != requiredType && Float.class != requiredType) {
                                    if (Double.TYPE != requiredType && Double.class != requiredType && Number.class != requiredType) {
                                        if (BigDecimal.class == requiredType) {
                                            return rs.getBigDecimal(index);
                                        }

                                        if (Date.class == requiredType) {
                                            return rs.getDate(index);
                                        }

                                        if (Time.class == requiredType) {
                                            return rs.getTime(index);
                                        }

                                        if (Timestamp.class != requiredType && java.util.Date.class != requiredType) {
                                            if (byte[].class == requiredType) {
                                                return rs.getBytes(index);
                                            }

                                            if (Blob.class == requiredType) {
                                                return rs.getBlob(index);
                                            }

                                            if (Clob.class == requiredType) {
                                                return rs.getClob(index);
                                            }

                                            if (requiredType.isEnum()) {
                                                Object obj = rs.getObject(index);
                                                if (obj instanceof String) {
                                                    return obj;
                                                }

                                                if (obj instanceof Number) {
                                                    return NumberUtils.convertNumberToTargetClass((Number)obj, Integer.class);
                                                }

                                                return rs.getString(index);
                                            }


                                            try {
                                                return rs.getObject(index, requiredType);
                                            } catch (AbstractMethodError | SQLException var5) {
                                                System.out.println(var5.getMessage());
                                            }

                                            String typeName = requiredType.getSimpleName();
                                            if ("LocalDate".equals(typeName)) {
                                                return rs.getDate(index);
                                            }

                                            if ("LocalTime".equals(typeName)) {
                                                return rs.getTime(index);
                                            }

                                            if ("LocalDateTime".equals(typeName)) {
                                                return rs.getTimestamp(index);
                                            }

                                            return getResultSetValue(rs, index);
                                        }

                                        return rs.getTimestamp(index);
                                    }

                                    value = rs.getDouble(index);
                                } else {
                                    value = rs.getFloat(index);
                                }
                            } else {
                                value = rs.getLong(index);
                            }
                        } else {
                            value = rs.getInt(index);
                        }
                    } else {
                        value = rs.getShort(index);
                    }
                } else {
                    value = rs.getByte(index);
                }
            } else {
                value = rs.getBoolean(index);
            }

            return rs.wasNull() ? null : value;
        }
    }


    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }

        if (obj instanceof Blob) {
            Blob blob = (Blob)obj;
            obj = blob.getBytes(1L, (int)blob.length());
        } else if (obj instanceof Clob) {
            Clob clob = (Clob)obj;
            obj = clob.getSubString(1L, (int)clob.length());
        } else if (!"oracle.sql.TIMESTAMP".equals(className) && !"oracle.sql.TIMESTAMPTZ".equals(className)) {
            if (className != null && className.startsWith("oracle.sql.DATE")) {
                String metaDataClassName = rs.getMetaData().getColumnClassName(index);
                if (!"java.sql.Timestamp".equals(metaDataClassName) && !"oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
                    obj = rs.getDate(index);
                } else {
                    obj = rs.getTimestamp(index);
                }
            } else if (obj instanceof Date && "java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
                obj = rs.getTimestamp(index);
            }
        } else {
            obj = rs.getTimestamp(index);
        }

        return obj;
    }

}
