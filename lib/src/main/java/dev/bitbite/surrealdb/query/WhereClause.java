package dev.bitbite.surrealdb.query;

public class WhereClause {

    private String field, operator;
    private Object value;
    
    public WhereClause(String field, String operator, Object value){
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String toString() {
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
            default:
                sb.append(value);
        }
        return sb.toString();
    }

}
