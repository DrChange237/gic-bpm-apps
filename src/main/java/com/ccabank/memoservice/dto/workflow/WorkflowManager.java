package com.ccabank.memoservice.dto.workflow;

import com.ccabank.memoservice.entity.Approval;
import com.ccabank.memoservice.util.field.FieldUtils;

public class WorkflowManager {

    public static boolean  evaluateCondition(Condition condition, Approval approval) {
        // Vérifier si nous avons une condition logique
        if (condition.getLogical() != null) {
            Logical logical = condition.getLogical();
            boolean result = logical.getOperator().equals("AND");

            for (Comparison comp : logical.getConditions()) {
                boolean evalResult = evaluateComparison(comp.getComparison(), approval);
                // Pour AND, tous doivent être vrais
                // Pour OR, au moins un doit être vrai
                if (logical.getOperator().equals("AND")) {
                    result = result && evalResult;
                } else if (logical.getOperator().equals("OR")) {
                    result = result || evalResult;
                }
            }
            return result;
        } else if (condition.getComparison() != null) {
            // Si c'est une seule condition
            return evaluateComparison(condition.getComparison(), approval);
        }
        return false;
    }

    private static  boolean evaluateComparison(ComparisonDetails comparison, Approval approval) {

        String operator = comparison.getOperator();

        String conditionVariable = comparison.getField().getKey();
        String conditionValue = comparison.getField().getValue();
        String variableValue = FieldUtils.getValueOfField(approval.getRequest(), conditionVariable);

        // Assurez-vous que la variable à évaluer correspond à la variable de condition
        if (variableValue == null) {
            return false; // La condition ne s'applique pas à cette variable
        }

        // Évaluer la condition en fonction de l'opérateur
        switch (operator) {
            case "equals":
                return variableValue.equals(conditionValue);
            case "notEquals":
                return !variableValue.equals(conditionValue);
            case "greaterThan":
                return Integer.parseInt(variableValue) > Integer.parseInt(conditionValue);
            case "lessThan":
                return Integer.parseInt(variableValue) < Integer.parseInt(conditionValue);
            case "greaterThanOrEquals":
                return Integer.parseInt(variableValue) >= Integer.parseInt(conditionValue);
            case "lessThanOrEquals":
                return Integer.parseInt(variableValue) <= Integer.parseInt(conditionValue);
            default:
                throw new IllegalArgumentException("Opérateur non reconnu: " + operator);
        }
    }
}
