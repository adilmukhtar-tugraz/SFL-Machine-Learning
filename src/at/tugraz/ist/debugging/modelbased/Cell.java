package at.tugraz.ist.debugging.modelbased;

import at.tugraz.ist.debugging.spreadsheets.datastructures.Coords;
import at.tugraz.ist.debugging.modelbased.SpreadsheetCell;
import at.tugraz.ist.debugging.spreadsheets.expressions.IConstraintExpression;
import at.tugraz.ist.debugging.spreadsheets.expressions.NullExpression;
import at.tugraz.ist.debugging.spreadsheets.expressions.constants.ConstExpression;
import at.tugraz.ist.debugging.spreadsheets.expressions.constants.IntConstant;
import at.tugraz.ist.debugging.spreadsheets.expressions.evaluation.EvaluationException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.ss.usermodel.CellValue;

/**
 * Represents an Excel cell
 * 
 */
public class Cell extends SpreadsheetCell {

    public Cell(){}
    
	/**
	 * Creates a null cell, i.e. a cell including a NullExpression instance
	 * 
	 * @param column
	 * @param row
	 * @param index
	 */
	public Cell(int column, int row, int index) {
		super(new Coords(index, row, column), null, new NullExpression(), "");
	}

	/**
	 * 
	 * @param c column
	 * @param r row
	 * @param s sheet index
	 * @param cellValue
	 * @param expression
	 * @param cellFormula
	 *            string representation of the formula
	 */
	public Cell(Coords coords, CellValue cellValue,
			IConstraintExpression expression, String cellFormula) {
		super(coords, cellValue, expression, cellFormula);

	}


	/**
	 * Evaluates the expression tree and returns an object representing the
	 * result. The object type is determined dynamically by the expression's
	 * evaluation method, and can be Bool, Integer, Double, String.
	 * 
	 * @return Result object
	 */
	public Object evaluate() {
		if (this.expression == null)
			throw new InvalidOperationException(
					"Cannot evaluate cell since there is no expression available.");

		try {
			Object result = expression.evaluate();
			return result;
		} catch (EvaluationException e) {
			throw e.setCell(getCoords()).setExpression(
					expression.toString());
		}
	}





	/**
	 * Looks for conditional expressions in the expression tree and returns the
	 * set of all found expressions
	 * 
	 * @return
	 */
	public Set<IConstraintExpression> getConditionalExpressions() {
		if (expression != null)
			return expression.getConditionalExpressions();
		return new HashSet<IConstraintExpression>();

	}
	
	/**
	 * Returns a set of cells which are needed in order to determine the
	 * evaluation result for each possible path through the expression tree
	 * 
	 * @return
	 */
	public Set<Cell> getAllReferencesRecursive() {
		
		Set<Cell> referenced = new HashSet<Cell>();
		
		//recursive
		for (Cell cell : expression.getReferencedCells(false, true))
		{
			referenced.add(cell);
			referenced.addAll(cell.getAllReferencesRecursive());
		}
		return referenced;
	}

	public void setExpression(IConstraintExpression expression) {
		super.expression = expression;
		this.expression = expression;
	}

	@Override
	public String toString() {
		return /*"Cell " + */coords.getPOIStringWithSheetPrefix();
	}
	
	
}
