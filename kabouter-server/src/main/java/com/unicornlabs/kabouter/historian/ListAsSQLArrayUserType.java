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
 * ListAsSQLArrayUserType.java
 *
 * @author Mitchell Just <mitch.just@gmail.com>
 *
 * Description: Hibernate mappings for SQL Arrays
 */
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public abstract class ListAsSQLArrayUserType<T> implements UserType {

    private static final int SQL_TYPE = Types.ARRAY;
    private static final int[] SQL_TYPES = {SQL_TYPE};

    abstract protected Array getDataAsArray(Object value);

    abstract protected List<T> getDataFromArray(Object primitivesArray);

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$BOOLEAN"
     * hibernate.column name="fieldName" sql-type="bool[]"
     */
    public static class BOOLEAN extends ListAsSQLArrayUserType<Boolean> {

        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SQLArray.BOOLEAN((List<Boolean>) value);
        }

        @Override
        protected List<Boolean> getDataFromArray(Object array) {
            boolean[] booleans = (boolean[]) array;
            ArrayList<Boolean> result = new ArrayList<Boolean>(booleans.length);
            for (boolean b : booleans) {
                result.add(b);
            }

            return result;
        }
    }

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$INTEGER"
     * hibernate.column name="fieldName" sql-type="int[]"
     */
    public static class INTEGER extends ListAsSQLArrayUserType<Integer> {

        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SQLArray.INTEGER((List<Integer>) value);
        }

        @Override
        protected List<Integer> getDataFromArray(Object array) {
            int[] ints = (int[]) array;
            ArrayList<Integer> result = new ArrayList<Integer>(ints.length);
            for (int i : ints) {
                result.add(i);
            }

            return result;
        }
    }

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$FLOAT"
     * hibernate.column name="fieldName" sql-type="real[]"
     */
    public static class FLOAT extends ListAsSQLArrayUserType<Float> {

        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SQLArray.FLOAT((List<Float>) value);
        }

        @Override
        protected List<Float> getDataFromArray(Object array) {
            float[] floats = (float[]) array;
            ArrayList<Float> result = new ArrayList<Float>(floats.length);
            for (float f : floats) {
                result.add(f);
            }

            return result;
        }
    }

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$DOUBLE"
     * hibernate.column name="fieldName" sql-type="float8[]"
     */
    public static class DOUBLE extends ListAsSQLArrayUserType<Double> {

        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SQLArray.DOUBLE((List<Double>) value);
        }

        @Override
        protected List<Double> getDataFromArray(Object array) {
            double[] doubles = (double[]) array;
            ArrayList<Double> result = new ArrayList<Double>(doubles.length);
            for (double d : doubles) {
                result.add(d);
            }

            return result;
        }
    }

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$STRING"
     * hibernate.column name="fieldName" sql-type="text[]"
     */
    public static class STRING extends ListAsSQLArrayUserType<String> {

        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SQLArray.STRING((List<String>) value);
        }

        @Override
        protected List<String> getDataFromArray(Object array) {
            String[] strings = (String[]) array;
            ArrayList<String> result = new ArrayList<String>(strings.length);
            result.addAll(Arrays.asList(strings));

            return result;
        }
    }

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$DATE"
     * hibernate.column name="fieldName" sql-type="timestamp[]"
     */
    public static class DATE extends ListAsSQLArrayUserType<Date> {

        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SQLArray.DATE((List<Date>) value);
        }

        @Override
        protected List<Date> getDataFromArray(Object array) {
            Date[] dates = (Date[]) array;
            ArrayList<Date> result = new ArrayList<Date>(dates.length);
            result.addAll(Arrays.asList(dates));

            return result;
        }
    }

    /**
     * Warning, this one is special. You have to define a class that extends
     * ENUM_LIST&lt;E&gt; and that has a no arguments constructor. For example :
     * class MyEnumsList extends ENUM_LIST&&ltMyEnumType&gt; { public
     * MyEnumList(){ super( MyEnum.values() ); } } Then, define :
     * hibernate.property type="com.myPackage.MyEnumsList" hibernate.column
     * name="fieldName" sql-type="int[]"
     */
    public static class ENUM<E extends Enum<E>> extends ListAsSQLArrayUserType<E> {

        private E[] theEnumValues;

        /**
         * @param clazz the class of the enum.
         * @param theEnumValues The values of enum (by invoking .values()).
         */
        protected ENUM(E[] theEnumValues) {
            this.theEnumValues = theEnumValues;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            List<E> enums = (List<E>) value;
            List<Integer> integers = new ArrayList<Integer>(enums.size());
            for (E theEnum : enums) {
                integers.add(theEnum.ordinal());
            }

            return new SQLArray.INTEGER(integers);
        }

        @Override
        protected List<E> getDataFromArray(Object array) {
            int[] ints = (int[]) array;
            ArrayList<E> result = new ArrayList<E>(ints.length);
            for (int val : ints) {
                for (int i = 0; i < theEnumValues.length; i++) {
                    if (theEnumValues[i].ordinal() == val) {
                        result.add(theEnumValues[i]);
                        break;
                    }
                }
            }

            if (result.size() != ints.length) {
                throw new RuntimeException("Error attempting to convert " + array + " into an array of enums (" + theEnumValues + ").");
            }

            return result;
        }
    }

    @Override
    public Class returnedClass() {
        return List.class;
    }

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Object deepCopy(Object value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @SuppressWarnings("unused")
    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
            throws HibernateException, SQLException {

        Array SQLArray = resultSet.getArray(names[0]);
        if (resultSet.wasNull()) {
            return null;
        }

        return getDataFromArray(SQLArray.getArray());
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(index, SQL_TYPE);
        } else {
            preparedStatement.setArray(index, getDataAsArray(value));
        }
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (null == x || null == y) {
            return false;
        }
        Class javaClass = returnedClass();
        if (!javaClass.equals(x.getClass()) || !javaClass.equals(y.getClass())) {
            return false;
        }

        return x.equals(y);
    }

    @SuppressWarnings("unused")
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @SuppressWarnings("unused")
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
