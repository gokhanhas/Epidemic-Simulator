/**
 * @author Gökhan Has
 */
package model;

/**
 * It is the enum that keeps the information in which direction individuals will go.
 * Since the detailed directions are not specified in the PDF, it is calculated that
 * it can only go in four different directions.
 */
public enum IndividualDirections {
    RIGHT,
    LEFT,
    BOTTOM,
    TOP
}
