package dev.bitbite.surrealdb.query;

import java.util.List;

/**
 * Represents a WHERE clause in a database query.
 */
public class WhereClause {

    private String field, operator;
    private Object value;
    private String combined;
    
    /**
     * Constructs a WhereClause object with the specified field, operator, and value.
     * 
     * @param field the field to compare
     * @param operator the comparison operator
     * @param value the value to compare against
     */
    public WhereClause(String field, String operator, Object value){
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Constructs a WhereClause object with the specified combined condition.
     * 
     * @param combined the combined condition
     */
    public WhereClause(String combined) {
        this.combined = combined;
    }

    /**
     * Returns the string representation of the WhereClause object.
     * 
     * @return the string representation of the WhereClause object
     */
    @Override
    public String toString() {
        if(combined != null){
            return combined;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(field);
        sb.append(" ");
        sb.append(operator);
        sb.append(" ");
        switch(value) {
            case String s:
                sb.append("'");
                sb.append(s);
                sb.append("'");
                break;
            case Integer i:
                sb.append(i);
                break;
            case Double d:
                sb.append(d);
                break;
            case Float f:
                sb.append(f);
                break;
            case Long l:
                sb.append(l);
                break;
            case Boolean b:
                sb.append(b);
                break;
            case List<?> list:
                sb.append("[");
                for(int i = 0; i < list.size(); i++){
                    sb.append(list.get(i));
                    if(i < list.size() - 1){
                        sb.append(", ");
                    }
                }
                sb.append("]");
                break;
            case String[] array:
                sb.append("[");
                for(int i = 0; i < array.length; i++){
                    sb.append("'");
                    sb.append(array[i]);
                    sb.append("'");
                    if(i < array.length - 1){
                        sb.append(", ");
                    }
                }
                sb.append("]");
                break;
            case int[] array:
                sb.append("[");
                for(int i = 0; i < array.length; i++){
                    sb.append(array[i]);
                    if(i < array.length - 1){
                        sb.append(", ");
                    }
                }
                sb.append("]");
                break;
            case double[] array:
                sb.append("[");
                for(int i = 0; i < array.length; i++){
                    sb.append(array[i]);
                    if(i < array.length - 1){
                        sb.append(", ");
                    }
                }
                sb.append("]");
                break;
            case float[] array:
                sb.append("[");
                for(int i = 0; i < array.length; i++){
                    sb.append(array[i]);
                    if(i < array.length - 1){
                        sb.append(", ");
                    }
                }
                sb.append("]");
                break;
            case long[] array:
                sb.append("[");
                for(int i = 0; i < array.length; i++){
                    sb.append(array[i]);
                    if(i < array.length - 1){
                        sb.append(", ");
                    }
                }
                sb.append("]");
                break;
            case boolean[] array:
                sb.append("[");
                for(int i = 0; i < array.length; i++){
                    sb.append(array[i]);
                    if(i < array.length - 1){
                        sb.append(", ");
                    }
                }
                sb.append("]");
                break;
            case Object[] array:
                sb.append("[");
                for(int i = 0; i < array.length; i++){
                    sb.append(array[i]);
                    if(i < array.length - 1){
                        sb.append(", ");
                    }
                }
                sb.append("]");
                break;
            default:
                sb.append(value);
        }
        return sb.toString();
    }

}
