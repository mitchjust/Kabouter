// <editor-fold defaultstate="collapsed" desc="License">
/*
 * Copyright 2012 Mitchell Just <mitch.just@gmail.com>.
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
// </editor-fold>
package com.unicornlabs.kabouter.historian;

/**
 * SQLArray.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Hibernate mappings for arrays of basic types
 */
import java.sql.*;
import java.util.Date;
import java.util.*;

class SQLArray<T> implements Array {

    private List<T> data;
    private int baseType;
    private String baseTypeName = null;

    protected SQLArray(List<T> data, int baseType) {
        this.data = data;
        this.baseType = baseType;
    }

    protected SQLArray(List<T> data, int baseType, String baseTypeName) {
        this(data, baseType);
        this.baseTypeName = baseTypeName;
    }

    @Override
    public void free() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static class BOOLEAN extends SQLArray<Boolean> {

        public BOOLEAN(List<Boolean> data) {
            super(data, Types.BIT);
        }
    }

    public static class INTEGER extends SQLArray<Integer> {

        public INTEGER(List<Integer> data) {
            super(data, Types.INTEGER);
        }
    }

    public static class FLOAT extends SQLArray<Float> {

        public FLOAT(List<Float> data) {
            super(data, Types.FLOAT);
        }
    }

    public static class DOUBLE extends SQLArray<Double> {

        public DOUBLE(List<Double> data) {
            super(data, Types.DOUBLE);
        }
    }

    public static class STRING extends SQLArray<String> {

        public STRING(List<String> data) {
            super(data, Types.VARCHAR, "text");
        }
    }

    public static class DATE extends SQLArray<Date> {

        public DATE(List<Date> data) {
            super(data, Types.TIMESTAMP);
        }
    }

    @Override
    public String getBaseTypeName() {
        if (baseTypeName != null) {
            return baseTypeName;
        }
        return Historian.GetSettings().getDialect().getTypeName(baseType);
    }

    @Override
    public int getBaseType() {
        return baseType;
    }

    @Override
    public Object getArray() {
        return data.toArray();
    }

    @Override
    public Object getArray(long index, int count) {
        int lastIndex = count - (int) index;
        if (lastIndex > data.size()) {
            lastIndex = data.size();
        }

        return data.subList((int) (index - 1), lastIndex).toArray();
    }

    @SuppressWarnings("unused")
    @Override
    public Object getArray(Map<String, Class<?>> arg0) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    @Override
    public Object getArray(long arg0, int arg1, Map<String, Class<?>> arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet getResultSet() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    @Override
    public ResultSet getResultSet(Map<String, Class<?>> arg0) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    @Override
    public ResultSet getResultSet(long index, int count) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    @Override
    public ResultSet getResultSet(long arg0, int arg1, Map<String, Class<?>> arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('{');
        boolean first = true;

        for (T t : data) {
            if (first) {
                first = false;
            } else {
                result.append(',');
            }

            if (t == null) {
                result.append("null");
                continue;
            }

            switch (baseType) {
                case Types.BIT:
                case Types.BOOLEAN:
                    result.append(((Boolean) t).booleanValue() ? "true" : "false");
                    break;

                case Types.INTEGER:
                case Types.FLOAT:
                case Types.DOUBLE:
                case Types.REAL:
                case Types.NUMERIC:
                case Types.DECIMAL:
                    result.append(t);
                    break;

                case Types.VARCHAR:
                    String s = (String) t;
                    // Escape the string
                    result.append('\"');
                    for (int p = 0; p < s.length(); ++p) {
                        char ch = s.charAt(p);
                        if (ch == '\0') {
                            throw new IllegalArgumentException("Zero bytes may not occur in string parameters.");
                        }
                        if (ch == '\\' || ch == '"') {
                            result.append('\\');
                        }
                        result.append(ch);
                    }
                    result.append('\"');
                    break;

                case Types.TIMESTAMP:
                    Date d = (Date) t;
                    result.append('\'');
                    appendDate(result, d);
                    result.append(d);
                    result.append('\'');
                    break;

                default:
                    throw new UnsupportedOperationException("Unsupported type " + baseType + " / " + getBaseTypeName());
            }
        }

        result.append('}');

        return result.toString();
    }
    private static GregorianCalendar calendar = null;

    protected void appendDate(StringBuilder sb, Date date) {
        if (calendar == null) {
            calendar = new GregorianCalendar();
        }

        calendar.setTime(date);

        // Append Date
        {
            int l_year = calendar.get(Calendar.YEAR);
            // always use at least four digits for the year so very
            // early years, like 2, don't get misinterpreted
            //
            int l_yearlen = String.valueOf(l_year).length();
            for (int i = 4; i > l_yearlen; i--) {
                sb.append("0");
            }

            sb.append(l_year);
            sb.append('-');
            int l_month = calendar.get(Calendar.MONTH) + 1;
            if (l_month < 10) {
                sb.append('0');
            }
            sb.append(l_month);
            sb.append('-');
            int l_day = calendar.get(Calendar.DAY_OF_MONTH);
            if (l_day < 10) {
                sb.append('0');
            }
            sb.append(l_day);
        }

        sb.append(' ');

        // Append Time
        {
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            if (hours < 10) {
                sb.append('0');
            }
            sb.append(hours);

            sb.append(':');
            int minutes = calendar.get(Calendar.MINUTE);
            if (minutes < 10) {
                sb.append('0');
            }
            sb.append(minutes);

            sb.append(':');
            int seconds = calendar.get(Calendar.SECOND);
            if (seconds < 10) {
                sb.append('0');
            }
            sb.append(seconds);

            if (date instanceof Timestamp) {
                // Add nanoseconds.
                // This won't work for postgresql versions < 7.2 which only want
                // a two digit fractional second.

                Timestamp t = (Timestamp) date;
                char[] decimalStr = {'0', '0', '0', '0', '0', '0', '0', '0', '0'};
                char[] nanoStr = Integer.toString(t.getNanos()).toCharArray();
                System.arraycopy(nanoStr, 0, decimalStr, decimalStr.length - nanoStr.length, nanoStr.length);
                sb.append('.');
                sb.append(decimalStr, 0, 6);
            }
        }

        // Append Time Zone offset
        {
            //int offset = -(date.getTimezoneOffset());
            int offset = (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000);
            int absoff = Math.abs(offset);
            int hours = absoff / 60;
            int mins = absoff - hours * 60;

            sb.append((offset >= 0) ? "+" : "-");

            if (hours < 10) {
                sb.append('0');
            }
            sb.append(hours);

            if (mins < 10) {
                sb.append('0');
            }
            sb.append(mins);
        }

        // Append Era
        if (calendar.get(Calendar.ERA) == GregorianCalendar.BC) {
            sb.append(" BC");
        }
    }
}